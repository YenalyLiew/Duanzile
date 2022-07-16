package com.yenaly.duanzile

import com.yenaly.yenaly_libs.utils.getSpValue
import com.yenaly.yenaly_libs.utils.putSpValue

var isLogin: Boolean
    get() = getSpValue("isLogin", false)
    set(value) = putSpValue("isLogin", value)

var loginToken: String
    get() = getSpValue("loginToken", "")
    set(value) = putSpValue("loginToken", value)