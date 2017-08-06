/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.events;

import com.mrhuo.gobang.bean.GameAction;
import com.mrhuo.gobang.bean.GameData;

import java.net.Socket;

/**
 * 服务器事件监听器
 */
public interface OnServerActionListener {
    void onAction(GameAction gameAction, GameData gameData, Socket clientSocket);
    void onServerError(GameAction gameAction, Socket clientSocket, Exception ex);
}
