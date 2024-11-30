package io.github.lemcoder.mikrosoundfont

interface MikroSoundFontLogger {
    fun log(message: String)
}

expect fun getLogger(): MikroSoundFontLogger