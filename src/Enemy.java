import java.awt.*;

/**
 * Robbie Sollie - Enemy.java - CSC313 - CBU - 2020-03-02
 */
public class Enemy {
    public Rectangle hitbox;
    public boolean left = true;

    public Enemy(int x, int y) {
        hitbox = new Rectangle(x, y, 16, 16);
    }
}
