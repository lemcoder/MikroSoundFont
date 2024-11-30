package io.github.lemcoder.mikrosoundfont

actual fun getLogger(): MikroSoundFontLogger = object : MikroSoundFontLogger {
    override fun log(message: String) {
        println(message)
    }
}