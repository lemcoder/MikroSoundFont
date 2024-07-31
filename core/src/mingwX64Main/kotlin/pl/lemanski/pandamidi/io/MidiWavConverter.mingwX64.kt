package pl.lemanski.pandamidi.io

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.memScoped
import pl.lemanski.pandamidi.util.getCwd

@OptIn(ExperimentalForeignApi::class)
actual fun getMidiWavConverter(): MidiWavConverter {
    return object : MidiWavConverter {
        override fun generate(soundFontPath: String, midiFilePath: String): String {
            val pwd = getCwd()
            val sanitizedSoundFontPath = soundFontPath.replace("/", "\\")
            val sanitizedMidiFilePath = midiFilePath.replace("/", "\\")

            val midiFileName = sanitizedMidiFilePath.split("\\").last().replace(".mid", "")
            val wavFilePath = "$pwd\\$midiFileName.wav"
            memScoped {
                libmwc.initialize_soundfont(sanitizedSoundFontPath)
                libmwc.generate(sanitizedMidiFilePath, wavFilePath)
                libmwc.release_soundfont()
            }
            return wavFilePath
        }
    }
}