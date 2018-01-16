/*
This program works as a pipe or wrapper for connecting various music playback libraries and your programs.
This program at LGPL You can modify this program freely to use various music playback library with your own program.
by TE3 2018
*/

import java.io.*;
import ozmod.*;

public class Player {
	OZMod oz;
	XMPlayer player;
	public void play(String filename){
		oz = new OZMod();
		oz.initOutput();
		player = new XMPlayer();
		player.load(new PipeIn(new File(filename),PipeIn.LITTLEENDIAN));
		player.setLoopable(true);
		player.play();
	}
	public void done(){
		player.done();
		oz = null;
		player = null;
	}
	public void setVolume(float vol){
		player.mainVolume_ = vol;
	}
}
