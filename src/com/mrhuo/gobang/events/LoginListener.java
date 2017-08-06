/*
 * Copyright  (c) mrhuo.com 2017.
 */

package com.mrhuo.gobang.events;

import com.mrhuo.gobang.bean.User;

public interface LoginListener {
    void onLoginSuccess(User user);

    void onLoginFailed(String message);
}
