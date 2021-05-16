package com.latao.game.Tools;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.latao.game.LataoMaster;
import com.latao.game.Sprites.Enemy;
import com.latao.game.Sprites.InteractiveTileObject;
import com.latao.game.Sprites.Latao;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cDef){
            case LataoMaster.LATAO_HEAD_BIT |LataoMaster.COIN_BIT:
                if(fixA.getFilterData().categoryBits == LataoMaster.LATAO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Latao) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Latao) fixB.getUserData());
                break;
            case LataoMaster.LATAO_HEAD_BIT |LataoMaster.BRICK_BIT:
                if(fixA.getFilterData().categoryBits == LataoMaster.LATAO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Latao) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Latao) fixB.getUserData());
                break;
            case LataoMaster.ENEMY_HEAD_BIT | LataoMaster.LATAO_BIT:
                if(fixA.getFilterData().categoryBits == LataoMaster.ENEMY_HEAD_BIT)
                    ((Enemy)fixA.getUserData()).hitOnHead((Latao) fixB.getUserData());
                else
                    ((Enemy)fixB.getUserData()).hitOnHead((Latao) fixA.getUserData());
                break;
            case LataoMaster.ENEMY_BIT | LataoMaster.BRICK_BIT:
                if (fixA.getFilterData().categoryBits == LataoMaster.ENEMY_BIT)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case LataoMaster.ENEMY_BIT | LataoMaster.ENEMY_BIT:
                ((Enemy)fixA.getUserData()).hitByEnemy((Enemy)fixB.getUserData());
                ((Enemy)fixB.getUserData()).hitByEnemy((Enemy)fixA.getUserData());
                break;
            case LataoMaster.LATAO_BIT | LataoMaster.ENEMY_BIT:
                if(fixA.getFilterData().categoryBits == LataoMaster.LATAO_BIT)
                    ((Latao) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Latao) fixB.getUserData()).hit((Enemy)fixA.getUserData());
                break;
            case LataoMaster.LATAO_BIT | LataoMaster.END_BIT:
                if(fixA.getFilterData().categoryBits == LataoMaster.LATAO_BIT)
                    ((Latao) fixA.getUserData()).hitWin();
                else
                    ((Latao) fixB.getUserData()).hitWin();
                break;

        }
    }
        @Override
        public void endContact (Contact contact){ }

        @Override
        public void preSolve (Contact contact, Manifold oldManifold){ }

        @Override
        public void postSolve (Contact contact, ContactImpulse impulse){ }
}
