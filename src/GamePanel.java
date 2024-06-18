import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {
    // Constants
    private static final int SCREEN_WIDTH = 1400;
    private static final int SCREEN_HEIGHT = 800;
    private static final int UNIT_SIZE = 20;
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 100;   // The delay between each game update

    // Snake and Apple properties
    private final int[] snakeX = new int[GAME_UNITS];
    private final int[] snakeY = new int[GAME_UNITS];
    private final Random random;  // A random number generator for generating the apple's position
    private int bodyParts = 6;
    private char direction = 'D';   // The direction the snake is moving (U, D, L, R)
    private int applesEaten;
    private int appleX, appleY;
    private boolean running = false;
    private Timer timer;    // The timer for the game

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        for (int i = 0; i < bodyParts; i++) {
            snakeX[i] = SCREEN_WIDTH / 2 - i;
            snakeY[i] = SCREEN_HEIGHT / 2;
        }
        startGame();
    }

    public void startGame() {
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newApple() {
        appleX = random.nextInt((SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        appleY = random.nextInt((SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }
        switch (direction) {
            case 'U':
                snakeY[0] = snakeY[0] - UNIT_SIZE;
                break;
            case 'D':
                snakeY[0] = snakeY[0] + UNIT_SIZE;
                break;
            case 'L':
                snakeX[0] = snakeX[0] - UNIT_SIZE;
                break;
            case 'R':
                snakeX[0] = snakeX[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple() {
        if ((snakeX[0] == appleX) && (snakeY[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        for (int i = bodyParts; i > 0; i--) {
            if ((snakeX[0] == snakeX[i]) && (snakeY[0] == snakeY[i])) {
                running = false;
                break;
            }
        }
        if (snakeX[0] < 0 || snakeX[0] > SCREEN_WIDTH || snakeY[0] < 0 || snakeY[0] > SCREEN_HEIGHT) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();  // This triggers the paintComponent method
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void drawSnake(Graphics g) {
        // Draw the snake's body parts
        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                g.setColor(Color.green);
                g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
            } else {
                g.setColor(new Color(45, 180, 0));
                g.fillRect(snakeX[i], snakeY[i], UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    private void drawApple(Graphics g) {
        // Draw the apple
        g.setColor(Color.red);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
    }

    private void drawScore(Graphics g) {
        // Draw the score
        g.setColor(Color.red);
        g.setFont(new Font("Digital-7 Mono", Font.PLAIN, 25));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2, g.getFont().getSize());
    }

    private void drawGameOverScreen(Graphics g) {
        // Draw the game over screen
        Scores.writeScore(applesEaten);
        int highestScore = Scores.readHighestScore();
        g.setColor(Color.red);
        g.setFont(new Font("Digital-7 Mono", Font.PLAIN, 40));
        FontMetrics metrics1 = getFontMetrics(g.getFont());
        g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: " + applesEaten)) / 2, SCREEN_HEIGHT * 2 / 3);
        g.drawString("Highest Score: " + highestScore, (SCREEN_WIDTH - metrics1.stringWidth("Highest Score: " + highestScore)) / 2, SCREEN_HEIGHT * 3 / 4);
        g.setColor(Color.red);
        g.setFont(new Font("Digital-7 Mono", Font.BOLD, 75));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over")) / 2, SCREEN_HEIGHT / 2);
    }

    public void draw(Graphics g) {
        if (running) {
            drawSnake(g);
            drawApple(g);
            drawScore(g);
        } else {
            drawGameOverScreen(g);
        }
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}