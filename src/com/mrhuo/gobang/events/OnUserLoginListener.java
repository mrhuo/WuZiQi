/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.events;
import com.mrhuo.gobang.bean.User;

public interface OnUserLoginListener {
    void onLoginSuccess(User user);
    void onLoginOut();
}
