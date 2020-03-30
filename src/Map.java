import java.awt.*;
import java.util.LinkedList;
import java.util.Random;

/**
 * Robbie Sollie - Map.java - CSC313 - CBU - 2020-03-02
 */
public class Map {
    public LinkedList<Rectangle> walls;
    public LinkedList<Enemy> enemies;
    public int goombaKills;


    public Map() {
        walls = new LinkedList<>();
        walls.add(new Rectangle(0, 200, 1104, 24));
        walls.add(new Rectangle(1136, 200, 240, 24));
        walls.add(new Rectangle(1424, 200, 2447-1424, 24));
        walls.add(new Rectangle(2480, 200, 3391-2480, 24));


        walls.add(new Rectangle(256, 136, 16, 16));
        walls.add(new Rectangle(320, 136, 80, 16));
        walls.add(new Rectangle(352, 72, 16, 16));
        walls.add(new Rectangle(448, 168, 32, 32));
        walls.add(new Rectangle(608, 152, 32, 48));
        walls.add(new Rectangle(736, 136, 32, 64));
        walls.add(new Rectangle(912, 136, 32, 64));
        walls.add(new Rectangle(1232, 136, 48, 16));
        walls.add(new Rectangle(1280, 72, 128, 16));
        walls.add(new Rectangle(1456, 72, 64, 16));
        walls.add(new Rectangle(1504, 136, 16, 16));
        walls.add(new Rectangle(1600, 136, 32, 16));
        walls.add(new Rectangle(1696, 136, 16, 16));
        walls.add(new Rectangle(1744, 72, 16, 16));
        walls.add(new Rectangle(1744, 136, 16, 16));
        walls.add(new Rectangle(1792, 136, 16, 16));
        walls.add(new Rectangle(1888, 136, 16, 16));
        walls.add(new Rectangle(1936, 72, 48, 16));
        walls.add(new Rectangle(2048, 72, 64, 16));
        walls.add(new Rectangle(2064, 136, 32, 16));
        walls.add(new Rectangle(2144, 184, 64, 16));
        walls.add(new Rectangle(2160, 168, 48, 16));
        walls.add(new Rectangle(2176, 152, 32, 16));
        walls.add(new Rectangle(2192, 136, 16, 16));
        walls.add(new Rectangle(2240, 136, 16, 16));
        walls.add(new Rectangle(2240, 152, 32, 16));
        walls.add(new Rectangle(2240, 168, 48, 16));
        walls.add(new Rectangle(2240, 184, 64, 16));

        walls.add(new Rectangle(2368, 184, 80, 16));
        walls.add(new Rectangle(2384, 168, 64, 16));
        walls.add(new Rectangle(2400, 152, 48, 16));
        walls.add(new Rectangle(2416, 136, 32, 16));

        walls.add(new Rectangle(2480, 184, 64, 16));
        walls.add(new Rectangle(2480, 168, 48, 16));
        walls.add(new Rectangle(2480, 152, 32, 16));
        walls.add(new Rectangle(2480, 136, 16, 16));

        walls.add(new Rectangle(2608, 168, 32, 32));
        walls.add(new Rectangle(2688, 136, 64, 16));
        walls.add(new Rectangle(2864, 168, 32, 32));

        walls.add(new Rectangle(2896, 184, 144, 16));
        walls.add(new Rectangle(2912, 168, 128, 16));
        walls.add(new Rectangle(2928, 152, 112, 16));
        walls.add(new Rectangle(2944, 136, 96, 16));
        walls.add(new Rectangle(2960, 120, 80, 16));
        walls.add(new Rectangle(2976, 104, 64, 16));
        walls.add(new Rectangle(2992, 88, 48, 16));
        walls.add(new Rectangle(3008, 72, 32, 16));

        walls.add(new Rectangle(3168, 184, 16, 16));


        enemies = new LinkedList<>();
        enemies.add(new Enemy(300, 184));
        enemies.add(new Enemy(800, 184));
        enemies.add(new Enemy(2318, 184));
        enemies.add(new Enemy(2700, 184));
        enemies.add(new Enemy(3090, 184));
    }

    public Rectangle collides(Rectangle r) {
        for (Rectangle e : walls) {
            if (e.intersects(r)) {
                return e;
            }
        }
        return null;
    }

    public Enemy enemyCollides(Rectangle r) {
        for (Enemy e : enemies) {
            if (e.hitbox.intersects(r)) {
                return e;
            }
        }
        return null;
    }

    public void kill(Enemy e) {
        enemies.remove(e);
        goombaKills++;
    }
}
