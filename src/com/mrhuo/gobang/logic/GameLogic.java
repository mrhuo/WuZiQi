/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.logic;

import com.mrhuo.gobang.bean.*;
import com.mrhuo.gobang.common.CONSTANT;
import com.mrhuo.gobang.events.DropChessListener;
import com.mrhuo.gobang.events.LoginListener;
import com.mrhuo.gobang.events.OnReceiveServerActionListener;
import com.mrhuo.gobang.ui.LoginFrame;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

import static com.mrhuo.gobang.common.CONSTANT.*;

public class GameLogic {

    private static GameLogic gameLogicInstance;
    private User currentUser;
    private LoginListener loginListener;
    private DropChessListener dropChessListener;
    private OnReceiveServerActionListener onReceiveServerActionListener;
    private ChessColor[][] chessColors;
    //当前正在下的旗子颜色
    private ChessColor currentChessColor = ChessColor.BLACK;
    private boolean isGameStart = false;
    private GameMode gameMode = null;
    private Socket clientSocket;

    public ChessColor getCurrentChessColor() {
        return currentChessColor;
    }

    private GameLogic() {
        this.chessColors = new ChessColor[15][15];
    }

    /**
     * 单例模式
     *
     * @return
     */
    public synchronized static GameLogic getInstance() {
        if (gameLogicInstance == null) {
            gameLogicInstance = new GameLogic();
        }
        return gameLogicInstance;
    }

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public void setOnReceiveServerActionListener(OnReceiveServerActionListener listener){
        this.onReceiveServerActionListener = listener;
    }

    public void setDropChessListener(DropChessListener dropChessListener) {
        this.dropChessListener = dropChessListener;
    }

    /**
     * 创建到服务器的连接
     * @return
     */
    private synchronized boolean startSocket(){
        try {
            this.clientSocket = new Socket(CONSTANT.serverAddress, CONSTANT.serverPort);

            Thread receiveThread = new Thread(new clientReceiveThread(this.clientSocket));
            receiveThread.start();

            //Thread heartBeatThread = new Thread(new heartBeatThread(this.clientSocket));
            //heartBeatThread.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void login() {
        if (this.loginListener == null) {
            return;
        }
        LoginFrame loginFrame = new LoginFrame(null, true);
        loginFrame.setVisible(true);
        User user = loginFrame.getLoginedUser();
        if (user == null) {
            this.loginListener.onLoginFailed("取消登录");
            return;
        }
        if (!startSocket()) {
            CONSTANT.alertUser("连接服务器失败，无法登录，稍后再试~~");
            return;
        }
        try{
            CONSTANT.sendData(this.clientSocket.getOutputStream(),
                    GameData.createData(GameAction.USER_LOGIN)
                            .setUser(user)
            );
            this.currentUser = user;
            this.loginListener.onLoginSuccess(user);
        }catch (Exception ex){
            CONSTANT.alertUser("登录失败，稍后再试~~");
            ex.printStackTrace();
        }
    }

    public void logout() {
        if (this.loginListener == null) {
            return;
        }
        try{
            CONSTANT.sendData(this.clientSocket.getOutputStream(),
                    GameData.createData(GameAction.USER_LOGOUT)
                            .setUser(this.currentUser)
            );
            this.clientSocket.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        this.chessColors = new ChessColor[15][15];
        this.currentUser = null;
        this.loginListener.onLoginFailed("退出登录");
    }

    /**
     * 鼠标在棋盘上移动
     *
     * @param point
     */
    public void moveOnBoard(Point point, Graphics g) {
        if (isAvaliableArea(point)) {
            ChessPoint chessPoint = ChessPoint.transformPoint(point);
            int x = chessPoint.getX();
            int y = chessPoint.getY();
            Image image = CONSTANT.getImage("empty");
            g.drawImage(image, offsetSizeX + x * gridSize - 10, offsetSizeY + y * gridSize - 10, 20, 20, null);
        }
    }

    /**
     * 交换下棋
     */
    public void turnSwap(){
        if (currentChessColor == ChessColor.BLACK) {
            currentChessColor = ChessColor.WHITE;
        } else if (currentChessColor == ChessColor.WHITE) {
            currentChessColor = ChessColor.BLACK;
        }
        System.out.println("交换场地：" + currentChessColor.getChessColorCNName() + "方");
    }

    /**
     * 在鼠标点击区域落子
     *
     * @param point
     */
    public void dropChess(Point point) {
        if (!this.isUserLogined()) {
            CONSTANT.alertUser("请您先登录！");
            return;
        }
        if (!this.isGameStart()) {
            CONSTANT.alertUser("游戏还未开始！");
            return;
        }
        if (currentChessColor != this.getCurrentUser().getChessColor()) {
            System.out.println("当前轮到：" + currentChessColor + "，不是你");
            return;
        }
        System.out.println("OK, 游戏已经开始，正在判断点击区域");
        if (!isAvaliableArea(point)) {
            System.out.println("点击无效区域：" + point);
            return;
        }
        ChessPoint chessPoint = ChessPoint.transformPoint(point);
        System.out.println("转换后的棋盘坐标：" + chessPoint);
        if (this.hasChessOnPoint(chessPoint)) {
            System.out.println("坐标：" + chessPoint + "已经有了棋子");
            return;
        }
        //如果当前位置没有棋子
        System.out.println("坐标：" + chessPoint + "还没有棋子，开始下棋");
        this.putChess(chessPoint);
        if (this.dropChessListener != null) {
            this.dropChessListener.onDropChess(chessPoint.getX(), chessPoint.getY(), currentChessColor);
        }
    }

    /**
     * 游戏是否开始
     *
     * @return
     */
    public boolean isGameStart() {
        return this.isGameStart;
    }

    /**
     * 用户是否已登录
     *
     * @return
     */
    public boolean isUserLogined() {
        return this.getCurrentUser() != null;
    }

    /**
     * 开启新的一盘游戏
     */
    public void startNewGame(GameMode gameMode, ChessColor userChessColor) {
        this.chessColors = new ChessColor[15][15];
        this.isGameStart = true;
        this.gameMode = gameMode;
        this.currentUser.setChessColor(userChessColor);
    }

    /**
     * 获取当前游戏模式
     *
     * @return
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * 获得当前棋盘状况
     *
     * @return
     */
    public ChessColor[][] getChess() {
        return this.chessColors;
    }

    /**
     * 获取当前登录用户信息
     *
     * @return
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * 落子逻辑
     *
     * @param point
     */
    private void putChess(ChessPoint point) {
        System.out.println("putChess: (" + point.getX() + ", " + point.getY() + ") " + currentChessColor.getChessColorName());
        try{
            GameData gameData = GameData.createData(GameAction.USER_DROP_CHESS)
                    .setUser(this.currentUser)
                    .setPoint(point);
            CONSTANT.sendData(this.clientSocket.getOutputStream(), gameData);
            System.out.println("已发送数据到服务器：" + gameData);
        }catch (Exception ex){
            CONSTANT.alertUser("发送数据到服务器失败，稍后再试~~");
            ex.printStackTrace();
        }

        this.chessColors[point.getX()][point.getY()] = currentChessColor;
        this.turnSwap();
    }

    /**
     * 点击区域是否有效
     *
     * @param point
     * @return
     */
    private boolean isAvaliableArea(Point point) {
        double x = point.getX();
        double y = point.getY();
        int maxSize = 15 * gridSize;

        if (x >= offsetSizeX - 10 &&
                x <= maxSize + offsetSizeX - 10 &&
                y >= offsetSizeY - 10 &&
                y <= maxSize + offsetSizeY - 10) {
            return true;
        }
        return false;
    }

    /**
     * 落子处是否已经有了棋子
     *
     * @param point 这里的坐标点是转换后的棋盘内的坐标
     * @return
     */
    private boolean hasChessOnPoint(ChessPoint point) {
        return this.chessColors[point.getX()][point.getY()] != null;
    }

    /**
     * 心跳线程
     */
    class heartBeatThread implements Runnable {
        private Socket socket;

        public heartBeatThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            while (CONSTANT.isSocketConnected(socket)) {
                try {
                    Thread.sleep(30000);
                    CONSTANT.sendData(
                            socket.getOutputStream(),
                            GameData.createData(GameAction.HEART_BEAT).setUser(getCurrentUser()));
                } catch (Exception e) {
                    e.printStackTrace();
                    socket = null;
                    break;
                }
            }
        }
    }

    class clientReceiveThread implements Runnable {
        private Socket socket;

        public clientReceiveThread(Socket socket){
            this.socket = socket;
        }

        @Override
        public void run() {
            while (CONSTANT.isSocketConnected(socket)) {
                try {
                    GameData gameData = CONSTANT.receiveData(socket.getInputStream());
                    if (gameData.getAction() == GameAction.USER_DROP_CHESS) {
                        if (!gameData.getUser().equals(currentUser)) {
                            System.out.println("收到消息：" + gameData);
                            //putChess(gameData.getPoint());
                            chessColors[gameData.getPoint().getX()][gameData.getPoint().getY()]
                                    = gameData.getUser().getChessColor();
                            turnSwap();
                            if (onReceiveServerActionListener != null) {
                                onReceiveServerActionListener.onReceiveServerAction(
                                        socket,
                                        gameData.getAction(),
                                        gameData);
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    socket = null;
                    break;
                }
            }
            System.out.println("SOCKET 已断开~");
        }
    }
}