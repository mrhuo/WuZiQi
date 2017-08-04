/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.ui;

import com.mrhuo.gobang.bean.CONSTANT;
import com.mrhuo.gobang.bean.ChessColor;
import com.mrhuo.gobang.bean.User;
import sun.swing.UIAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JDialog {

    private JLabel labelForUserName;
    private JTextField textFieldForUserName;
    private JButton buttonForLogin;
    private JButton buttonForRandomName;
    private User loginedUser;

    public LoginFrame(JFrame mainFrame, boolean b) {
        super(mainFrame, b);
        init();
    }

    /**
     * 初始化方法
     */
    private void init() {
        this.setSize(300, 150);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setLayout(null);
        this.setTitle("登录");
        //this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.labelForUserName = new JLabel("昵称：");
        this.textFieldForUserName = new JTextField();
        this.textFieldForUserName.setText(CONSTANT.randomName());
        this.buttonForLogin = new JButton("登录");
        this.buttonForLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        this.buttonForRandomName = new JButton("刷新");
        this.buttonForRandomName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textFieldForUserName.setText(CONSTANT.randomName());
            }
        });

        this.labelForUserName.setBounds(30, 20, 50, 25);
        this.textFieldForUserName.setBounds(70, 20, 120, 25);
        this.buttonForRandomName.setBounds(200, 20, 60, 25);
        this.buttonForLogin.setBounds(70, 70, 190, 25);

        this.add(labelForUserName);
        this.add(textFieldForUserName);
        this.add(buttonForRandomName);
        this.add(buttonForLogin);
        //this.setVisible(true);
    }

    private void login(){
        if (this.textFieldForUserName.getText().equals("")) {
            JOptionPane.showMessageDialog(this,"用户昵称不能为空！");
            return;
        }
        this.loginedUser = new User(this.textFieldForUserName.getText(), ChessColor.NULL);
        this.setVisible(false);
    }

    public User getLoginedUser(){
        return this.loginedUser;
    }
}
