/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

import java.net.Socket;

/**
 * 维护了用户和用户对应是socket
 */
public class UserSocket {
    private User user;
    private Socket socket;
    private boolean master;

    public UserSocket() {
    }

    public UserSocket(User user, Socket socket) {
        this(user, socket, false);
    }

    public UserSocket(User user, Socket socket, boolean master) {
        this.user = user;
        this.socket = socket;
        this.master = master;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ChessColor getChessColor() {
        return user.getChessColor();
    }

    public boolean isMaster() {
        return master;
    }

    public void setMaster(boolean master) {
        this.master = master;
    }
}
