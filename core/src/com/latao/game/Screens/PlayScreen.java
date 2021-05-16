package com.latao.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthoCachedTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;

import com.badlogic.gdx.utils.viewport.Viewport;
import com.latao.game.LataoMaster;
import com.latao.game.Scenes.Hud;
import com.latao.game.Sprites.Enemy;
import com.latao.game.Sprites.Latao;
import com.latao.game.Sprites.Rat;
import com.latao.game.Tools.B2WorldCreator;
import com.latao.game.Tools.WorldContactListener;

public class PlayScreen implements Screen {
    //referencia para o jogo
    private LataoMaster game;
    private TextureAtlas atlas;
    private Latao player;

    //musica
    private Music music;

    //variaveis da tela
    private OrthographicCamera gamecam;
    private Viewport gamePort;
    private Hud hud;

    //tiled map variaveis
    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    //box2d variaveis
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;

    public PlayScreen(LataoMaster game){
        atlas = new TextureAtlas("Lixo_Rato.pack");

        this.game = game;

        gamecam = new OrthographicCamera();

        gamePort = new FitViewport(LataoMaster.V_WIDTH / LataoMaster.PPM,LataoMaster.V_HEIGHT / LataoMaster.PPM,gamecam);

        hud = new Hud(game.batch);

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("mapa1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / LataoMaster.PPM);

        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);


        world = new World(new Vector2(0,-10), true);

        b2dr = new Box2DDebugRenderer();

        creator = new B2WorldCreator(this);

        player = new Latao( this);

        world.setContactListener(new WorldContactListener());

        music = LataoMaster.manager.get("audio/music/bensound-highoctane.ogg", Music.class);
        music.setLooping(true);
        music.play();

    }

    public TextureAtlas getAtlas(){
        return atlas;
    }
    @Override
    public void show() {

    }

    public void handleinput(float dt){
        if (player.currentState != Latao.State.DEAD) {
            if (Gdx.input.isKeyJustPressed(Input.Keys.UP))
                player.b2body.applyLinearImpulse(new Vector2(0, 4f), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 2)
                player.b2body.applyLinearImpulse(new Vector2(0.1f, 0), player.b2body.getWorldCenter(), true);
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -2)
                player.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    public void update(float dt){
        handleinput(dt);

        world.step(1/60f, 6, 2);

        player.update(dt);
        for(Enemy enemy : creator.getRats()) {
            enemy.update(dt);
            if (enemy.getX() < player.getX() + 224/LataoMaster.PPM)
                enemy.b2body.setActive(true);
        }

        hud.update(dt);

        if (player.currentState != Latao.State.DEAD){
            gamecam.position.x =player.b2body.getPosition().x;
        }

        gamecam.update();
        renderer.setView(gamecam);

    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.render();

        b2dr.render(world, gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(Enemy enemy : creator.getRats())
            enemy.draw(game.batch);

        game.batch.end();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if (gameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }

        if (gameWim()){
            game.setScreen(new GameWinScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);

    }

    public boolean gameOver(){
        if (player.currentState == Latao.State.DEAD && player.getStateTimer() > 3){
            return true;
        }
        return false;
    }

    public boolean gameWim(){
        if(player.currentState == Latao.State.WIN){
            return true;
        }
        return false;
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld(){
        return world;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();

    }

    public Hud getHud() {
        return hud;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }
}
