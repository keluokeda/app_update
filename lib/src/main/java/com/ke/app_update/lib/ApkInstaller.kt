package com.ke.app_update.lib

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.MainThread
import androidx.core.content.FileProvider
import java.io.File

/**
 * apk安装器
 */
interface ApkInstaller {

    @MainThread
    fun install(context: Context, apkFile: File)
}

class ApkInstallerImpl : ApkInstaller {

    override fun install(context: Context, apkFile: File) {

        val intent = Intent(Intent.ACTION_VIEW)

        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            FileProvider.getUriForFile(
                context,
                context.packageName + ".app_update.provider",
                apkFile
            )
        } else {
            Uri.fromFile(apkFile)
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive")

        context.startActivity(intent)
    }
}