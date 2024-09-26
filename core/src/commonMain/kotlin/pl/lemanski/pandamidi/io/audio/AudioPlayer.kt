package pl.lemanski.pandamidi.io.audio

import pl.lemanski.mikroaudio.MikroAudio

fun playFromBuffer(byteArray: ByteArray) {
    MikroAudio.playback(byteArray)
}
