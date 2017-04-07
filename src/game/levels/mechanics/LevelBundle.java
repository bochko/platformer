package game.levels.mechanics;

import java.util.HashMap;

/**
 * Created by lazarus on 07/04/2017.
 */
public class LevelBundle {
    // mandatory that the main menu index is 0
    // mandatory that all other levels are consecutive numbers from 1 to n
    public static final int MENU_INDEX = 0;

    // signals
    public static final int SIGNAL_CONTINUE = 77999;
    public static final int SIGNAL_RESTART_LEVEL = 77077;
    public static final int SIGNAL_NEXT_LEVEL = 77177;
    public static final int SIGNAL_MAIN_MENU = 77277;

    HashMap<Integer, Level> levels;

    public LevelBundle() {
        levels = new HashMap<>();
    }


    public void addLevel(int key, Level level) {
        levels.put(key, level);
    }

    public Level getLevel(int key) {
        if(levels.containsKey(key)) {
            return levels.get(key);
        } else {
            return levels.get(MENU_INDEX);
        }
    }
}
