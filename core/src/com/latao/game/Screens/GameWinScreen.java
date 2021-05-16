package com.latao.game.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.latao.game.LataoMaster;
import com.latao.game.Scenes.Hud;

public class GameWinScreen implements Screen {
    private Viewport viewport;
    private Stage stage;

    private Game game;

    public GameWinScreen(Game game){
        this.game = game;
        viewport = new FitViewport(LataoMaster.V_WIDTH, LataoMaster.V_HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, ((LataoMaster)game).batch);

        Label.LabelStyle font = new Label.LabelStyle(new BitmapFont(), Color.BLACK);

        Table table = new Table();
        table.center();
        table.setFillParent(true);

        Label gameWinLabel = new Label("GAME WIN", font);
        Label gameWinLabel2 = new Label("TUDO RECICLADO" , font);
        Label gameWinLabel3 = new Label("Score: "+Hud.getScore() , font);
        Label playAgainLabel = new Label("jogar novamente", font);

        table.add(gameWinLabel).expand();
        table.row();
        table.add(gameWinLabel2).expand();
        table.row();
        table.add(gameWinLabel3).expand();
        table.row();
        table.add(playAgainLabel).expandX().padTop(10f);

        stage.addActor(table);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            game.setScreen(new PlayScreen((LataoMaster)game));
            dispose();
        }
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {

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
        stage.dispose();

    }
}
