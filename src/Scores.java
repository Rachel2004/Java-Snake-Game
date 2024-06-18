import java.io.*;
import java.util.Scanner;

public class Scores {
    private static final String SCORES_FILE = "scores.txt";

    public static void writeScore(int score) {
        try (PrintWriter out = new PrintWriter(new FileWriter(SCORES_FILE, true))) {
            out.println(score);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public static int readHighestScore() {
        int highestScore = 0;

        try (Scanner scanner = new Scanner(new File(SCORES_FILE))) {
            while (scanner.hasNextInt()) {
                int score = scanner.nextInt();
                highestScore = Math.max(highestScore, score);
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }

        return highestScore;
    }
}