/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.events;

import com.mrhuo.gobang.bean.GameAction;
import com.mrhuo.gobang.bean.GameData;

import java.net.Socket;

public interface OnReceiveServerActionListener {
    void onReceiveServerAction(Socket clientSocket, GameAction gameAction, GameData gameData);
}
