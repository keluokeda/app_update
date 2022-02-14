package com.ke.app_update.lib

sealed interface DownloadResult {
    data class Downloading(val progress: Int) : DownloadResult

    object Success : DownloadResult
}