#!/usr/bin/env sh
# Fails unless every 64-bit native library in the given AAR has 16 KB aligned LOAD segments,
# which Google Play requires for Android 15+ devices with 16 KB pages.
# Usage: scripts/check-16kb-alignment.sh path/to/library.aar
set -eu

aar=$1
readelf=$(ls "${ANDROID_NDK_HOME:?set ANDROID_NDK_HOME}"/toolchains/llvm/prebuilt/*/bin/llvm-readelf | head -1)
work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
unzip -q "$aar" 'jni/*' -d "$work"

status=0
for so in "$work"/jni/arm64-v8a/*.so "$work"/jni/x86_64/*.so; do
    [ -e "$so" ] || { echo "missing 64-bit libraries in $aar"; exit 1; }
    for align in $("$readelf" -lW "$so" | awk '$1 == "LOAD" { print $NF }'); do
        if [ $((align)) -lt 16384 ]; then
            echo "FAIL ${so#"$work"/}: LOAD segment aligned to $align"
            status=1
        fi
    done
    [ $status -eq 0 ] && echo "ok   ${so#"$work"/}"
done
exit $status
