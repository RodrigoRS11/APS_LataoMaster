package com.latao.game.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.latao.game.LataoMaster;
import com.latao.game.Scenes.Hud;
import com.latao.game.Screens.PlayScreen;

public class Coin extends InteractiveTileObject{
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN =2;
    public Coin(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        tileSet = map.getTileSets().getTileSet("Teste");
        fixture.setUserData(this);
        setCategoryFilter(LataoMaster.COIN_BIT);
    }

    @Override
    public void onHeadHit(Latao latao) {
        if(getCell().getTile().getId() == BLANK_COIN){
            LataoMaster.manager.get("audio/sounds/mixkit-player-jumping-in-a-video-game-2043.wav", Sound.class).play();
            Hud.lessScore(100);}
        else {
            LataoMaster.manager.get("audio/sounds/mixkit-arcade-video-game-bonus-2044.wav", Sound.class).play();
            getCell().setTile(tileSet.getTile(BLANK_COIN));
            Hud.addScore(400);
        }
    }
}
