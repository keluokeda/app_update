package com.ke.app_update.lib

import java.lang.Exception

sealed interface UpdateResult {

    data class Progress(val value: Int) : UpdateResult

    data class Error(val exception: Exception) : UpdateResult

    object Success : UpdateResult
}