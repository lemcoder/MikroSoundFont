
## Kotlin Multiplatform SoundFont library
[![Kotlin](https://img.shields.io/badge/kotlin-2.0.21-blue.svg?logo=kotlin)](http://kotlinlang.org)  
[![GitHub License](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0)
>:warning: This library is NOT ready for production use.:warning:

Library for [sample-based synthesis](https://en.wikipedia.org/wiki/Sample-based_synthesis "Sample-based synthesis") to play [MIDI](https://en.wikipedia.org/wiki/MIDI "MIDI") files. The library is split in two modules:
* **Midi module**: Allows to read and write standard MIDI files and parsing/encoding MIDI messages. Can generate F32 audio data from MIDI messages.
* **SoundFont module**: Allows to load `.sf2` files from filesystem and from memory. Is basically a KMP wrapper around [TinySoundFont](https://github.com/schellingb/TinySoundFont) library.

The goal of this library is to allow operations on MIDI files and generating audio in Kotlin Multiplatform projects. The library has `batteries-included` so no configuration is required.

Non goals:
* Audio file export (such as WAV or MP3)
* Base File I/O operations
* Audio format conversion
* Resampling, mixing etc...


Platform support:
| Android |iOS | Mingw X64 | Linux X64 |
|--|--|--|--|
| :heavy_check_mark: | :hourglass: | :heavy_check_mark: | :heavy_check_mark: |




___
### Features
This library is in early stage. Full set of features as well as documentation is yet to be released.



___
### Installation
>:warning: This library is not YET available on Maven Central :warning:
> For now use the `msf.zip` file provided in [releases](https://github.com/lemcoder/MikroSoundFont/releases/) and unpack it in [local maven repository](https://www.baeldung.com/maven-local-repository).

To add library to Your project paste the following snippet in your TOML file.
```
[versions]
mikrosoundfont = "<latest_version>"

[libraries]
mikrosoundfont-midi = { module = "pl.lemanski.mikrosoundfont:midi", version.ref = "mikrosoundfont" }  
mikrosoundfont-soundFont = { module = "pl.lemanski.mikrosoundfont:soundFont", version.ref = "mikrosoundfont" }
```
___
### Usage
```
val midiBuffer // .mid file bytes  
val sfBuffer   // .sf2 file bytes
    
val midiMessages = MidiFileParser(midiBuffer).parse().getMessages()    
val soundFont = MikroSoundFont.load(sfBuffer)  

val sampleRate = 44_100
val audioBytes = MidiSequencer(soundFont, 44_100).apply {   
	loadMidiEvents(midiMessages)  
}.generate()

// use audioBytes (e.g. write to .wav file)
```

___

### Useful resources:
* [Sandard MIDI files specification](https://drive.google.com/file/d/1t4jcCCKoi5HMi7YJ6skvZfKcefLhhOgU/view?u)
* [Summary of MIDI messages](https://drive.google.com/file/d/1I-bH8zhfS37fnLzV-xnonOCYZycaGzbn/view)