package pl.lemanski.pandamidi.sequencer

interface Sequencer {
    fun setSoundFont(path: String)
    fun setSequence(sequence: Sequence)
    fun nextChunk(): ShortArray
}

expect fun getSequencer(): Sequencer