#!/usr/bin/env python3
"""Turns an .sf2 into an .sf3 by compressing every sample to Ogg Vorbis.

Used to make the SF3 test fixture (needs `pip install soundfile numpy`):
    python3 scripts/make-test-sf3.py in.sf2 out.sf3

SF3 layout (as read by TinySoundFont and MuseScore): each sample is a standalone Ogg Vorbis
stream in the smpl chunk, its shdr start/end are byte offsets of that stream, its loop points
are sample frames relative to the sample's start, and sampleType has the 0x10 (Vorbis) flag.
"""
import io
import struct
import sys

import numpy
import soundfile

SHDR = struct.Struct("<20sIIIIIBbHH")


def chunks(data):
    pos = 0
    while pos + 8 <= len(data):
        cid, size = struct.unpack_from("<4sI", data, pos)
        yield cid, data[pos + 8:pos + 8 + size]
        pos += 8 + size + (size & 1)


def chunk(cid, body):
    return struct.pack("<4sI", cid, len(body)) + body + (b"\0" if len(body) & 1 else b"")


def list_chunk(kind, body):
    return chunk(b"LIST", kind + body)


def encode(pcm, rate):
    ogg = io.BytesIO()
    soundfile.write(ogg, numpy.frombuffer(pcm, "<i2"), rate, format="OGG", subtype="VORBIS")
    return ogg.getvalue()


def main(src, dst):
    data = open(src, "rb").read()
    assert data[:4] == b"RIFF" and data[8:12] == b"sfbk", "not a SoundFont"
    lists = {body[:4]: body[4:] for cid, body in chunks(data[12:]) if cid == b"LIST"}
    smpl = dict(chunks(lists[b"sdta"]))[b"smpl"]
    pdta = list(chunks(lists[b"pdta"]))

    shdr = dict(pdta)[b"shdr"]
    records = [list(SHDR.unpack_from(shdr, i)) for i in range(0, len(shdr), SHDR.size)]
    out = bytearray()
    for rec in records[:-1]:  # the last record is the terminal "EOS"
        start, end, loop_start, loop_end, rate = rec[1:6]
        ogg = encode(smpl[start * 2:end * 2], rate)
        rec[1:5] = [len(out), len(out) + len(ogg), loop_start - start, loop_end - start]
        rec[9] |= 0x10
        out += ogg
    new_shdr = b"".join(SHDR.pack(*rec) for rec in records)

    sdta = list_chunk(b"sdta", chunk(b"smpl", bytes(out)))
    pdta_body = b"".join(chunk(cid, new_shdr if cid == b"shdr" else body) for cid, body in pdta)
    body = b"sfbk" + list_chunk(b"INFO", lists[b"INFO"]) + sdta + list_chunk(b"pdta", pdta_body)
    open(dst, "wb").write(b"RIFF" + struct.pack("<I", len(body)) + body)


if __name__ == "__main__":
    main(*sys.argv[1:3])
