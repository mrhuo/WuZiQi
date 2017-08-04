/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.ui;

import javax.swing.*;
import java.awt.*;

/**
 * 棋盘
 */
public class ChessBoard extends JPanel {

    private final int gridSize = 30;
    private final int offsetSizeX = 90;
    private final int offsetSizeY = 40;

    public ChessBoard() {
        this.setSize(500, 500);
        this.setBackground(new Color(240, 240, 190));
    }

    /**
     * 重写 paint 方法
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        paintGrid(g);
        paintChessMan(g);
    }

    /**
     * 画棋盘
     * @param g2
     */
    private void paintGrid(Graphics g2) {
        Graphics2D g = (Graphics2D) g2;
        for (int i = 0; i < 15; i++) {
            //边界线比较粗
            if (i == 0 || i == 14) {
                g.setStroke(new BasicStroke(2.0f));
            } else {
                g.setStroke(new BasicStroke(1.0f));
            }
            g.drawLine(offsetSizeX + 0, offsetSizeY + i * gridSize, offsetSizeX + 14 * gridSize, offsetSizeY + i * gridSize);
            g.drawLine(offsetSizeX + i * 30, offsetSizeY + 0, offsetSizeX + i * gridSize, offsetSizeY + 14 * gridSize);

            //画棋盘上的黑点和最中间的点
            if (i == 3 || i == 11 || i == 7) {
                g.fillOval(offsetSizeX + i * gridSize - 3, offsetSizeY + i * gridSize - 3, 6, 6);
                //第四行和第十二行有两个点
                if (i == 3) {
                    g.fillOval(offsetSizeX + i * gridSize - 3, offsetSizeY + 11 * gridSize - 3, 6, 6);
                }
                if (i == 11) {
                    g.fillOval(offsetSizeX + i * gridSize - 3, offsetSizeY + 3 * gridSize - 3, 6, 6);
                }
            }

            //画边界线上的数字
            g.drawString((i + 1) + "", offsetSizeX + i * gridSize - 4, offsetSizeY - 8);
            if (i < 9) {
                g.drawString((i + 1) + "", offsetSizeX - 14, offsetSizeY + i * gridSize + 6);
            } else {
                g.drawString((i + 1) + "", offsetSizeX - 20, offsetSizeY + i * gridSize + 6);
            }
        }
    }

    /**
     * 画棋子
     * @param g
     */
    private void paintChessMan(Graphics g) {
    }
}
