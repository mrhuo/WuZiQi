/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.bean;

import java.io.Serializable;

public enum GameAction implements Serializable {
    USER_LOGIN {
        @Override
        public String getActionName() {
            return "登录";
        }
    },
    USER_LOGOUT {
        @Override
        public String getActionName() {
            return "退出";
        }
    },
    USER_JOIN_TABLE {
        @Override
        public String getActionName() {
            return "加入游戏桌";
        }
    },
    USER_LEAVE_TABLE {
        @Override
        public String getActionName() {
            return "离开游戏桌";
        }
    },
    USER_DROP_CHESS {
        @Override
        public String getActionName() {
            return "落子";
        }
    },
    SERVER_RELOGIN {
        @Override
        public String getActionName() {
            return "重复登录";
        }
    },
    SERVER_BORADCAST {
        @Override
        public String getActionName() {
            return "服务器广播";
        }
    },
    SERVER_START_ERROR {
        @Override
        public String getActionName() {
            return "服务器启动失败";
        }
    },
    SERVER_START_SUCCESS {
        @Override
        public String getActionName() {
            return "服务器启动成功";
        }
    },
    SERVER_STOP {
        @Override
        public String getActionName() {
            return "服务器停止";
        }
    },
    UNKNOWN {
        @Override
        public String getActionName() {
            return "未知错误";
        }
    },
    HEART_BEAT {
        @Override
        public String getActionName() {
            return "心跳";
        }
    };

    public abstract String getActionName();
}
