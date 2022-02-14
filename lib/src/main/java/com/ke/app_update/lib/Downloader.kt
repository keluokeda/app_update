package com.ke.app_update.lib

import android.util.Log
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * 文件下载器
 */
interface Downloader {

    /**
     * 下载文件
     * @param fileName 保存的文件名
     * @param 文件的远程地址
     * @param update 更新进度
     */
    suspend fun download(
        fileName: File,
        url: String,
        update: suspend (DownloadResult) -> Unit
    )
}

class DownloaderImpl : Downloader {
    override suspend fun download(
        fileName: File,
        url: String,
        update: suspend (DownloadResult) -> Unit
    ) {
        val fileOutputStream = FileOutputStream(fileName)
        val httpURLConnection = URL(url).openConnection()
        httpURLConnection.connect()

        val inputStream = httpURLConnection.getInputStream()
        val bufferedInputStream = BufferedInputStream(inputStream)
        val length = httpURLConnection.contentLength

        var len:Int = 0
        var total:Int = 0
        val byteArray = ByteArray(1024*8)
        while ((bufferedInputStream.read(byteArray).also { len = it }) != -1) {

            total += len

            Log.d("HTTP","total = $total ,len = $len , all = $length ")
            fileOutputStream.write(byteArray, 0, len)

            //更新进度
            val progress = (total * 100L) / length
            update(DownloadResult.Downloading(progress.toInt()))
        }
        fileOutputStream.close()
        inputStream.close()
        bufferedInputStream.close()
        update(DownloadResult.Success)
    }
}