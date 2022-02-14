package com.ke.app_update.lib

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.ke.app_update.lib.databinding.AppUpdateLibProgressDialogBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class AppUpdateManager {


    /**
     * 开始下载
     */
    suspend fun start(
        context: Context,
        params: AppUpdateParams,
        downloader: Downloader = DownloaderImpl(),
        apkInstaller: ApkInstaller = ApkInstallerImpl(),
        viewBuilder: (Context) -> View = {
            AppUpdateLibProgressDialogBinding.inflate(LayoutInflater.from(context)).root
        },
        onProgressUpdate: (Int, View) -> Unit = { progress, view ->
            view.findViewById<ProgressBar>(R.id.progress).apply {
                isIndeterminate = false
                setProgress(progress)
            }
        },
        callback: (UpdateResult) -> Unit
    ) {
        withContext(Dispatchers.IO) {
            try {
                val dialogAndView = withContext(Dispatchers.Main) {
                    val view =
                        viewBuilder(context)
                    val dialog = AlertDialog.Builder(context)
                        .setView(view)
                        .setCancelable(false)
                        .show()

                    dialog to view
                }

                if (!params.savePath.exists()) {
                    params.savePath.mkdir()
                }

                val apkFile = File(params.savePath, params.apkName)
                if (apkFile.exists()) {
                    //文件存在就删除
                    apkFile.delete()
                }



                downloader.download(apkFile, params.url) {

                    when (it) {
                        is DownloadResult.Downloading -> {
                            withContext(Dispatchers.Main) {
                                callback(UpdateResult.Progress(it.progress))
                                onProgressUpdate(it.progress, dialogAndView.second)
                            }

                        }
                        DownloadResult.Success -> {
                            withContext(Dispatchers.Main) {
                                dialogAndView.first.dismiss()
                                callback(UpdateResult.Success)
                                apkInstaller.install(context, apkFile)
                            }
                        }
                    }
                }

            } catch (e: Exception) {
                //发生了错误
                callback(UpdateResult.Error(e))
            }
        }
    }
}