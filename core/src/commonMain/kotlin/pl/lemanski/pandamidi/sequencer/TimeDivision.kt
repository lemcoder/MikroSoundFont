package pl.lemanski.pandamidi.sequencer

enum class TimeDivision(val value: Double) {
    THIRTY_TWO(8.0),
    SIXTEENTH(4.0),
    EIGHTH(2.0),
    QUARTER(1.0),
    HALF(0.5),
    WHOLE(0.25)
}