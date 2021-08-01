package bkirst.treasurehuntsolver.simulator;

import bkirst.treasurehuntsolver.model.BoardSolver;
import bkirst.treasurehuntsolver.model.GameBoard;
import bkirst.treasurehuntsolver.model.Move;

import java.util.ArrayList;
import java.util.List;

public class GameSimulator {
    private static final int NUM_SIMS = 100;
    public static void main(String[] args) {
        List<Integer> gameTurns = new ArrayList<>();
        for (int i = 0; i < NUM_SIMS; i++) {
            int turnCount = 8;
            int turnsSurvived = 0;
            GameBoard board = new GameBoard();
            while (turnCount > 0) {
                turnsSurvived++;
                //System.out.println(board);
                Move move = BoardSolver.getOptimalMove(board);
                // You lose a turn on a 3 match
                int maxNumCleared = Math.max(move.getNumCleared() , move.getNumClearedWithCascade());
                if (maxNumCleared == 3)
                    turnCount--;
                // You gain a turn on a 5+ match
                if (maxNumCleared >= 5)
                    turnCount++;
                board = move.getFinalBoard();
            }
            gameTurns.add(turnsSurvived);
            System.out.println("Game " + i + " " + turnsSurvived + " turns");
            System.out.println("\n" + board);
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

