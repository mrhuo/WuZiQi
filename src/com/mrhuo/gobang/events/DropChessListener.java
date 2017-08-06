/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.events;

import com.mrhuo.gobang.bean.ChessColor;

public interface DropChessListener {
    void onDropChess(int x, int y, ChessColor color);
}
