package pl.lemanski.pandamidi.core

// Triads

fun ChordBuilder.minorTriad(): List<Note> {
    addIntervalToRoot(Interval.MinorThird)
    addIntervalToRoot(Interval.PerfectFifth)
    return build()
}

fun ChordBuilder.majorTriad(): List<Note> {
    addIntervalToRoot(Interval.MajorThird)
    addIntervalToRoot(Interval.PerfectFifth)
    return build()
}

fun ChordBuilder.diminishedTriad(): List<Note> {
    addIntervalToRoot(Interval.MinorThird)
    addIntervalToRoot(Interval.Tritone)
    return build()
}

fun ChordBuilder.augmentedTriad(): List<Note> {
    addIntervalToRoot(Interval.MajorThird)
    addIntervalToRoot(Interval.MinorSixth)
    return build()
}

// 7th

fun ChordBuilder.dominantSeventh(): List<Note> {
    addIntervalToRoot(Interval.MajorThird)
    addIntervalToRoot(Interval.PerfectFifth)
    addIntervalToRoot(Interval.MinorSeventh)
    return build()
}

fun ChordBuilder.minorSeventh(): List<Note> {
    addIntervalToRoot(Interval.MinorThird)
    addIntervalToRoot(Interval.PerfectFifth)
    addIntervalToRoot(Interval.MinorSeventh)
    return build()
}

fun ChordBuilder.majorSeventh(): List<Note> {
    addIntervalToRoot(Interval.MajorThird)
    addIntervalToRoot(Interval.PerfectFifth)
    addIntervalToRoot(Interval.MajorSeventh)
    return build()
}

fun ChordBuilder.halfDiminished(): List<Note> {
    addIntervalToRoot(Interval.MinorThird)
    addIntervalToRoot(Interval.Tritone)
    addIntervalToRoot(Interval.MinorSeventh)
    return build()
}