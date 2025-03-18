
## Kotlin Multiplatform SoundFont library
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)  
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)

Library for [sample-based synthesis](https://en.wikipedia.org/wiki/Sample-based_synthesis "Sample-based synthesis") to generate audio data from [MIDI](https://en.wikipedia.org/wiki/MIDI "MIDI") files. 
The library allows to load `.sf2` files from filesystem and from memory. Is basically a KMP wrapper around [TinySoundFont](https://github.com/schellingb/TinySoundFont) library.

The goal of this library is to allow operations on .sf2 files and generating audio in Kotlin Multiplatform projects. 
The library has `batteries-included` so no configuration is required.

Non goals:
* <b>Audio playback</b>
* Audio file export (such as WAV or MP3)
* File I/O operations
* Audio format conversion
* Resampling, mixing etc...

Platform support:
| Android |iOS | Mingw X64 | Linux X64 | MacOs | Wasm | JS |
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
val sfBuffer   // .sf2 file bytes
    
val soundFont = MikroSoundFont.load(sfBuffer)
```

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE.md](LICENSE.md) file for details
