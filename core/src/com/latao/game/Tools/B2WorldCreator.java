package com.latao.game.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.latao.game.LataoMaster;
import com.latao.game.Screens.PlayScreen;
import com.latao.game.Sprites.Brick;
import com.latao.game.Sprites.Coin;
import com.latao.game.Sprites.End;
import com.latao.game.Sprites.Rat;

public class B2WorldCreator {
    private Array<Rat> rats;

    public B2WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        //criando o ground
        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2) / LataoMaster.PPM, (rect.getY() + rect.getHeight() / 2) / LataoMaster.PPM);

            body = world.createBody(bdef);

            shape.setAsBox((rect.getWidth() / 2) / LataoMaster.PPM, (rect.getHeight() / 2) / LataoMaster.PPM);
            fdef.shape = shape;
            fdef.filter.categoryBits = LataoMaster.OBJECT_BIT;
            body.createFixture(fdef);
        }

        //criando o brick
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Brick(screen, rect);

        }

        //criando a moeda
        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Coin( screen, rect);
        }

        //criando os ratos
        rats = new Array<Rat>();
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            rats.add(new Rat(screen, rect.getX() / LataoMaster.PPM, rect.getY() / LataoMaster.PPM));
        }

        //criando o final
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new End(screen, rect);

        }
    }

    public Array<Rat> getRats() {
        return rats;
    }
}
