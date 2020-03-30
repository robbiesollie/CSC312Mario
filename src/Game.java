import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ConcurrentModificationException;

/**
 * Robbie Sollie - Game.java - CSC313 - CBU - 2020-02-25
 */
public class Game {
    private JFrame appFrame;
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 500;
    public static double scale = 2;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;
    private static final String UP_KEY_BINDING = "UP";
    private static final String DOWN_KEY_BINDING = "DOWN";
    private static final String LEFT_KEY_BINDING = "LEFT";
    private static final String RIGHT_KEY_BINDING = "RIGHT";

    private static BufferedImage background;
    private int distance = 0;

    public Player mario;
    private static BufferedImage p1Image;
    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private double horizVelocity;
    private double vertVelocity;

    private Map gameMap;
    private static BufferedImage goomba;

    private int deathCounter;


    public void setup() {
        appFrame = new JFrame("Mario");
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        JPanel panel = new JPanel();

        bindKey(panel, UP_KEY_BINDING);
        bindKey(panel, DOWN_KEY_BINDING);
        bindKey(panel, LEFT_KEY_BINDING);
        bindKey(panel, RIGHT_KEY_BINDING);

        appFrame.add(panel);

        try {
            background = ImageIO.read(new File("cropped.png"));
            p1Image = ImageIO.read(new File("luigi.png"));
            goomba = ImageIO.read(new File("goomba.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        scale = SCREEN_HEIGHT / (double) background.getHeight();

        mario = new Player(p1Image.getWidth(), p1Image.getHeight());
        gameMap = new Map();
    }

    public void start() {

        appFrame.setVisible(true);
        Thread[] threads = new Thread[4];
        threads[0] = new Thread(new Animate());
        threads[1] = new Thread(new PlayerMover());
        threads[2] = new Thread(new EnemyMover());
        threads[3] = new Thread(new Music());
        for (Thread t : threads) {
            t.start();
        }

    }

    private void bindKey(JPanel myPanel, String key) {
        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("pressed " + key), key + " pressed");
        myPanel.getActionMap().put(key + " pressed", new KeyAction(key, true));

        myPanel.getInputMap(IFW).put(KeyStroke.getKeyStroke("released " + key), key + " released");
        myPanel.getActionMap().put(key + " released", new KeyAction(key, false));
    }

    public class Animate implements Runnable {

        public void run() {
            while (true) {
                drawBackground();
                drawPlayer();
                drawEnemies();
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void drawBackground() {
            Graphics g = appFrame.getGraphics();
            g.setColor(Color.BLACK);
            int x = mario.getX();
            if (x - distance > (appFrame.getWidth() / 2) / scale) {
                distance += x - (distance + ((appFrame.getWidth() / 2) / scale));
            }
            scale = appFrame.getHeight() / (double) background.getHeight();
            g.drawImage(background, -(int) (distance * scale), 0, (int) (background.getWidth() * scale),
                    (int) (background.getHeight() * scale), null);
//            for (Rectangle r : gameMap.walls) {
//                g.drawRect((int) ((r.x - distance) * scale), (int) (r.y * scale), (int) (r.width * scale), (int) (r.height * scale));
//            }
            g.drawString("Deaths: " + deathCounter, 0, 50);
            g.drawString("Kills: " + gameMap.goombaKills, 0, 75);
        }

        private void drawPlayer() {
            Graphics g = appFrame.getGraphics();
            g.drawImage(p1Image,
                    (int) ((mario.getX() - distance) * scale),
                    (int) (mario.getY() * scale),
                    (int) (mario.getWidth() * scale),
                    (int) (p1Image.getHeight() * scale),
                    null);
//            g.drawRect((int) ((mario.getX() - distance) * scale),
//                    (int) (mario.getY() * scale),
//                    (int) (mario.getWidth() * scale),
//                    (int) (mario.getHeight() * scale));
        }

        private void drawEnemies() {
            Graphics g = appFrame.getGraphics();
            try {
                for (Enemy e : gameMap.enemies) {
                    g.drawImage(goomba,
                            (int) ((e.hitbox.x - distance) * scale),
                            (int) (e.hitbox.y * scale),
                            (int) (e.hitbox.width * scale),
                            (int) (e.hitbox.height * scale),
                            null);
                }
            } catch (ConcurrentModificationException e) {
                //meh
            }

        }
    }

    public class PlayerMover implements Runnable {
        public void run() {
            while (true) {
                if (mario.isOnGround()) {
                    if (upPressed) {
                        mario.setOnGround(false);
                        mario.setVertVelocity(-9);
                    }

                    if (!leftPressed && !rightPressed) {
                        mario.setHorizVelocity(0);
                    }
                }
                mario.increaseVertVelocity(.5);
                if (rightPressed) {
                    mario.increaseHorizVelocity(.5);
                }
                if (leftPressed) {
                    mario.increaseHorizVelocity(-.5);
                }
                mario.move(gameMap);
                if (mario.getX() < distance) {
                    mario.setX(distance);
                }

                if (mario.isDead()) {
                    mario = new Player(mario.getWidth(), mario.getHeight());
                    distance = 0;
                    deathCounter++;
                }

                if (mario.getY() > background.getHeight()) {
                    mario = new Player(mario.getWidth(), mario.getHeight());
                    distance = 0;
                    deathCounter++;
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class EnemyMover implements Runnable {
        public void run() {
            while (true) {
                try {
                    for (Enemy e : gameMap.enemies) {
                        if (e.left) {
                            e.hitbox.x -= 1;
                        } else {
                            e.hitbox.x += 1;
                        }
                        if (gameMap.collides(e.hitbox) != null) {
                            e.left = !e.left;
                        }
                    }
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (ConcurrentModificationException e) {
                    //meh
                }
            }
        }
    }

    private class Music implements Runnable {
        Clip c;
        public Music() {
            try {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("music.wav"));
                c = AudioSystem.getClip();
                c.loop(Clip.LOOP_CONTINUOUSLY);
                c.open(ais);
            } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            c.start();
        }
    }


    private class KeyAction extends AbstractAction {
        private String action;
        private boolean pressed;

        public KeyAction(String key, boolean pressed) {
            action = key;
            this.pressed = pressed;
        }

        public void actionPerformed(ActionEvent e) {
            switch (action) {
                case UP_KEY_BINDING:
                    upPressed = pressed;
                    break;
                case DOWN_KEY_BINDING:
                    downPressed = pressed;
                    break;
                case LEFT_KEY_BINDING:
                    leftPressed = pressed;
                    break;
                case RIGHT_KEY_BINDING:
                    rightPressed = pressed;
                    break;
            }
        }
    }

    public static void main(String[] args) {
        Game g = new Game();
        g.setup();
        g.start();
    }
}


