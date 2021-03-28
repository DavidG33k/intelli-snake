package dev.ian.snakeboi.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import dev.ian.snakeboi.asset.Asset;
import dev.ian.snakeboi.asset.SoundPlayer;
import dev.ian.snakeboi.game.SnakeGame;

public class Menu extends Game {

    private SpriteBatch batch;
    private SnakeGame game;

    private boolean start_pressed;
    private boolean mode_pressed;
    private Texture logo;
    private Texture background;
    private MenuButton start;
    private MenuButton mode;
    private MenuButton exit;
    private int logo_time;
    private float transition_logo_fadeIn;
    private float transition_logo_fadeOut;
    private float transition_menu;

    private int WIDTH;
    private int HEIGHT;


    @Override
    public void create() { //Praticamente va al posto del costruttore.
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        Gdx.input.setCatchBackKey(true);

        Asset.instance().loadAsset();
        SoundPlayer.init();
        batch = new SpriteBatch();
        game = new SnakeGame(this);


        WIDTH = Gdx.graphics.getWidth();
        HEIGHT = Gdx.graphics.getHeight();
        start_pressed = false;
        mode_pressed = false;
        logo = new Texture(Asset.LOGO);
        background = new Texture(Asset.MENU_BACKGROUND);
        start = new MenuButton(Asset.START_BUTTON, (WIDTH/2)-230, (HEIGHT/2)-500, 400, 400);
        mode = new MenuButton(Asset.MODE_BUTTON, (WIDTH/2)-700, (HEIGHT/2)-450, 256, 256);
        exit = new MenuButton(Asset.EXIT_BUTTON, (WIDTH/2)+350, (HEIGHT/2)-450, 256, 256);
        logo_time = 0;
        transition_logo_fadeIn = 0;
        transition_logo_fadeOut = 1;
        transition_menu = 0;
    }

    @Override
    public void render() {
        batch.begin();


        if(start_pressed) {
            game.update(Gdx.graphics.getDeltaTime());
            clearScreen();
            game.render(batch);
        }
        else if(logo_time < 250) {
            SoundPlayer.playMusic(Asset.MENU_THEME, false);
            logo_transition_fadeIn();
            batch.draw(logo, 0, 0);
            if(logo_time >= 150)
                logo_transition_fadeOut();
            logo_time++;
        }
        else {
            menu_transition();
            draw();
            update();
        }


        batch.end();
    }

    private void logo_transition_fadeIn() {
        if(transition_logo_fadeIn <= 1) {
            batch.setColor(1, 1, 1, transition_logo_fadeIn);
            transition_logo_fadeIn += 0.01f;
        }
    }

    private void logo_transition_fadeOut() {
        if(transition_logo_fadeOut >= 0) {
            batch.setColor(1, 1, 1, transition_logo_fadeOut);
            transition_logo_fadeOut -= 0.01f;
        }
    }

    private void menu_transition() {
        if(transition_menu <= 1) {
            batch.setColor(1, 1, 1, transition_menu);
            transition_menu += 0.02f;
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void dispose() {
        batch.dispose();
        Asset.instance().dispose();
    }

    public void draw() {
        SoundPlayer.playMusic(Asset.MENU_THEME, false);
        batch.draw(background, 0, 0);
        start.draw(batch);
        mode.draw(batch);
        exit.draw(batch);
    }

    public void update() {
        if(start.isPressed() || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            SoundPlayer.stopMusic(Asset.MENU_THEME);
            SoundPlayer.playSound(Asset.SELECT_SOUND, false);
            start_pressed = true;
        }

        if(mode.isPressed() || Gdx.input.isKeyPressed(Input.Keys.M)) {
            SoundPlayer.stopMusic(Asset.MENU_THEME);
            if(mode_pressed) {
                SoundPlayer.playSound(Asset.AI_OFF, false);
                mode_pressed = false;
            }
            else {
                SoundPlayer.playSound(Asset.AI_ON, false);
                mode_pressed = true;
            }

            try {
                Thread.sleep(700, 700); //Aspetto prima di poter riusare il tasto in caso di pressione prolungata.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if((exit.isPressed() || Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) && !start_pressed) {
            SoundPlayer.playSound(Asset.SELECT_SOUND, false);
            try {
                Thread.sleep(300, 300); //Aspetto giusto il tempo di far riprodurre il suono della selezione.
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Gdx.app.exit();
            System.exit(0);
        }
    }

    public void setStart_pressed(boolean start_pressed) {
        this.start_pressed = start_pressed;
    }

    public void setMode_pressed(boolean mode_pressed) {
        this.mode_pressed = mode_pressed;
    }

    public boolean isStart_pressed() {
        return start_pressed;
    }

    public boolean isMode_pressed() {
        return mode_pressed;
    }
}
