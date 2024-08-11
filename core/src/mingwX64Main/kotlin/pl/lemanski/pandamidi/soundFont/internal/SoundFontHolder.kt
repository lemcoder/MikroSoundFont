package pl.lemanski.pandamidi.soundFont.internal

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi

internal object SoundFontHolder {
    @OptIn(ExperimentalForeignApi::class)
    var tsf: CPointer<*>? = null
}