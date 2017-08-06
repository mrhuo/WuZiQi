/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.ui;

import com.mrhuo.gobang.common.CONSTANT;
import com.mrhuo.gobang.bean.ChessColor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 游戏信息（用户信息，游戏状态信息）
 */
public class GameInfo extends JPanel {
    private JLabel userInfo;
    private JLabel gameStatus;

    public GameInfo() {
        this.setBackground(CONSTANT.defaultChessBoradColor);
        this.setBorder(new EmptyBorder(15, 60, 0, 70));
        this.setLayout(new BorderLayout());

        this.userInfo = new JLabel("未登录");
        this.gameStatus = new JLabel("游戏未开始");

        this.add(this.userInfo, BorderLayout.WEST);
        this.add(this.gameStatus, BorderLayout.EAST);
    }

    /**
     * 更新用户棋子的图片
     * @param chessColor
     */
    public void updateUserChess(ChessColor chessColor){
        if (chessColor != null) {
            ImageIcon imageIcon = CONSTANT.getImageIcon(chessColor.getChessColorName());
            this.userInfo.setIcon(imageIcon);
        } else {
            this.userInfo.setIcon(null);
        }
    }

    /**
     * 更新用户登录状态
     * @param userInfo
     */
    public void updateUserInfo(String userInfo) {
        this.userInfo.setText(userInfo);
    }

    /**
     * 更新游戏状态
     * @param gameStatus
     */
    public void updateGameStatus(String gameStatus) {
        this.gameStatus.setText(gameStatus);
    }
}
