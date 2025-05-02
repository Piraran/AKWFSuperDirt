# AKWFSuperDirt

A class that generates SuperDirt ready SynthDefs for the wavetable library [AKWF](https://www.adventurekid.se/akrt/waveforms/adventure-kid-waveforms/).

SynthDef created by AFrancob. 


## Usage

```supercollider
s.options.numBuffers = 1024 * 16;
Server.default.options.memSize= 512000*20;
Server.default.options.maxNodes=128*1024;
Server.default.options.numWireBufs= 512;
s.boot;

SuperDirt.start;

AKWFSuperDirt.init(s);
AKWFSuperDirt.synthsList;
Synth(\aguitar);
```

