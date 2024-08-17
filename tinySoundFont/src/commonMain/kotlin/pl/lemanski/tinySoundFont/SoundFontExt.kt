package pl.lemanski.tinySoundFont

expect fun soundFont(path: String): SoundFont

expect fun soundFont(memory: ByteArray): SoundFont

external fun dupa()