/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

import java.io.Serializable;

/**
 * 棋子颜色
 */
public enum ChessColor implements Serializable {
    BLACK(1, "black", "黑"),
    WHITE(2, "white", "白");

    private final int chessColorValue;
    private final String chessColorName;
    private final String chessCNColorName;

    ChessColor(int color, String colorName, String colorCNName) {
        this.chessColorValue = color;
        this.chessColorName = colorName;
        this.chessCNColorName = colorCNName;
    }

    public int getChessColor() {
        return this.chessColorValue;
    }

    public String getChessColorName() {
        return this.chessColorName;
    }

    public String getChessColorCNName() {
        return this.chessCNColorName;
    }
}
