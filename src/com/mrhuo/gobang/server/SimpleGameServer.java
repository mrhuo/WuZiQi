/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.server;

import com.mrhuo.gobang.bean.GameAction;
import com.mrhuo.gobang.bean.GameData;
import com.mrhuo.gobang.common.CONSTANT;
import com.mrhuo.gobang.events.OnServerActionListener;
import com.mrhuo.gobang.logic.ServerLogic;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class SimpleGameServer implements OnServerActionListener {

    private ServerLogic serverLogic;

    public SimpleGameServer(int serverPort) {
        this.serverLogic = new ServerLogic(serverPort);
        this.serverLogic.setOnServerActionListener(this);
    }

    /**
     * 服务器入口
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        SimpleGameServer server = new SimpleGameServer(CONSTANT.serverPort);
        server.start();

        System.out.println("按 ENTER 停止服务器...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        server.stop();
    }

    /**
     * 启动服务
     */
    public void start() {
        this.serverLogic.start();
    }

    /**
     * 结束服务
     */
    public void stop() {
        this.serverLogic.stop();
    }

    /**
     * 当服务器接收到任意事件之后的处理：
     * 如：  收到玩家登录之后，广播给所有人，XX上线了 等
     *
     * @param gameAction
     * @param gameData
     */
    @Override
    public void onAction(GameAction gameAction, GameData gameData, Socket clientSocket) {
        System.out.println("SERVER-ACTION: " + gameAction.getActionName() + ", data = " + gameData);
        if (GameAction.SERVER_START_SUCCESS.equals(gameAction)) {
            System.out.println("Game server started at " + CONSTANT.serverAddress + ":" + CONSTANT.serverPort);
        } else if (GameAction.USER_LOGIN.equals(gameAction) || GameAction.USER_LOGOUT.equals(gameAction)) {
            //给服务器所有玩家广播一条用户登录信息
            this.serverLogic.boradcastMessageWithoutSomeones(gameData.getUser(), GameData.
                    createData(gameAction).
                    setUser(gameData.getUser())
            );
            if (GameAction.USER_LOGIN.equals(gameAction)) {
                this.serverLogic.newUser(gameData.getUser(), clientSocket);
            } else {
                this.serverLogic.userLeave(gameData.getUser());
            }
        } else if (GameAction.HEART_BEAT.equals(gameAction) && gameData.getUser() != null) {
            System.out.println("收到心跳包：" + gameData.getUser().getName() + ", " + new Date());
        } else if (GameAction.USER_DROP_CHESS.equals(gameAction)) {
            this.serverLogic.boradcastMessageWithoutSomeones(gameData.getUser(), gameData);
        }
        System.out.println("服务器当前人数：" + this.serverLogic.userCount());
    }

    @Override
    public void onServerError(GameAction gameAction, Socket currentSocket, Exception ex) {
        System.out.println("SERVER-ERROR: " + gameAction.getActionName() + (ex != null ? " " + ex.getMessage() : ""));
        this.serverLogic.removeUserBySocket(currentSocket);
        System.out.println("服务器当前人数：" + this.serverLogic.userCount());
        if (ex != null) {
            //ex.printStackTrace();
        }
        if (ex instanceof StreamCorruptedException) {
            if (currentSocket != null) {
                try {
                    currentSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("SERVER: 收到非法连接，已强制关闭.");
                }
            }
        }
    }
}