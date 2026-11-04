
## Kotlin Multiplatform SoundFont library
[![Kotlin](https://img.shields.io/badge/kotlin-2.4.21-blue.svg?logo=kotlin)](http://kotlinlang.org)  
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Library for [sample-based synthesis](https://en.wikipedia.org/wiki/Sample-based_synthesis "Sample-based synthesis") to generate audio data. 
The library allows to load `.sf2` and `.sf3` files from memory. Is basically a KMP wrapper around [TinySoundFont](https://github.com/schellingb/TinySoundFont) library.

The goal of this library is to allow operations on .sf2 files and generating audio in Kotlin Multiplatform projects. 
The library has `batteries-included` so no configuration is required.

Non goals:
* <b>Audio playback</b>
* Audio file export (such as WAV or MP3)
* File I/O operations
* Audio format conversion
* Resampling, mixing etc...

Platform support:
| Android |iOS | Mingw X64 | Linux X64 | MacOs (Apple silicon) | Wasm | JS |
|--|--|--|--|--|--|--|
|:heavy_check_mark:|:heavy_check_mark:|:heavy_check_mark:|:heavy_check_mark:|:heavy_check_mark:|:hourglass:|:heavy_multiplication_x:|

___
### Installation

This library is stored on Maven Central repository. To add library to Your project paste the following snippet in your TOML file.
```
[versions]
mikrosoundfont = "<latest_version>"

[libraries]
mikrosoundfont = { module = "io.github.lemcoder.mikrosoundfont:soundfont", version.ref = "mikrosoundfont" }
```
___
### Usage
```
val sfBuffer   // .sf2 or .sf3 file bytes
    
MikroSoundFont.load(sfBuffer).use { soundFont ->
    // .. use soundFont to generate audio data
}
```

A `SoundFont` holds its samples in native memory until `close()` is called, so close it (or load it in `use { }`) when you are done with it.

#### SF3 files
`.sf3` files store their samples compressed with Ogg Vorbis. `load` decodes all of them up front, so:
* loading takes noticeably longer than an `.sf2` of the same size (seconds for a large font on a phone), so load off the main thread;
* the loaded `SoundFont` takes several times the file size in memory, as the samples are kept decoded.

Decoding uses [stb_vorbis](https://github.com/nothings/stb), which is not hardened against malformed data, so only load `.sf3` files you trust.

## Testing

The tests in `soundfont/src/commonTest` load an `.sf2` and an `.sf3` font and render audio through the native library:
* `./gradlew :soundfont:linuxX64Test` or `:soundfont:macosArm64Test` runs them on the host (CI runs the Linux ones on every pull request);
* `./gradlew :soundfont:connectedAndroidDeviceTest` runs them on a connected Android device or emulator.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details
