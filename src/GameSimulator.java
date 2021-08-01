import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameSimulator {
    private static final int NUM_SIMS = 10000;
    public static void main(String[] args) throws Exception {
        List<BufferedImage> images = new ArrayList<>();
        List<ImageProcessThread> threads;
        Map<BufferedImage, List<Point>> pointMap = new HashMap<>();
        List<Integer> gameTurns = new ArrayList<>();
        for (int i = 0; i < NUM_SIMS; i++) {
            int turnCount = 12;
            int turnsSurvived = 0;
            GameBoard board = new GameBoard();
            while (turnCount > 0) {
                turnsSurvived++;
                System.out.println(board);
                Move move = BoardSolver.getOptimalMove(board);
                System.out.println("Optimal Move: Swap " + move.getGem1Position() + " and " + move.getGem2Position() + " to clear " + move.getNumCleared() + " gems.");
                if (move.getNumCleared() < 4)
                    turnCount--;
                if (move.getNumCleared() > 4)
                    turnCount++;
                board = move.getFinalBoard();
            }
            gameTurns.add(turnsSurvived);
            System.out.println("Game " + i + " " + turnsSurvived + " turns");
        }

        // STATS
        int max = 0;
        int min = 10000;
        int total = 0;
        for (int turns : gameTurns)
        {
            if (turns > max)
                max = turns;
            if (turns < min)
                min = turns;
            total += turns;
        }
        System.out.println("min " + min);
        System.out.println("max " + max);
        System.out.println("avg " + total/NUM_SIMS);
    }
}

