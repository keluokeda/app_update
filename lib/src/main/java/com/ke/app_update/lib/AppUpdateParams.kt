package com.ke.app_update.lib

import java.io.File

data class AppUpdateParams(
    /**
     * apk的地址
     */
    val url: String,
    /**
     * 保存路径 不包含文件名
     */
    val savePath: File,

    /**
     * 保存apk的文件名 例如 mache.apk
     */
    val apkName: String
)