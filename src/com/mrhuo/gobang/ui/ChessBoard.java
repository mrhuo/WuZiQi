/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.ui;

import com.mrhuo.gobang.bean.GameAction;
import com.mrhuo.gobang.bean.GameData;
import com.mrhuo.gobang.common.CONSTANT;
import com.mrhuo.gobang.bean.ChessColor;
import com.mrhuo.gobang.bean.ChessPoint;
import com.mrhuo.gobang.events.DropChessListener;
import com.mrhuo.gobang.events.OnReceiveServerActionListener;
import com.mrhuo.gobang.logic.GameLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.net.Socket;

import static com.mrhuo.gobang.common.CONSTANT.*;

/**
 * 棋盘
 */
public class ChessBoard extends JPanel implements DropChessListener, OnReceiveServerActionListener {

    private GameInfo gameInfo;
    public ChessBoard(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
        this.setSize(500, 500);
        this.setBackground(CONSTANT.defaultChessBoradColor);
        GameLogic.getInstance().setDropChessListener(this);
        GameLogic.getInstance().setOnReceiveServerActionListener(this);

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                GameLogic.getInstance().moveOnBoard(e.getPoint(), getGraphics());
                repaint();
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameLogic.getInstance().dropChess(e.getPoint());
                repaint();
            }
        });
    }

    /**
     * 重写 paint 方法
     *
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
     *
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
     *
     * @param g
     */
    private void paintChessMan(Graphics g) {
        ChessColor[][] currentChess = GameLogic.getInstance().getChess();
        Image image;
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                ChessColor color = currentChess[i][j];
                Point point = ChessPoint.transformToClientPoint(i, j);
                if (color != null) {
                    image = CONSTANT.getImage(color == ChessColor.BLACK ? "black" : "white");
                    g.drawImage(image,
                            (int) point.getX(),
                            (int) point.getY(),
                            20, 20, null);
                }
            }
        }
    }

    @Override
    public void onDropChess(int x, int y, ChessColor color) {
        this.gameInfo.updateGameStatus("轮到" +
                GameLogic.getInstance().getCurrentChessColor().getChessColorCNName() +
                "方下棋"
        );
        repaint();
    }

    @Override
    public void onReceiveServerAction(Socket clientSocket, GameAction gameAction, GameData gameData) {
        System.out.println(GameLogic.getInstance().getCurrentChessColor());
        this.gameInfo.updateGameStatus("轮到" +
                GameLogic.getInstance().getCurrentChessColor().getChessColorCNName() +
                "方下棋"
        );
        repaint();
    }
}