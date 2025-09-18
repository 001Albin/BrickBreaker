import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private MapGenerator map;
    private boolean play = false;
    private boolean gameStarted = false;
    private int score = 0;
    private int totalBricks = 30;

    private Timer timer;
    private int delay = 5;

    private int playerX = 310;
    private double ballPosX = 310;
    private double ballPosY = 500;
    private double ballXdir = -2;
    private double ballYdir = -3;

    public Gameplay() {
        map = new MapGenerator(6, 5);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, getWidth(), getHeight());

        // Draw bricks
        map.draw((Graphics2D) g);

        // Score
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 18));
        g.drawString("Score: " + score, 10, 20);

        // Paddle
        g.setColor(Color.blue);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval((int)ballPosX, (int)ballPosY, 20, 20);

        int textY = 30; // Text above bricks

        // Start screen
        if (!gameStarted) {
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("Press Enter to Start", getWidth()/2 - 150, textY);
        }

        // Game over
        if (ballPosY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 28));
            g.drawString("Game Over! Score: " + score, getWidth()/2 - 180, 40);

            // Bottom of screen
            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press Enter to Restart", getWidth()/2 - 150, getHeight() -50);
        }

        // Win condition
        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.green);
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("You Won! Score: " + score, getWidth()/2 - 150, textY);

            g.setFont(new Font("Arial", Font.PLAIN, 18));
            g.drawString("Press Enter to Restart", getWidth()/2 - 150, getHeight() -50);
        }

        g.dispose();
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            // Ball intersects paddle
            if (new Rectangle((int)ballPosX, (int)ballPosY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            // Brick collision
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * (map.brickWidth + map.gap) + 80;
                        int brickY = i * (map.brickHeight + map.gap) + 50 + map.brickYOffset;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle brickRect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle((int)ballPosX, (int)ballPosY, 20, 20);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            // Ball direction
                            if ((int)(ballPosX + 19) <= brickRect.x || (int)(ballPosX + 1) >= brickRect.x + brickWidth) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }

                            // Gradual speed increase & move bricks down
                            if (score % 25 == 0 && score != 0) {
                                ballXdir *= 1.05;
                                ballYdir *= 1.05;
                                if (map.brickYOffset < 200) map.brickYOffset += 5;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXdir;
            ballPosY += ballYdir;

            if (ballPosX < 0 || ballPosX > 670) ballXdir = -ballXdir;
            if (ballPosY < 0) ballYdir = -ballYdir;
        }

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 600) moveRight();
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 10) moveLeft();

        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!gameStarted) {
                gameStarted = true;
                play = true;
            } else if (!play) {
                restartGame();
            }
        }
    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    private void restartGame() {
        play = true;
        ballPosX = 310;
        ballPosY = 500;
        ballXdir = -2;
        ballYdir = -3;
        playerX = 310;
        score = 0;
        totalBricks = 30;
        map = new MapGenerator(6, 5);
    }
}
