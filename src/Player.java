import java.awt.*;

/**
 * Robbie Sollie - Player.java - CSC313 - CBU - 2020-02-25
 */
public class Player {
    private boolean onGround = true;

    private Rectangle hitbox;

    private double vertVelocity;

    private double horizVelocity;
    private static final double maxVelocity = 3.0;
    private static final int GROUND = 150;
    private boolean dead = false;

    public Player(int width, int height) {
        hitbox = new Rectangle(0, GROUND, width, height);
    }

    public int getX() {
        return hitbox.x;
    }
    public void setX(int x) {
        hitbox.x = x;
    }

    public int getY() {
        return hitbox.y;
    }

    public void setY(int y) {
        hitbox.y = y;
    }

    public int getWidth() { return hitbox.width; }
    public int getHeight() { return hitbox.height; }

    public boolean isDead() {
        return dead;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public double getVertVelocity() {
        return vertVelocity;
    }

    public void setVertVelocity(double vertVelocity) {
        this.vertVelocity = vertVelocity;
    }

    public void increaseVertVelocity(double vertVelocity) {
        this.vertVelocity += vertVelocity;
    }

    public double getHorizVelocity() {
        return horizVelocity;
    }


    public void increaseHorizVelocity(double horizVelocity) {
        this.horizVelocity += horizVelocity;
        if (this.horizVelocity > maxVelocity) {
            this.horizVelocity = maxVelocity;
        } else if (this.horizVelocity < -maxVelocity) {
            this.horizVelocity = -maxVelocity;
        }
    }

    public void slowPlayer() {
        this.horizVelocity /= 4;
    }
    public void setHorizVelocity(double horizVelocity) {
        this.horizVelocity = horizVelocity;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void move(Map gameMap) {
        hitbox.x += horizVelocity;
        if (gameMap.collides(hitbox) != null) {
            Rectangle block = gameMap.collides(hitbox);
            if (horizVelocity > 0) {
                hitbox.x = block.x - hitbox.width;
                horizVelocity = 0;
            }
            else {
                hitbox.x = block.x + block.width;
                horizVelocity = 0;
            }
        }
        if (gameMap.enemyCollides(hitbox) != null) {
            dead = true;
            return;
        }
        hitbox.y += vertVelocity;
        if (gameMap.collides(hitbox) != null) {
            Rectangle block = gameMap.collides(hitbox);
            if (vertVelocity > 0) {
                hitbox.y = block.y - hitbox.height;
                vertVelocity = 0;
                onGround = true;
            }
            else {
                hitbox.y = block.y + block.height;
                vertVelocity = 0;
            }
        }
        if (gameMap.enemyCollides(hitbox) != null) {
            if (vertVelocity > 0) {
                gameMap.kill(gameMap.enemyCollides(hitbox));
            } else {
                dead = true;
            }
        }
    }
}
