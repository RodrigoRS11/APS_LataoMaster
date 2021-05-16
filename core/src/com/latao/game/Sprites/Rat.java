package com.latao.game.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.latao.game.LataoMaster;
import com.latao.game.Scenes.Hud;
import com.latao.game.Screens.PlayScreen;

public class Rat extends Enemy{
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Rat(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for(int i = 0; i<2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("Rato"), i * 32, 0,32,32));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 32 /LataoMaster.PPM , 32/LataoMaster.PPM );
        setToDestroy = false;
        destroyed = false;
    }

    public void update(float dt){
        stateTime += dt;
        if (setToDestroy && !destroyed){
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("Rato"),32,0,32,32));
            setBounds(getX(),getY(),40/LataoMaster.PPM, 8/LataoMaster.PPM);
            Hud.addScore(200);
            LataoMaster.manager.get("audio/sounds/mixkit-retro-game-notification-212.wav", Sound.class).play();
            stateTime = 0;
        }
        else if (!destroyed) {
            b2body.setLinearVelocity(velocity);
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            setRegion(walkAnimation.getKeyFrame(stateTime, true));
        }
    }

    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(10 / LataoMaster.PPM);
        fdef.filter.categoryBits = LataoMaster.ENEMY_BIT;
        fdef.filter.maskBits = LataoMaster.GROUND_BIT | LataoMaster.COIN_BIT | LataoMaster.BRICK_BIT | LataoMaster.ENEMY_BIT | LataoMaster.OBJECT_BIT | LataoMaster.LATAO_BIT;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //criando a cabeça
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-9, 15).scl(1/LataoMaster.PPM);
        vertice[1] = new Vector2(9, 15).scl(1/LataoMaster.PPM);
        vertice[2] = new Vector2(-5, 5).scl(1/LataoMaster.PPM);
        vertice[3] = new Vector2(5, 5).scl(1/LataoMaster.PPM);
        head.set(vertice);

        fdef.shape = head;
        fdef.restitution = 0.5f;
        fdef.filter.categoryBits = LataoMaster.ENEMY_HEAD_BIT;
        b2body.createFixture(fdef).setUserData(this);

    }

    public void draw(Batch batch){
        if (!destroyed || stateTime < 3)
            super.draw(batch);
    }

    @Override
    public void hitOnHead(Latao latao) {
        setToDestroy = true;
    }

    @Override
    public void hitByEnemy(Enemy enemy) {
        reverseVelocity(true, false);
    }
}
