import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    public GameFrame() {
        add(new GamePanel(), BorderLayout.CENTER);
        setTitle("Snake Game");

        // Icon
        ImageIcon snakeIcon = new ImageIcon("Snake-removebg-preview.png");  // Create an ImageIcon from the "Snake-removebg-preview.png" image file
        setIconImage(snakeIcon.getImage()); // Set the icon image of the frame to the snake icon

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}