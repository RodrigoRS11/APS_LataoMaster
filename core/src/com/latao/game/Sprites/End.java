package com.latao.game.Sprites;

import com.badlogic.gdx.math.Rectangle;
import com.latao.game.LataoMaster;
import com.latao.game.Screens.PlayScreen;

public class End extends InteractiveTileObject{
    public End(PlayScreen screen, Rectangle bounds){
        super(screen, bounds);
        fixture.setUserData(this);
        setCategoryFilter(LataoMaster.END_BIT);
    }

    @Override
    public void onHeadHit(Latao latao) {
    }
}
