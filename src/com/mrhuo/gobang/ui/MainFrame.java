/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.ui;

import com.mrhuo.gobang.bean.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 主界面
 */
public class MainFrame extends JFrame {

    private JMenuBar menuBar;
    private JMenu systemFunction;
    private JMenuItem loginMenu;
    private JMenuItem peopleWithPeopleFight;
    private JMenu peopleWithRobotFight;
    private JMenuItem peopleHoldBlack;
    private JMenuItem peopleHoldWhite;

    private ChessBoard chessBoard;
    private GameInfo gameInfo;

    private User user;

    public MainFrame() {
        super("社会大学习惯 - 五子棋 v1.0.1");
        this.init();
    }

    /**
     * 初始化界面
     */
    private void init(){
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setResizable(false);

        this.buildMenuBar();
        this.buildMainContent();
        this.addEventListener();

        this.setVisible(true);
    }

    /**
     * 增加事件
     */
    private void addEventListener(){
        this.loginMenu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loginMenu.getText().equals("登录")) {
                    LoginFrame loginFrame = new LoginFrame(MainFrame.this, true);
                    loginFrame.setVisible(true);
                    setUser(loginFrame.getLoginedUser());
                    loginMenu.setText("退出");
                } else {
                    setUser(null);
                    loginMenu.setText("登录");
                }
            }
        });
    }

    /**
     * 构造菜单条
     */
    private void buildMenuBar(){
        this.menuBar = new JMenuBar();
        this.systemFunction = new JMenu("系统功能");
        this.loginMenu = new JMenuItem("登录");
        this.peopleWithPeopleFight = new JMenuItem("人人对弈");
        this.peopleWithRobotFight = new JMenu("人机对弈");
        this.peopleHoldBlack = new JMenuItem("人执黑");
        this.peopleHoldWhite = new JMenuItem("人执白");

        //人机对弈下有两个子菜单
        //  人机对弈：
        //  |---人执黑
        //  |---人执白
        this.peopleWithRobotFight.add(this.peopleHoldBlack);
        this.peopleWithRobotFight.add(this.peopleHoldWhite);
        this.peopleWithRobotFight.setVisible(false);

        //系统功能下有两个子菜单：
        //  系统功能：
        //  |---登录
        //  |---人人对弈
        //  |---人机对弈
        this.peopleWithPeopleFight.setVisible(false);
        this.peopleWithRobotFight.setVisible(false);
        this.systemFunction.add(this.peopleWithPeopleFight);
        this.systemFunction.add(this.peopleWithRobotFight);
        this.systemFunction.add(this.loginMenu);

        //最终菜单条将是：
        //  系统功能：
        //  |---人人对弈
        //  |---人机对弈：
        //      |---人执黑
        //      |---人执白
        this.menuBar.add(this.systemFunction);
        this.setJMenuBar(this.menuBar);
    }

    /**
     * 构造主界面内容
     */
    private void buildMainContent(){
        this.chessBoard = new ChessBoard();
        this.gameInfo = new GameInfo();

        this.add(this.chessBoard, BorderLayout.CENTER);
        this.add(this.gameInfo, BorderLayout.NORTH);
    }

    /**
     * 获取当前用户
     * @return
     */
    public User getUser() {
        return user;
    }

    /**
     * 设置当前用户
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
        if (this.user == null) {
            this.gameInfo.updateUserInfo("未登录");
            return;
        }
        this.gameInfo.updateUserInfo("欢迎您，" + this.user.getName());
    }
}
