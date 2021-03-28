package dev.ian.snakeboi.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

import dev.ian.snakeboi.Direction;
import dev.ian.snakeboi.asset.Asset;
import dev.ian.snakeboi.game.GameInfo;
import dev.ian.snakeboi.menu.Menu;

import static dev.ian.snakeboi.game.GameInfo.SCALE;


public class Snake {

    private static final int INITIAL_BODY_COUNT = 3;

    private LinkedList<Cell> snakeBody;
    private Stack<GameObject> lives;
    private TextureAtlas atlas;
    private Direction dir;
    private Cell head;
    private Cell tail;
    private ArrayList<Integer> swipe_X;
    private ArrayList<Integer> swipe_Y;
    private Menu menu;


    public Snake(TextureAtlas atlas, Menu menu) {
        this.dir = Direction.RIGHT;
        this.atlas = atlas;
        lives = new Stack<GameObject>();
        snakeBody = new LinkedList<Cell>();
        restoreHealth();
        swipe_X = new ArrayList<Integer>();
        swipe_Y = new ArrayList<Integer>();
        init();
        this.menu = menu;
    }

    public void restoreHealth() {
        for (int i = 5; i > 0; i--) {
            GameObject life = new GameObject(Asset.instance().getSprite("heart"));
            life.setSize(120, 120);
            life.setPosition((GameInfo.SCREEN_HEIGHT + 660) - life.getWidth() * i, GameInfo.SCREEN_HEIGHT - life.getHeight() - 10);
            lives.add(life);
        }
    }

    private void init() {
        snakeBody.clear();
        for (int i = INITIAL_BODY_COUNT; i > 0; i--) {
            Cell body = new Cell(Asset.instance().getSprite(getBodyType(i)), SCALE * i, 0);
            snakeBody.add(body);
        }
        dir = Direction.RIGHT;
        head = snakeBody.getFirst().originCenter();
        tail = snakeBody.getLast().originCenter();
    }

    private String getBodyType(int index) {
        if (index == INITIAL_BODY_COUNT) return "snake_head";
        //if (index == 0) return "snake_tail"; //la sprite della coda non ruota, quindi gli setto la sprite quadrata del corpo.
        else return "snake_body";
    }

    public void moveBody() {
        for (int i = snakeBody.size() - 1; i > 0; i--) {
            Cell nextBody = snakeBody.get(i - 1);
            Cell body = snakeBody.get(i);
            body.setPosition(nextBody.getX(), nextBody.getY());
        }
        head.setDirection(dir);
        checkWallCollision();
    }

    public void handleEvents() {

        comandiFrecce();

        comandiSwipe();

        comandiAccelerometro();
    }

    public void comandiFrecce () {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && dir != Direction.DOWN) dir = Direction.UP;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN) && dir != Direction.UP)
            dir = Direction.DOWN;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT) && dir != Direction.RIGHT)
            dir = Direction.LEFT;
        else if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT) && dir != Direction.LEFT)
            dir = Direction.RIGHT;
    }

    public void comandiSwipe () {
        if(Gdx.input.isTouched()) {
            swipe_X.add(Gdx.input.getX());
            swipe_Y.add(Gdx.input.getY());
        }
        else if(swipe_X.size()>=5 && dir != Direction.LEFT && dir != Direction.RIGHT) {
            if ((swipe_X.get(0) > swipe_X.get(4)) && dir != Direction.RIGHT) {
                dir = Direction.LEFT;
            } else if ((swipe_X.get(0) < swipe_X.get(4)) && dir != Direction.LEFT) {
                dir = Direction.RIGHT;
            }
            swipe_X.clear();
            swipe_Y.clear();
        }
        else if(swipe_Y.size()>=5 && dir != Direction.UP && dir != Direction.DOWN) {
            if ((swipe_Y.get(0) > swipe_Y.get(4)) && dir != Direction.DOWN) {
                dir = Direction.UP;
            } else if ((swipe_Y.get(0) < swipe_Y.get(4)) && dir != Direction.UP) {
                dir = Direction.DOWN;
            }
            swipe_X.clear();
            swipe_Y.clear();
        }
        else {
            swipe_X.clear();
            swipe_Y.clear();
        }
    }

    public void comandiAccelerometro () {
        if(Gdx.input.getAccelerometerX() > 5 && dir != Direction.UP && menu.isMode_pressed())
            dir = Direction.DOWN;
        else if(Gdx.input.getAccelerometerX() < -5 && dir != Direction.DOWN && menu.isMode_pressed())
            dir = Direction.UP;
        else if(Gdx.input.getAccelerometerY() > 5 && dir != Direction.LEFT && menu.isMode_pressed())
            dir = Direction.RIGHT;
        else if(Gdx.input.getAccelerometerY() < -5 && dir != Direction.RIGHT && menu.isMode_pressed())
            dir = Direction.LEFT;
    }

    public void render(SpriteBatch batch) {
        for (Cell body : snakeBody) {
            body.draw(batch);
        }
        for (GameObject life : lives) {
            life.draw(batch);
        }
    }

    public boolean isCrash() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.isCollide(snakeBody.get(i))) return true;
        }
        return false;
    }

    private void checkWallCollision() {
        if (head.getY() > GameInfo.SCREEN_HEIGHT-56) head.setY(0);
        if (head.getY() < 0) head.setY(GameInfo.SCREEN_HEIGHT-56);
        if (head.getX() > GameInfo.SCREEN_HEIGHT-56) head.setX(0);
        if (head.getX() < 0) head.setX(GameInfo.SCREEN_HEIGHT-56);
    }


    public boolean isFoodTouch(GameObject food) {
        return this.snakeBody.getFirst().isCollide(food);
    }

    public void grow() {
        snakeBody.getLast().sprite.setRegion(atlas.getRegions().get(12));
        Cell body = new Cell(atlas.createSprite("snake_body"), tail.getX(), tail.getY());
        snakeBody.add(body);
        tail = snakeBody.getLast().originCenter();
        System.out.println(snakeBody.size());
    }

    public boolean hasLive() {
        return !lives.isEmpty();
    }

    public LinkedList<Cell> getBody() {
        return snakeBody;
    }

    public void popLife() {
        lives.pop();
    }

    public void reset() {
        init();
    }
}
