package pl.lemanski.mikroSoundFont.io

expect fun loadFile(path: String): ByteArray

expect fun saveFile(byteArray: ByteArray, path: String)