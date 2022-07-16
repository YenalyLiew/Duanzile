package com.yenaly.duanzile

import com.yenaly.yenaly_libs.utils.aesDecrypt
import com.yenaly.yenaly_libs.utils.decodeToByteArrayByBase64

internal fun String.ftpDecrypt(): String {
    return if (startsWith("ftp://")) {
        String(
            this.substringAfter("ftp://")
                .decodeToByteArrayByBase64()
                .aesDecrypt(DECRYPT_KEY.toByteArray(), algorithm = "AES/ECB/PKCS5Padding")
        )
    } else this
}

internal fun login(token: String) {
    loginToken = token
    isLogin = true
}

internal fun logout() {
    loginToken = ""
    isLogin = false
}