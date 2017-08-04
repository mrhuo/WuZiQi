/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

/**
 * 用户类
 */
public class User {
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
}