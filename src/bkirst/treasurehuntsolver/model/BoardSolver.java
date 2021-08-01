package bkirst.treasurehuntsolver.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BoardSolver {
    /*
     * Evaluates all possible moves and returns the move that clears the most gems
     */
    public static Move getOptimalMove(GameBoard originalState) {
        List<Move> potentialMoves = new ArrayList<>();
        for (int y = 7; y >= 0; y--) {
            for (int x = y % 2; x < 8; x += 2) {
                for (int i = 0; i < 4; i++) {
                    GameBoard newBoard = originalState.copyDeep();
                    Move move = null;
                    // UP
                    if (i == 0 && y > 0) {
                        move = new Move(newBoard.getGem(x, y), newBoard.getGem(x, y - 1));
                        move.setGem1Position(new Point(x, y));
                        move.setGem2Position(new Point(x, y - 1));
                        newBoard.executeMove(move);
                    }
                    // DOWN
                    if (i == 1 && y < 7) {
                        move = new Move(newBoard.getGem(x, y), newBoard.getGem(x, y + 1));
                        move.setGem1Position(new Point(x, y));
                        move.setGem2Position(new Point(x, y + 1));
                        newBoard.executeMove(move);
                    }
                    // LEFT
                    if (i == 2 && x > 0) {
                        move = new Move(newBoard.getGem(x, y), newBoard.getGem(x - 1, y));
                        move.setGem1Position(new Point(x, y));
                        move.setGem2Position(new Point(x - 1, y));
                        newBoard.executeMove(move);
                    }
                    // RIGHT
                    if (i == 3 && x < 7) {
                        move = new Move(newBoard.getGem(x, y), newBoard.getGem(x + 1, y));
                        move.setGem1Position(new Point(x, y));
                        move.setGem2Position(new Point(x + 1, y));
                        newBoard.executeMove(move);
                    }
                    if (move == null)
                        continue;
                    // If this is the best move we've found so far, clear out the potential list and add this one
                    // Otherwise if this move is as good as our previous best, add it to the list
                    if (potentialMoves.isEmpty() || move.getNumCleared() > potentialMoves.get(0).getNumCleared()) {
                        potentialMoves.clear();
                        potentialMoves.add(move);
                    } else if (move.getNumCleared() == potentialMoves.get(0).getNumCleared()) {
                        potentialMoves.add(move);
                    }
                }
            }
        }

        Move optimalMove = potentialMoves.get(0);
        if (potentialMoves.size() > 1) {
            if (optimalMove.getNumCleared() == 3) {
                // If we're only matching 3, just match whatever is lowest on the board to hopefully maximize shuffling the gems and clear trash
                double maxY = 0;
                for (Move move : potentialMoves) {
                    if (move.getGem1Position().getY() > maxY) {
                        optimalMove = move;
                        maxY = move.getGem1Position().getY();
                    }

                    if (move.getGem2Position().getY() > maxY) {
                        optimalMove = move;
                        maxY = move.getGem2Position().getY();
                    }
                }
            } else {
                optimalMove = potentialMoves.get(potentialMoves.size() - 1);
                // If matching 4+, just match whatever is closest to the top to minimize shuffling other 4 or 5 gem matches
                double minY = 10;
                for (Move move : potentialMoves) {
                    if (move.getGem1Position().getY() < minY) {
                        minY = move.getGem1Position().getY();
                        optimalMove = move;
                    }

                    if (move.getGem2Position().getY() < minY) {
                        minY = move.getGem2Position().getY();
                        optimalMove = move;
                    }
                }
            }
        }
        //System.out.println("Optimal move found: " + optimalMove.toString());
        return optimalMove;
    }
}
