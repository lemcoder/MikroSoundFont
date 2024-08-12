package pl.lemanski.pandamidi.soundFont

expect fun soundFont(path: String): SoundFont

expect fun soundFont(memory: ByteArray): SoundFont
