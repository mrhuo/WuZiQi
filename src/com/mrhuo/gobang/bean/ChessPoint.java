/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

import java.awt.*;
import java.io.Serializable;

import static com.mrhuo.gobang.common.CONSTANT.*;

/**
 * 棋盘坐标
 * x: 0 - 14
 * y: 0 - 14
 */
public class ChessPoint implements Serializable {
    private int x, y;

    public ChessPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * 将鼠标点击坐标转化为棋盘坐标
     *
     * @param point
     * @return
     */
    public static ChessPoint transformPoint(Point point) {
        int x = (int) Math.round((point.getX() - offsetSizeX) / gridSize * 1.0);
        int y = (int) Math.round((point.getY() - offsetSizeY) / gridSize * 1.0);
        return new ChessPoint(x, y);
    }

    /**
     * 将棋盘坐标转化为界面坐标
     *
     * @param point
     * @return
     */
    public static Point transformToClientPoint(ChessPoint point) {
        return transformToClientPoint(point.getX(), point.getY());
    }

    public static Point transformToClientPoint(int px, int py) {
        int x = offsetSizeX + px * gridSize - 10;
        int y = offsetSizeY + py * gridSize - 10;
        return new Point(x, y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x +
                ", " + y +
                ")";
    }
}
