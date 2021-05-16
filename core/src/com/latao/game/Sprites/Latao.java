package com.latao.game.Sprites;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.latao.game.LataoMaster;
import com.latao.game.Screens.PlayScreen;

import java.util.Random;


public class Latao extends Sprite {
    public enum State{FALLING, JUMPING, STANDING, RUNNING, DEAD, WIN};
    public State currentState;
    public State previousState;
    public World world;
    public Body b2body;
    private static Integer r;
    private TextureRegion lataoStand;
    private TextureRegion lataoDead;
    private Animation<TextureRegion> lataoRun;
    private Animation<TextureRegion> lataoJump;
    private float stateTimer;
    private boolean runningRight;
    private boolean lataoIsDead;
    private boolean lataoIsWin;
    private PlayScreen screen;

    public Latao(PlayScreen screen){
        this.screen = screen;
        this.world = screen.getWorld();
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        if (changeLatao() == 0) {
            Array<TextureRegion> frames = new Array<TextureRegion>();
            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("LixeiraRed"), 32 * i, 0, 32, 32));
            lataoRun = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 3; i < 4; i++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("LixeiraRed"), i * 32, 0, 32, 32));
            lataoJump = new Animation(0.1f, frames);

            lataoStand = new TextureRegion(screen.getAtlas().findRegion("LixeiraRed"), 0, 0, 32, 32);

            lataoDead = new TextureRegion(screen.getAtlas().findRegion("LixeiraRed"), 0, 0, 32, 32);

            defineLatao();
            setBounds(128, 0, (32 / LataoMaster.PPM) * 2, (32 / LataoMaster.PPM) * 2);
            setRegion(lataoStand);
        }else if (changeLatao() == 2){
            Array<TextureRegion> frames = new Array<TextureRegion>();
            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("LixeiraBlue"), 32 * i, 0, 32, 32));
            lataoRun = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 3; i < 4; i++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("LixeiraBlue"), i * 32, 0, 32, 32));
            lataoJump = new Animation(0.1f, frames);

            lataoStand = new TextureRegion(screen.getAtlas().findRegion("LixeiraBlue"), 0, 0, 32, 32);

            lataoDead = new TextureRegion(screen.getAtlas().findRegion("LixeiraBlue"), 0, 0, 32, 32);

            defineLatao();
            setBounds(128, 0, (32 / LataoMaster.PPM) * 2, (32 / LataoMaster.PPM) * 2);
            setRegion(lataoStand);
        }else{
            Array<TextureRegion> frames = new Array<TextureRegion>();
            for (int i = 0; i < 2; i++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("LixeiraYellow"), 32 * i, 0, 32, 32));
            lataoRun = new Animation(0.1f, frames);
            frames.clear();

            for (int i = 3; i < 4; i++)
                frames.add(new TextureRegion(screen.getAtlas().findRegion("LixeiraYellow"), i * 32, 0, 32, 32));
            lataoJump = new Animation(0.1f, frames);

            lataoStand = new TextureRegion(screen.getAtlas().findRegion("LixeiraYellow"), 0, 0, 32, 32);

            lataoDead = new TextureRegion(screen.getAtlas().findRegion("LixeiraYellow"), 0, 0, 32, 32);

            defineLatao();
            setBounds(128, 0, (32 / LataoMaster.PPM) * 2, (32 / LataoMaster.PPM) * 2);
            setRegion(lataoStand);
        }
    }

    public int changeLatao(){
        Random num = new Random();
        r = num.nextInt(2) * 2;
        return r;
    }

    public void update(float dt){
        if (screen.getHud().isTimeUp() && !isDead()) {
            die();
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        setRegion(getFrame(dt));

    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();

        TextureRegion region;
        switch (currentState){
            case DEAD:
                region = lataoDead;
                break;
            case JUMPING:
                region = lataoJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = lataoRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = lataoStand;
                break;
        }

        if((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()){
            region.flip(true, false);
            runningRight = false;
        }
        else if((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()){
            region.flip(true, false);
            runningRight = true;
        }

        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState(){
        if (lataoIsDead)
            return State.DEAD;
        else if (lataoIsWin)
            return State.WIN;
        else if(b2body.getLinearVelocity().y > 0)
            return State.JUMPING;
        else if(b2body.getLinearVelocity().y < 0)
            return State.JUMPING;
        else if(b2body.getLinearVelocity().x != 0)
            return State.RUNNING;
        else
            return State.STANDING;

    }

    public void defineLatao(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / LataoMaster.PPM, 32 / LataoMaster.PPM );
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(15 / LataoMaster.PPM);
        fdef.filter.categoryBits = LataoMaster.LATAO_BIT;
        fdef.filter.maskBits = LataoMaster.GROUND_BIT | LataoMaster.COIN_BIT | LataoMaster.BRICK_BIT | LataoMaster.ENEMY_BIT | LataoMaster.OBJECT_BIT | LataoMaster.ENEMY_HEAD_BIT | LataoMaster.END_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-4 / LataoMaster.PPM, 16 / LataoMaster.PPM), new Vector2(4 / LataoMaster.PPM, 16 / LataoMaster.PPM));
        fdef.filter.categoryBits = LataoMaster.LATAO_HEAD_BIT;
        fdef.shape = head;
        fdef.isSensor = true;

        b2body.createFixture(fdef).setUserData(this);
    }

    public void hit(Enemy enemy){
        LataoMaster.manager.get("audio/sounds/mixkit-sword-slashes-in-battle-2763.wav", Sound.class).play();
        die();
    }

    public void hitWin(){
        win();
    }
    public void die() {

        if (!isDead()) {
            lataoIsDead = true;
            Filter filter = new Filter();
            filter.maskBits = LataoMaster.NOTHING_BIT;

            for (Fixture fixture : b2body.getFixtureList()) {
                fixture.setFilterData(filter);
            }
            LataoMaster.manager.get("audio/music/bensound-highoctane.ogg", Music.class).stop();
            LataoMaster.manager.get("audio/sounds/mixkit-arcade-retro-game-over-213.wav", Sound.class).play();
            b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
        }
    }

    public void win(){
        if (!isWin()) {
            lataoIsWin = true;
            LataoMaster.manager.get("audio/music/bensound-highoctane.ogg", Music.class).stop();
            LataoMaster.manager.get("audio/sounds/mixkit-video-game-win-2016.wav", Sound.class).play();
        }
    }
    public boolean isDead(){
        return lataoIsDead;
    }

    public boolean isWin(){return lataoIsWin;}

    public float getStateTimer(){
        return stateTimer;
    }
}
