package dev.ian.snakeboi.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.Random;

import dev.ian.snakeboi.Scorer;
import dev.ian.snakeboi.Server;
import dev.ian.snakeboi.asset.Asset;
import dev.ian.snakeboi.asset.SoundPlayer;
import dev.ian.snakeboi.entities.Board;
import dev.ian.snakeboi.entities.GameObject;
import dev.ian.snakeboi.entities.Snake;
import dev.ian.snakeboi.menu.Menu;
import dev.ian.snakeboi.menu.MenuButton;

public class SnakeGame {

    private static final int WIDTH = Gdx.graphics.getHeight();
    private static final int HEIGHT = Gdx.graphics.getHeight();

    private Board board;
    private Snake snake;
    private float timeState;
    private BitmapFont font;

    private GameObject food;
    private boolean isGameOver;

    private Texture background_texture;
    private Image background;

    private Menu menu;

    private Server server;


    public SnakeGame(Menu menu) {
        TextureAtlas atlas = Asset.instance().get(Asset.SNAKE_PACK);
        font = Asset.instance().get(Asset.PIXEL_FONT);
        snake = new Snake(atlas, menu);
        board = new Board(snake, WIDTH+64, HEIGHT+64);
        food = board.generateFood();
        background_texture = new Texture(Asset.BACKGROUND);
        background = new Image(background_texture);
        background.setPosition(HEIGHT,0);
        if(Gdx.graphics.getWidth() > 1920)
            background.setWidth(840+(Gdx.graphics.getWidth()-1920));

        this.menu = menu;

        try {
            server = new Server();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void update(float delta) {
        SoundPlayer.playMusic(Asset.MEMO_SOUND, false);

        if (snake.hasLive()) {
            timeState += delta;
            snake.handleEvents();
            if (timeState >= .09f) {
                snake.moveBody();
                timeState = 0;
            }
            if (snake.isCrash()) {
                snake.reset();
                snake.popLife();
                SoundPlayer.playSound(Asset.CRASH_SOUND, false);
            }
            if (snake.isFoodTouch(food)) {
                SoundPlayer.playSound(Asset.EAT_FOOD_SOUND, false);
                Scorer.score();
                snake.grow();
                food = board.generateFood();
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                returnToMenu();
            }

            if (!menu.isMode_pressed()) {
                if (shake()) {
                    food = board.generateFood();
                }
            }

            if(server.getComandoVocale() != "" && server.getComandoVocale() != null)
                comandiVocali();

        } else {
            gameOver();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY) || Gdx.input.isTouched()) start();

            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK)) {
                returnToMenu();
            }
        }
    }

    private void comandiVocali() {
        Random r = new Random();
        int random_donuts  = r.nextInt(6);

        String comando_impartito = server.getComandoVocale();

        String comando_sposta_ciambella = "sposta la ciambella";
        String comando_una_ciambella = "dammi una ciambella";
        String comando_tante_ciambelle = "dammi tante ciambelle";
        String comando_piu_ciambelle = "dammi più ciambelle";
        String riavvia_livello = "riavvia il livello";
        String torna_menu = "torna al menù";

        String comando_sposta_ciambella1 = "Sposta la ciambella";
        String comando_una_ciambella1 = "Dammi una ciambella";
        String comando_tante_ciambelle1 = "Dammi tante ciambelle";
        String comando_piu_ciambelle1 = "Dammi più ciambelle";
        String riavvia_livello1 = "Riavvia il livello";
        String torna_menu1 = "Torna al menù";

        if(comando_impartito.equals(comando_sposta_ciambella) || comando_impartito.equals(comando_sposta_ciambella1)) {
            food = board.generateFood();
        }
        else if(comando_impartito.equals(comando_una_ciambella) || comando_impartito.equals(comando_una_ciambella1)) {
            SoundPlayer.playSound(Asset.EAT_FOOD_SOUND, false);
            Scorer.score();
            snake.grow();
        }
        else if(comando_impartito.equals(comando_tante_ciambelle) || comando_impartito.equals(comando_tante_ciambelle1)) {
            for(int i=0; i <= random_donuts; ++i) {
                SoundPlayer.playSound(Asset.EAT_FOOD_SOUND, false);
                Scorer.score();
                snake.grow();
            }
        }
        else if(comando_impartito.equals(comando_piu_ciambelle) || comando_impartito.equals(comando_piu_ciambelle1)) {
            for(int i=0; i <= random_donuts; ++i) {
                SoundPlayer.playSound(Asset.EAT_FOOD_SOUND, false);
                Scorer.score();
                snake.grow();
            }
        }
        else if(comando_impartito.equals(riavvia_livello) || comando_impartito.equals(riavvia_livello1)) {
            start();
        }
        else if(comando_impartito.equals(torna_menu) || comando_impartito.equals(torna_menu1)) {
            returnToMenu();
        }

        server.setComandoVocale("");
    }

    private boolean shake() {
        boolean shake_x = false;
        boolean shake_y = false;

        if(Gdx.input.getAccelerometerX() > 10 || Gdx.input.getAccelerometerX() < -10)
            shake_x = true;

        if(Gdx.input.getAccelerometerY() > 10 || Gdx.input.getAccelerometerY() < -10)
            shake_y =true;

        return shake_x && shake_y;
    }

    private void returnToMenu() {
        isGameOver = false;
        SoundPlayer.stopMusic(Asset.MEMO_SOUND);
        snake.reset();
        Scorer.reset();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        menu.setStart_pressed(false);
    }

    private void gameOver() {
        isGameOver = true;
        SoundPlayer.stopMusic(Asset.MEMO_SOUND);
        SoundPlayer.playMusic(Asset.GAME_OVER_SOUND, false);
    }

    private void start() {
        SoundPlayer.playMusic(Asset.MEMO_SOUND, false);
        SoundPlayer.stopMusic(Asset.GAME_OVER_SOUND);

        isGameOver = false;
        snake.reset();
        snake.restoreHealth();
        food = board.generateFood();
        Scorer.reset();
    }

    public void render(SpriteBatch batch) {
        background.draw(batch, 1);
        board.render(batch);
        food.draw(batch);
        snake.render(batch);

        if (isGameOver) {
            font.draw(batch, "GAME OVER", (WIDTH - 350) / 2, (HEIGHT + 320) / 2);
            font.draw(batch, "Press any key or touch\nthe screen to continue", (WIDTH - 1000) / 2, (HEIGHT + 60) / 2);
        }

        font.draw(batch, "Score: " + Scorer.getScore(), HEIGHT + 60, GameInfo.SCREEN_HEIGHT - 150);
        font.draw(batch, "Size: " + snake.getBody().size(), HEIGHT + 60, GameInfo.SCREEN_HEIGHT - 250);
    }

}
