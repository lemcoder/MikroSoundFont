package io.github.lemcoder.mikrosoundfont

sealed class MidiException(override val message: String?) : Exception(message)

class InvalidMidiDataException(override val message: String?) : MidiException(message)

