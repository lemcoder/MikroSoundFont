package pl.lemanski.pandamidi.io

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ULongVar
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readBytes
import kotlinx.cinterop.refTo
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.value
import platform.posix.malloc

@OptIn(ExperimentalForeignApi::class)
actual fun getMidiProcessor(): MidiProcessor {
    return object : MidiProcessor {
        private var audioCallback: (ByteArray) -> Unit = { }

        override fun setAudioCallback(callback: (ByteArray) -> Unit) {
            audioCallback = callback
        }

        override fun setSoundFontFromPath(soundFontPath: String) {
            val sanitizedSoundFontPath = soundFontPath.replace("/", "\\")
            libmwc.initialize_soundfont(sanitizedSoundFontPath)
        }

        override suspend fun processMidiBytes(midiBytes: ByteArray) {
            memScoped {
                val pOutputBufferSize: CPointer<ULongVar> = malloc(8u)?.reinterpret() ?: throw RuntimeException("Memory allocation failed")
                val pUByteArray = midiBytes.toUByteArray().refTo(0)
                val midiSize = midiBytes.size.toULong()

                val buffer = libmwc.generate_from_midi_buffer(pUByteArray, midiSize, pOutputBufferSize) ?: throw RuntimeException("Failed to generate audio buffer")
                val bufferBytes = buffer.readBytes(pOutputBufferSize.pointed.value.toInt())

                audioCallback(bufferBytes)
            }
        }

        override fun close() {
            libmwc.release_soundfont()
        }
    }
}