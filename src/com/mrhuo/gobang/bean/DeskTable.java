/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

import java.net.Socket;
import java.util.UUID;

/**
 * 游戏桌
 */
public class DeskTable {
    private String deskTableId;
    private UserSocket blackUser;
    private UserSocket whiteUser;

    public DeskTable(User firstUser, Socket socket) {
        this.deskTableId = UUID.randomUUID().toString();
        if (firstUser.getChessColor() == ChessColor.BLACK) {
            this.blackUser = new UserSocket(firstUser, socket, true);
        } else if (firstUser.getChessColor() == ChessColor.WHITE) {
            this.whiteUser = new UserSocket(firstUser, socket, true);
        }
    }

    public UserSocket getBlackUser() {
        return blackUser;
    }

    public void setBlackUser(UserSocket blackUser) {
        this.blackUser = blackUser;
    }

    public UserSocket getWhiteUser() {
        return whiteUser;
    }

    public void setWhiteUser(UserSocket whiteUser) {
        this.whiteUser = whiteUser;
    }

    //自定义方法
    public boolean isFull() {
        return this.blackUser != null && this.whiteUser != null;
    }

    /**
     * 游戏桌是否为空
     * @return
     */
    public boolean isEmpty(){
        return !isFull();
    }

    public String getDeskTableId() {
        return deskTableId;
    }

    /**
     * 获取当前游戏桌桌主
     * @return
     */
    public UserSocket getMaster(){
        if (this.isBlackMaster()) {
            return this.blackUser;
        }
        return this.whiteUser;
    }

    /**
     * 是否是黑子是游戏桌桌主
     * @return
     */
    public boolean isBlackMaster (){
        return this.blackUser != null && this.blackUser.isMaster();
    }

    /**
     * 是否是白子是游戏桌桌主
     * @return
     */
    public boolean isWhiteMaster(){
        return this.whiteUser != null && this.whiteUser.isMaster();
    }

    /**
     * 获取当前游戏桌上的人数
     * @return
     */
    public int userCount(){
        if (this.isFull()) {
            return 2;
        }
        if (this.blackUser != null || this.whiteUser != null) {
            return 1;
        }
        return 0;
    }

    /**
     * 用户离开游戏桌
     * @param userSocket
     * @throws Exception
     */
    public void leaveTable(UserSocket userSocket) throws Exception{
        if (this.isEmpty()) {
            throw new Exception("当前游戏桌已经无人！");
        }
        //TODO 如果此人是房主，退出后交换房主权利
        if (userSocket.equals(this.blackUser)) {
            this.blackUser = null;
        }
        if (userSocket.equals(this.whiteUser)) {
            this.whiteUser = null;
        }
    }

    /**
     * 加入游戏桌
     * 如果游戏桌当前没有人或者已满，则抛出异常
     * 加入游戏桌后，玩家的棋子颜色会自动根据情况变化
     * @param userSocket
     */
    public void joinTable(UserSocket userSocket) throws Exception {
        if (this.isFull()) {
            throw new Exception("此游戏桌已满，加入失败！");
        }
        UserSocket master = this.getMaster();
        if (master == null) {
            throw new Exception("此游戏桌还没房主，加入失败！");
            //如果没有房主，自己成为房主
            //userSocket.setMaster(true);
        }
        //房主执黑，则玩家棋子自动变白
        if (ChessColor.BLACK == master.getChessColor()) {
            if (ChessColor.WHITE != userSocket.getUser().getChessColor()) {
                //TODO 给用户发送通知，棋子颜色变化
                userSocket.getUser().setChessColor(ChessColor.WHITE);
            }
            this.whiteUser = userSocket;
        } else if (ChessColor.BLACK != userSocket.getUser().getChessColor()) {
            //TODO 给用户发送通知，棋子颜色变化
            userSocket.getUser().setChessColor(ChessColor.BLACK);
            this.blackUser = userSocket;
        }
    }

    /**
     * 加入游戏桌
     */
    public void joinTable(User user, Socket socket) throws Exception{
        this.joinTable(new UserSocket(user, socket));
    }
}
