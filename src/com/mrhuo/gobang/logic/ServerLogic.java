/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.logic;

import com.mrhuo.gobang.bean.DeskTable;
import com.mrhuo.gobang.bean.GameAction;
import com.mrhuo.gobang.bean.GameData;
import com.mrhuo.gobang.bean.User;
import com.mrhuo.gobang.common.CONSTANT;
import com.mrhuo.gobang.events.OnServerActionListener;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器逻辑
 */
public class ServerLogic {
    private Map<User, Socket> onlineUsers;
    private Map<DeskTable, Thread> deskTableThreads;
    private OnServerActionListener onServerActionListener;
    private ServerSocket serverSocket;
    private boolean isServerRunning = false;

    public ServerLogic(int serverPort) {
        this.onlineUsers = new HashMap<>();
        this.deskTableThreads = new HashMap<>();
        try {
            this.serverSocket = new ServerSocket(serverPort);
        } catch (Exception e) {
            this.dispatchServerErrorEvent(GameAction.SERVER_START_ERROR, null, e);
        }
    }

    /**
     * 向在线用户列表中插入数据
     * @param user
     * @param socket
     * @return
     */
    public boolean newUser(User user, Socket socket){
        if (this.onlineUsers.containsKey(user)) {
            return false;
        }
        this.onlineUsers.put(user, socket);
        return true;
    }

    /**
     * 向在线用户列表中插入数据
     * @param user
     * @return
     */
    public void userLeave(User user){
        if (this.onlineUsers.containsKey(user)) {
            this.onlineUsers.remove(user);
        }
    }

    /**
     * 移除指定连接的用户
     * @param socket
     */
    public void removeUserBySocket(Socket socket){
        if (socket == null) {
            return;
        }
        for (Map.Entry<User, Socket> entry : this.onlineUsers.entrySet()) {
            if (socket.equals(entry.getValue())) {
                this.onlineUsers.remove(entry.getKey());
                return;
            }
        }
    }

    /**
     * 在线人数总数
     * @return
     */
    public int userCount(){
        return this.onlineUsers.size();
    }

    /**
     * 启动服务器逻辑
     */
    public void start() {
        if (this.serverSocket == null) {
            this.dispatchServerErrorEvent(GameAction.SERVER_START_ERROR,
                    null,
                    new Exception("服务器未启动，无法调用 start() 方法"));
            return;
        }
        isServerRunning = true;
        Thread serverThread = new Thread(new ServerThread(this.serverSocket));
        serverThread.start();
    }

    /**
     * 停止服务器
     */
    public void stop() {
        isServerRunning = false;
        try {
            this.dispatchEvent(GameAction.SERVER_STOP, null);
            this.serverSocket.close();
        } catch (IOException ignore) {
        }
    }

    public void setOnServerActionListener(OnServerActionListener onServerActionListener) {
        this.onServerActionListener = onServerActionListener;
    }

    /**
     * 广播消息
     *
     * @param gameData
     */
    public void boradcastMessage(GameData gameData) {
        //this.dispatchEvent(GameAction.SERVER_BORADCAST, gameData);
        for (Map.Entry<User, Socket> entry : this.onlineUsers.entrySet()) {
            this.sendToUser(entry.getKey(), gameData);
        }
    }

    /**
     * 广播给服务器所有人，除了列表中的人
     *
     * @param users
     */
    public void boradcastMessageWithoutSomeones(List<User> users, GameData gameData) {
        //this.dispatchEvent(GameAction.SERVER_BORADCAST, gameData);
        for (Map.Entry<User, Socket> entry : this.onlineUsers.entrySet()) {
            if (users.contains(entry.getKey())) {
                continue;
            }
            this.sendToUser(entry.getKey(), gameData);
        }
    }

    /**
     * 广播给服务器所有人，除了某个人
     *
     * @param user
     */
    public void boradcastMessageWithoutSomeones(User user, GameData gameData) {
       // this.dispatchEvent(GameAction.SERVER_BORADCAST, gameData);
        for (Map.Entry<User, Socket> entry : this.onlineUsers.entrySet()) {
            if (user == entry.getKey()) {
                continue;
            }
            this.sendToUser(entry.getKey(), gameData);
        }
    }

    /**
     * 发送消息给某个用户
     *
     * @param user
     * @param gameData
     */
    public void sendToUser(User user, GameData gameData) {
        if (user == null || gameData == null) {
            return;
        }
        Socket socket = null;
        try {
            socket = this.onlineUsers.get(user);
            CONSTANT.sendData(socket.getOutputStream(), gameData);
        } catch (Exception ex) {
            this.dispatchServerErrorEvent(GameAction.UNKNOWN, socket, ex);
        }
    }

    /**
     * 分发事件
     *
     * @param gameAction
     * @param gameData
     * @param socket
     */
    private void dispatchEvent(GameAction gameAction, GameData gameData, Socket socket) {
        if (this.onServerActionListener != null) {
            this.onServerActionListener.onAction(gameAction, gameData, socket);
        }
    }
    /**
     * 分发事件
     *
     * @param gameAction
     * @param gameData
     */
    private void dispatchEvent(GameAction gameAction, GameData gameData) {
        this.dispatchEvent(gameAction, gameData, null);
    }

    /**
     * 分发服务器错误事件
     *
     * @param socket
     * @param ex
     */
    private void dispatchServerErrorEvent(GameAction gameAction, Socket socket, Exception ex) {
        if (this.onServerActionListener != null) {
            this.onServerActionListener.onServerError(gameAction, socket, ex);
        }
    }

    /**
     * 为客户工作的线程，用于收发，判断等
     */
    class ServeClientThread implements Runnable {

        private Socket socket;

        public ServeClientThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            while (true) {
                try{
                    GameData gameData = CONSTANT.receiveData(socket.getInputStream());
                    dispatchEvent(gameData.getAction(), gameData, socket);
                }catch (Exception ex) {
                    //如果没有收到正确的数据，向服务器分发一个未知错误事件
                    dispatchServerErrorEvent(GameAction.UNKNOWN, socket, ex);
                    break;
                }
            }
        }
    }

//    class DeskTableServeThread implements Runnable {
//
//        private DeskTable deskTable;
//        public DeskTableServeThread(DeskTable deskTable){
//            this.deskTable = deskTable;
//        }
//
//        @Override
//        public void run() {
//
//        }
//    }

    /**
     * 服务器主线程，负责接收 socket
     */
    class ServerThread implements Runnable {
        private ServerSocket serverSocket;

        public ServerThread(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            dispatchEvent(GameAction.SERVER_START_SUCCESS, null);
            while (isServerRunning) {
                Socket userSocket = null;
                try {
                    userSocket = serverSocket.accept();

                    Thread serveThread = new Thread(new ServeClientThread(userSocket));
                    serveThread.start();

                } catch (Exception ex) {
                    dispatchServerErrorEvent(GameAction.UNKNOWN, userSocket, ex);
                }
            }
        }
    }
}
