package game.subsidiaries.visuals;

import game.actors.player.PlayerEntity;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lazarus on 03/04/17.
 */
public class HeadsUpDisplay {

    int screen_width;
    int screen_height;
    int max_health;
    int current_health;
    int current_score;
    ImageIcon healthbar;


    public HeadsUpDisplay(int width, int height, String healthbar_path, PlayerEntity player) {
        screen_width = width;
        screen_height = height;
        healthbar = new ImageIcon(healthbar_path);
        max_health = PlayerEntity.PLAYER_MAX_HEALTH;
        current_health = player.getHealth_points();
        current_score = 0;
    }

    public void update(PlayerEntity player) {
        current_health = player.getHealth_points();
    }

    public void draw(Graphics2D g) {
        float full_width = 62f;
        float percentage_health = (float)current_health/max_health;
        float bar_width = full_width * percentage_health;
        if ((int) bar_width > 0) {
            g.setColor(new Color(0x63d5ae));
            g.fillRect(46, 34, (int)bar_width, 22);
        }
        g.drawImage(healthbar.getImage(), 16, 28, null);
    }

}
