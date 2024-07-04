package pl.lemanski.pandamidi.sequencer

@JvmInline
value class Velocity(val value: Int) {
    companion object {
        val MIN = Velocity(0)
        val MAX = Velocity(127)
    }

    init {
        require(value in 0..127) {
            "Velocity is 7 bits"
        }
    }
}