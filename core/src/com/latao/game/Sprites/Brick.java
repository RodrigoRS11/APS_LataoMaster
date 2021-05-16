package com.latao.game.Sprites;


import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;
import com.latao.game.LataoMaster;
import com.latao.game.Scenes.Hud;
import com.latao.game.Screens.PlayScreen;

public class Brick extends InteractiveTileObject {
    public Brick(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(LataoMaster.BRICK_BIT);
    }

    @Override
    public void onHeadHit(Latao latao) {
        setCategoryFilter(LataoMaster.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.lessScore(50);
        LataoMaster.manager.get("audio/sounds/mixkit-game-ball-tap-2073.wav", Sound.class).play();

    }
}
