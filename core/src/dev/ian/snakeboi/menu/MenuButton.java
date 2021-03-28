package dev.ian.snakeboi.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class MenuButton{
    private Texture button;
    private int x;
    private int y;
    private int width;
    private int height;

    public MenuButton(String texture_path, int x, int y, int width, int height) {
        button = new Texture(texture_path);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void draw(Batch batch) {
        batch.draw(button, x, y, width, height);
    }

    public Boolean isPressed() {
        return Gdx.input.isTouched() && (Gdx.input.getX() >= x && Gdx.input.getX() <= x+height) && (Gdx.graphics.getHeight()-Gdx.input.getY() >= y && Gdx.graphics.getHeight()-Gdx.input.getY() <= (y+width));
    }
}
