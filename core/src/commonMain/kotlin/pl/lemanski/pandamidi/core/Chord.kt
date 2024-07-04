package pl.lemanski.pandamidi.core

sealed interface Chord {
    fun notes(): List<Note>
    fun chordType(): Type

    enum class Type {
        MAJOR,
        MINOR
    }
}