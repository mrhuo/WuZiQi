/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * 游戏信息（用户信息，游戏状态信息）
 */
public class GameInfo extends JPanel{
    private JLabel userInfo;
    private JLabel gameStatus;
    public GameInfo(){
        this.setBackground(new Color(240, 240, 190));
        this.setBorder(new EmptyBorder(15, 60, 0, 70));
        this.setLayout(new BorderLayout());

        this.userInfo = new JLabel("未登录");
        this.gameStatus = new JLabel("游戏未开始");

        this.add(this.userInfo, BorderLayout.WEST);
        this.add(this.gameStatus, BorderLayout.EAST);
    }

    public void updateUserInfo(String userInfo){
        this.userInfo.setText(userInfo);
    }

    public void updateGameStatus(String gameStatus){
        this.gameStatus.setText(gameStatus);
    }
}
