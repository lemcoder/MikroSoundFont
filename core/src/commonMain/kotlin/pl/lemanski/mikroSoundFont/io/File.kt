package pl.lemanski.mikroSoundFont.io

expect fun loadFile(path: String): ByteArray

expect fun saveFile(path: String, byteArray: ByteArray)