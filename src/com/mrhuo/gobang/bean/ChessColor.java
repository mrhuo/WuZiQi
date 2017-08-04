/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

/**
 * 棋子颜色
 */
public enum ChessColor {
    NULL(0, ""),
    BLACK(1, "黑"),
    WHITE(2, "白");

    private final int chessColorValue;
    private final String chessColorName;

    ChessColor(int color, String colorName) {
        this.chessColorValue = color;
        this.chessColorName = colorName;
    }

    public int getChessColor() {
        return this.chessColorValue;
    }

    public String getChessColorName() {
        return this.chessColorName;
    }
}
