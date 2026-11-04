package io.github.lemcoder.mikrosoundfont

actual fun readTestResource(name: String): ByteArray =
    object {}.javaClass.classLoader!!.getResourceAsStream(name)?.use { it.readBytes() }
        ?: error("Missing test resource $name")
