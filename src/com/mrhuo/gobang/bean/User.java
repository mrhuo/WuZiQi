/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

import java.io.Serializable;

/**
 * 用户类
 */
public class User implements Serializable{
    private String name;
    private int score;
    private ChessColor chessColor;

    public User() {
    }

    public User(String name, ChessColor color) {
        this.score = 0;
        this.name = name;
        this.chessColor = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void updateScore(int score) {
        this.score += score;
    }

    public void resetScore() {
        this.score = 0;
    }

    public ChessColor getChessColor() {
        return chessColor;
    }

    public void setChessColor(ChessColor chessColor) {
        this.chessColor = chessColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (score != user.score) return false;
        if (!name.equals(user.name)) return false;
        return chessColor == user.chessColor;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + score;
        result = 31 * result + (chessColor != null ? chessColor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", chessColor=" + chessColor +
                '}';
    }
}