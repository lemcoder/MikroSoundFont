package pl.lemanski.pandamidi.io

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.toKString
import kotlinx.cinterop.usePinned
import platform.posix.getcwd

@OptIn(ExperimentalForeignApi::class)
internal fun getCwd(): String? {
    return ByteArray(1024).usePinned { getcwd(it.addressOf(0), 1024) }?.toKString()
}

@OptIn(ExperimentalForeignApi::class)
actual fun getMidiWavConverter(): MidiWavConverter {
    return object : MidiWavConverter {
        override fun generate(soundFontPath: String, midiFilePath: String): String {
            val pwd = getCwd()
            val sanitizedSoundFontPath = soundFontPath.replace("/", "\\")
            val sanitizedMidiFilePath = midiFilePath.replace("/", "\\")

            val midiFileName = sanitizedMidiFilePath.split("\\").last().replace(".mid", "")
            val wavFilePath = "$pwd\\$midiFileName.wav"
            pl.lemanski.mwc.generate(sanitizedSoundFontPath, sanitizedMidiFilePath, wavFilePath)
            return wavFilePath
        }
    }
}