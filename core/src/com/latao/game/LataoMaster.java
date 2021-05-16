package com.latao.game;


import com.badlogic.gdx.Game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.latao.game.Screens.PlayScreen;

public class LataoMaster extends Game {
	public static final int V_WIDTH = 400;
	public static final int V_HEIGHT = 208;
	public static final float PPM = 100;
    public static final short NOTHING_BIT = 0;
	public static final short GROUND_BIT = 1;
	public static final short LATAO_BIT = 2;
	public static final short BRICK_BIT = 4;
	public static final short COIN_BIT = 8;
	public static final short DESTROYED_BIT = 16;
	public static final short OBJECT_BIT = 32;
	public static final short ENEMY_BIT = 64;
	public static final short ENEMY_HEAD_BIT = 128;
	public static final short LATAO_HEAD_BIT = 256;
	public static final short END_BIT = 512;


	public SpriteBatch batch;

	public static AssetManager manager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/music/bensound-highoctane.ogg", Music.class);
		manager.load("audio/sounds/mixkit-arcade-video-game-bonus-2044.wav", Sound.class);
		manager.load("audio/sounds/mixkit-game-ball-tap-2073.wav", Sound.class);
		manager.load("audio/sounds/mixkit-player-jumping-in-a-video-game-2043.wav", Sound.class);
		manager.load("audio/sounds/mixkit-retro-game-notification-212.wav", Sound.class);
		manager.load("audio/sounds/mixkit-sword-slashes-in-battle-2763.wav", Sound.class);
		manager.load("audio/sounds/mixkit-arcade-retro-game-over-213.wav",Sound.class);
		manager.load("audio/sounds/mixkit-video-game-win-2016.wav",Sound.class);


		manager.finishLoading();

		setScreen(new PlayScreen(this));

	}

	@Override
	public void render () {
		super.render();

	}

	@Override
	public void dispose() {
		super.dispose();
		manager.dispose();
		batch.dispose();
	}
}
