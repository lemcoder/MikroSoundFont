package pl.lemanski.pandamidi

import kotlinx.cinterop.ExperimentalForeignApi
import pl.lemanski.pandamidi.gpio.gpioInitialise

@OptIn(ExperimentalForeignApi::class)
actual fun gpioInitialize() {
    gpioInitialise()
}