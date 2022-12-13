package game;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;

public class Puyo {
    public static final char RED = 'R';
    public static final char GREEN = 'G';
    public static final char BLUE = 'B';
    public static final char YELLOW = 'Y';
    public static final char PURPLE = 'P';
    public static final char GARBAGE = 'X';
    public static final char NONE = '.';
    public static final char[] COLOR_SET = new char[]{'R', 'Y', 'B', 'P', 'G'};
    static final Random rng = new Random();

    public static final HashMap<Character, Color> COLOR_MAP = new HashMap<>();
    static{
        COLOR_MAP.put('R', Color.RED);
        COLOR_MAP.put('G', Color.GREEN);
        COLOR_MAP.put('B', Color.BLUE);
        COLOR_MAP.put('Y', Color.YELLOW);
        COLOR_MAP.put('P', new Color(0x862db3));
        COLOR_MAP.put('X', Color.GRAY);
        COLOR_MAP.put('.', Color.BLACK);
    }
    static int PUYO_COUNTER = 0;

    public int puyoID;
    public char color = NONE;

    public Puyo(){
        this.color = COLOR_SET[rng.nextInt(Parameters.COLORS)];
        this.puyoID = ++PUYO_COUNTER;
    }

    public Puyo(char color){
        this.color = color;
        this.puyoID = ++PUYO_COUNTER;
    }
}
