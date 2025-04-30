AKWFSuperDirt {
	classvar <synthsList;

	*makeSynthDef {|buffersMap|
		buffersMap.do({|item|
			var name = item.name.postln;
			var min = item.buffers[0].bufnum.postln;
			var max = item.buffers.last.bufnum.postln;


			SynthDef(name, {
				| out=0, sustain=1, freq=220, speed=1, begin=0, end=1, pan=0.5, accelerate=0, offset, volume=0.9, inter= 100, phase=1, minw=0.1, maxw=0.15, att=0.01, rel=0.99|
				// var minS = gain.linlin(0,1,min,max);
				// var maxS = pan.linlin(0,1,min,max);
				var minS = minw.linlin(0,1,min,max);
				var maxS = maxw.linlin(0,1,min,max);

				var pitch = freq*speed;

				var env = EnvGen.ar(Env.pairs([[begin,1],[end,1],[end,0]]), timeScale: sustain, doneAction: Done.freeSelf);
				var env2 = EnvGen.ar(Env.perc(att,rel),timeScale:sustain);
				var sig= LPF.ar(VOsc.ar(Line.kr(minS,maxS,sustain*inter), pitch,mul: volume),7500,1);
				sig= LPF.ar(sig, 3500);
				OffsetOut.ar(out,DirtPan.ar(sig*env2, ~dirt.numChannels, pan, env));
			}).add;
		})
	}

	*init {|server|
		var validation = if(server == nil, {"A server instance must be passed to AWKF.init".throw});
		var validation2 = if(~dirt == nil, {"SuperDirt must be initialized before this.".throw});

		var waveShapeCatalogue ="wavetable".resolveRelative.postln;
		var dirs = ["AKWF","_aguitar","_altosax","_birds","_bitreduced","_blended","_bw_saw","_bw_sawbright","_bw_sawgap","_bw_sawrounded","_bw_sin","_bw_sq","_bw_sqrounded","_bw_tri","_c604","_cello","_clarinett","_clavinet","_dbass","_distorted","_ebass","_eguitar","_eorgan","_epiano","_flute","_fmsynth","_granular","_hdrawn","_hvoice","_oboe","_oscchip","_overtone","_piano","_pluckalgo","_raw","_sinharm","_snippets","_stereo","_stringbox","_symetric","_theremin","_vgame","_vgamebasic","_violin"];
		var buffersMap = dirs.collect({|dir|
			var prefix = "AKWF";
			var hasUnderScoreAt0 = dir[0].asString == "_";
			var dirname = if(hasUnderScoreAt0, {prefix++dir}, {dir});
			var path = ("wavetable/"++dirname++"/*").resolveRelative.postln;
			(
				name: if(hasUnderScoreAt0, {dir[1..]}, {prefix}).toLower,
				buffers: SoundFile.collectIntoBuffers(path, server)
			);
		});

		this.makeSynthDef(buffersMap);
		synthsList = buffersMap.collect(_.name);
	}
}


