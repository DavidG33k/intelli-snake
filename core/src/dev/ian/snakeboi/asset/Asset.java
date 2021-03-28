package dev.ian.snakeboi.asset;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;


public class Asset {

    private static Asset instance = new Asset();
    private AssetManager assetManager = new AssetManager();

    private static String desktop_path = "android/assets/"; //android/assets/

    public static final String SNAKE_PACK = desktop_path + "images/snake.pack";
    public static final String PIXEL_FONT = desktop_path + "fonts/pixel.ttf";
    public static final String BACKGROUND = desktop_path + "images/background.png";
    public static final String MENU_BACKGROUND = desktop_path + "images/menu_background.png";
    public static final String LOGO = desktop_path + "images/logo.png";
    public static final String START_BUTTON = desktop_path + "images/start_button.png";
    public static final String MODE_BUTTON = desktop_path + "images/mode_button.png";
    public static final String EXIT_BUTTON = desktop_path + "images/exit_button.png";
    public static final String MEMO_SOUND = desktop_path + "sounds/8bit_bg.mp3";
    public static final String GAME_OVER_SOUND = desktop_path + "sounds/gameover.mp3";
    public static final String EAT_FOOD_SOUND = desktop_path + "sounds/eat_food.mp3";
    public static final String MENU_THEME = desktop_path + "sounds/menu_theme.mp3";
    public static final String SELECT_SOUND = desktop_path + "sounds/select.wav";
    public static final String CRASH_SOUND = desktop_path + "sounds/crash.ogg";
    public static final String AI_ON = desktop_path + "sounds/ai_on.wav";
    public static final String AI_OFF = desktop_path + "sounds/ai_off.wav";


    private Asset() {

    }

    public static Asset instance() {
        return instance;
    }

    public void loadAsset() {
        loadFont();
        loadSounds();
        loadImages();
        assetManager.finishLoading();
    }

    private void loadFont() {
        FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
        FreetypeFontLoader.FreeTypeFontLoaderParameter mySmallFont = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        mySmallFont.fontFileName = PIXEL_FONT;
        mySmallFont.fontParameters.size = 70;
        mySmallFont.fontParameters.color = Color.WHITE;
        assetManager.load(PIXEL_FONT, BitmapFont.class, mySmallFont);
    }

    private void loadImages() {assetManager.load(SNAKE_PACK, TextureAtlas.class);}

    private void loadSounds() {
        assetManager.load(MENU_THEME, Music.class);
        assetManager.load(MEMO_SOUND, Music.class);
        assetManager.load(GAME_OVER_SOUND, Music.class);
        assetManager.load(EAT_FOOD_SOUND, Sound.class);
        assetManager.load(CRASH_SOUND, Sound.class);
        assetManager.load(SELECT_SOUND, Sound.class);
        assetManager.load(AI_ON, Sound.class);
        assetManager.load(AI_OFF, Sound.class);
    }

    public <T> T get(String filename) {
        return assetManager.get(filename);
    }

    public Sprite getSprite(String name) {
        TextureAtlas atlas = get(Asset.SNAKE_PACK);
        return atlas.createSprite(name);
    }

    public void dispose() {
        assetManager.dispose();
    }

}
