package pl.lemanski.pandamidi.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.getcwd


@OptIn(ExperimentalForeignApi::class)
internal fun getCwd(): String? {
    return ByteArray(1024).usePinned { getcwd(it.addressOf(0), 1024) }?.toKString()
}