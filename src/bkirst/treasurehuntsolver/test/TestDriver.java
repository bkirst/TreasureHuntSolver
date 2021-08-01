package bkirst.treasurehuntsolver.test;

import bkirst.treasurehuntsolver.model.BoardSolver;
import bkirst.treasurehuntsolver.model.GameBoard;
import bkirst.treasurehuntsolver.model.Gem;
import bkirst.treasurehuntsolver.model.Move;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TestDriver {
    public static void main(String[] args) throws Exception {
        int failures = 0;
        System.out.println("\n");
        int[][] topRow5Match = new int[][]{

                {3, 0, 3, 3, 2, 3, 3, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {1, 2, 1, 0, 2, 3, 2, 2},
                {0, 0, 3, 1, 3, 2, 1, 3},
                {2, 1, 3, 3, 0, 0, 2, 1},
                {0, 1, 1, 0, 3, 2, 0, 0},
                {1, 2, 0, 1, 1, 0, 2, 3},
                {2, 3, 2, 2, 3, 1, 2, 2}
        };

        GameBoard board = createGameBoard(topRow5Match);
        Move optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 5)
            System.out.println("topRow5Match failure. " + ++failures);

        System.out.println("\n");
        int[][] middle5L = new int[][]{

                {3, 0, 2, 3, 2, 3, 2, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {1, 2, 1, 0, 2, 1, 2, 2},
                {0, 0, 2, 1, 2, 3, 1, 3},
                {2, 1, 2, 3, 3, 0, 2, 1},
                {0, 1, 1, 0, 3, 3, 0, 0},
                {1, 2, 0, 1, 1, 3, 2, 3},
                {2, 3, 2, 3, 2, 1, 3, 2}
        };

        board = createGameBoard(middle5L);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 5)
            System.out.println("middle5L failure. " + ++failures);

        System.out.println("\n");
        int[][] bottomRow5Match = new int[][]{

                {3, 0, 2, 3, 2, 3, 2, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {1, 2, 1, 0, 2, 3, 2, 2},
                {0, 0, 3, 1, 3, 2, 1, 3},
                {2, 1, 3, 3, 0, 0, 2, 1},
                {0, 1, 1, 0, 3, 3, 0, 0},
                {1, 2, 0, 1, 1, 3, 2, 3},
                {2, 3, 2, 3, 3, 1, 3, 2}
        };

        board = createGameBoard(bottomRow5Match);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 5)
            System.out.println("bottomRow5Match failure. " + ++failures);

        System.out.println("\n");
        int[][] isStableTest = new int[][]{

                {3, 0, 2, 3, 2, 3, 2, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {3, 2, 1, 0, 2, 3, 2, 2},
                {0, 0, 3, 1, 3, 2, 1, 3},
                {2, 1, 3, 3, 0, 0, 2, 1},
                {0, 1, 1, 0, 3, 3, 0, 0},
                {1, 2, 0, 1, 1, 3, 2, 3},
                {2, 3, 2, 3, 3, 1, 3, 2}
        };

        board = createGameBoard(isStableTest);
        board.executeClear();
        System.out.println(board);
        if (!board.isStable())
            System.out.println("isStableTest failure. " + ++failures);

        System.out.println("\n");
        int[][] isNotStableTest = new int[][]{

                {2, 0, 2, 3, 2, 3, 2, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {3, 2, 1, 0, 2, 3, 2, 2},
                {3, 0, 3, 1, 3, 2, 1, 3},
                {2, 1, 3, 3, 0, 0, 2, 1},
                {0, 1, 1, 0, 3, 3, 0, 0},
                {1, 2, 0, 1, 1, 3, 2, 3},
                {2, 3, 2, 3, 3, 1, 3, 2}
        };

        board = createGameBoard(isNotStableTest);
        board.executeClear();
        System.out.println(board);
        if (board.isStable())
            System.out.println("isNotStableTest failure. " + ++failures);

        System.out.println("\n");
        int[][] fancyMoveTest = new int[][]{

                {2, 0, 2, 3, 3, 1, 2, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {1, 1, 2, 2, 0, 2, 0, 2},
                {3, 0, 2, 0, 3, 1, 1, 3},
                {2, 1, 3, 1, 3, 0, 2, 1},
                {0, 1, 1, 0, 2, 3, 0, 0},
                {1, 2, 0, 1, 1, 3, 2, 3},
                {2, 3, 2, 3, 3, 1, 0, 2}
        };

        board = createGameBoard(fancyMoveTest);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 5)
            System.out.println("fancyMoveTest failure. " + ++failures);

        System.out.println("\n");
        int[][] dropMoveTest = new int[][]{

                {2, 0, 2, 3, 3, 0, 2, 0},
                {3, 0, 0, 1, 3, 1, 3, 1},
                {1, 1, 2, 0, 2, 1, 2, 2},
                {3, 0, 2, 2, 3, 0, 1, 3},
                {2, 1, 3, 1, 3, 0, 2, 1},
                {0, 1, 1, 0, 2, 3, 0, 0},
                {1, 2, 0, 1, 1, 3, 2, 3},
                {2, 3, 2, 3, 3, 1, 0, 2}
        };

        board = createGameBoard(dropMoveTest);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 4)
            System.out.println("dropMoveTest failure. " + ++failures);

        System.out.println("\n");
        int[][] weirdScenario = new int[][]{
                {2, 0, 2, 1, 3, 0, 3, 1},
                {1, 1, 0, 0, 2, 0, 2, 0},
                {0, 2, 0, 2, 1, 2, 1, 1},
                {0, 4, 4, 1, 1, 4, 3, 2},
                {3, 4, 1, 1, 4, 0, 1, 4},
                {1, 2, 5, 0, 0, 6, 2, 0},
                {5, 3, 1, 2, 7, 4, 2, 2},
                {1, 2, 2, 6, 1, 3, 1, 0}
        };

        board = createGameBoard(weirdScenario);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 3)
            System.out.println("weirdScenario failure. " + ++failures);

        System.out.println("\n");
        int[][] weirdScenario2 = new int[][]{
                {2, 0, 4, 2, 2, 3, 0, 1},
                {2, 0, 2, 2, 0, 1, 2, 2},
                {3, 5, 0, 1, 2, 0, 4, 0},
                {1, 2, 1, 3, 3, 1, 1, 2},
                {2, 0, 3, 0, 1, 0, 0, 3},
                {2, 1, 1, 2, 2, 0, 0, 2},
                {1, 2, 4, 0, 4, 2, 1, 4},
                {1, 3, 0, 0, 3, 3, 0, 2},
        };
        // should be able to swap [3,4] and [4,4] for a cascade 4 match
        board = createGameBoard(weirdScenario2);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() != 4)
            System.out.println("weirdScenario2 failure. " + ++failures);

        System.out.println("\n");
        int[][] boring3Match = new int[][]{
                {2, 5, 4, 2, 1, 3, 0, 1},
                {2, 0, 2, 5, 0, 1, 2, 2},
                {3, 5, 0, 1, 2, 4, 4, 0},
                {1, 2, 1, 3, 3, 1, 1, 2},
                {2, 0, 3, 0, 1, 0, 5, 1},
                {2, 6, 1, 2, 2, 0, 5, 2},
                {1, 2, 4, 1, 4, 2, 1, 4},
                {1, 3, 0, 3, 3, 5, 0, 3},
        };
        // should match 3's on the bottom row
        board = createGameBoard(boring3Match);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getGem1Position().getY() < 7)
            System.out.println("boring3Match failure. " + ++failures);

        System.out.println("\n");
        int[][] top4Match = new int[][]{
                {2, 5, 4, 2, 1, 1, 0, 1},
                {2, 0, 2, 5, 0, 4, 1, 2},
                {3, 5, 0, 1, 2, 4, 4, 0},
                {1, 2, 1, 3, 3, 1, 1, 2},
                {2, 0, 3, 0, 1, 0, 5, 1},
                {2, 6, 1, 2, 2, 0, 5, 2},
                {1, 2, 4, 1, 4, 3, 1, 4},
                {1, 3, 0, 2, 3, 0, 3, 3},
        };
        // There's an obvious 4 match in the top row and the bottom row. We should pick the top row to maximize turns
        board = createGameBoard(top4Match);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getGem1Position().getY() > 1)
            System.out.println("top4Match failure. " + ++failures);


        System.out.println("\n");
        int[][] boring3Match2 = new int[][]{
                {1, 0, 0, 1, 4, 0, 0, 1},
                {0, 3, 3, 1, 3, 2, 1, 0},
                {4, 3, 2, 0, 2, 3, 0, 0},
                {3, 1, 2, 5, 0, 1, 1, 3},
                {1, 2, 1, 4, 3, 2, 2, 3},
                {2, 1, 1, 6, 4, 5, 2, 0},
                {1, 6, 5, 2, 2, 6, 4, 1},
                {2, 2, 3, 3, 6, 4, 0, 2},
        };
        // We should match a 3 lower down in the grid
        board = createGameBoard(boring3Match2);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getGem1Position().getY() < 3)
            System.out.println("boring3Match2 failure. " + ++failures);

        System.out.println("\n");
        int[][] lowSide3Match = new int[][]{
                {1, 3, 3, 0, 0, 2, 3, 0},
                {1, 3, 3, 1, 3, 3, 1, 1},
                {4, 2, 0, 1, 3, 1, 1, 2},
                {1, 0, 1, 2, 4, 2, 3, 4},
                {0, 3, 3, 6, 1, 5, 2, 1},
                {2, 4, 0, 0, 2, 5, 0, 0},
                {0, 5, 2, 5, 1, 3, 2, 1},
                {0, 3, 2, 5, 3, 3, 4, 2},
        };
        // We should match a 3 near the top of the grid because we're avoiding side matches
        board = createGameBoard(lowSide3Match);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getGem1Position().getY() > 3)
            System.out.println("lowSide3Match failure. " + ++failures);

        System.out.println("\n");
        int[][] noValidMoves = new int[][]{
                {1, 2, 2, 1, 4, 2, 3, 3},
                {1, 1, 0, 0, 1, 2, 0, 2},
                {4, 3, 3, 0, 5, 3, 1, 2},
                {2, 1, 1, 4, 2, 1, 3, 4},
                {0, 4, 6, 2, 1, 0, 4, 3},
                {4, 2, 5, 0, 7, 6, 6, 0},
                {6, 5, 5, 4, 7, 3, 7, 4},
                {7, 2, 6, 7, 4, 1, 7, 1},
        };
        // Num cleared should return as 0 since there are no valid moves
        board = createGameBoard(noValidMoves);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() !=0)
            System.out.println("noValidMoves failure. " + ++failures);

        System.out.println("\n");
        int[][] tooManyMatchedBug = new int[][]{
                {1, 0, 1, 0, 0, 3, 0, 2},
                {3, 3, 0, 3, 0, 2, 1, 2},
                {0, 1, 3, 2, 3, 3, 0, 1},
                {2, 3, 3, 0, 3, 1, 3, 3},
                {0, 2, 0, 3, 0, 2, 2, 1},
                {3, 4, 3, 2, 0, 2, 0, 0},
                {4, 2, 5, 0, 5, 1, 1, 0},
                {3, 2, 0, 0, 2, 0, 4, 2},
        };
        // Num cleared should return as 0 since there are no valid moves
        board = createGameBoard(tooManyMatchedBug);
        optimalMove = BoardSolver.getOptimalMove(board);
        if (optimalMove.getNumCleared() >5)
            System.out.println("tooManyMatchedBug failure. " + ++failures);


        System.out.println("\nTest Failures: " + failures);

    }

    public static GameBoard createGameBoard(int[][] gemNumbers) {
        List<List<Gem>> gameBoard = new ArrayList<>();
        for (int y = 0; y < gemNumbers.length; y++) {
            for (int x = 0; x < gemNumbers[y].length; x++) {
                Gem gem = new Gem(new Point(x, y), gemNumbers[y][x]);
                if (gameBoard.size() <= y)
                    gameBoard.add(new ArrayList<>());
                gameBoard.get(y).add(gem);
            }
        }
        GameBoard newBoard = new GameBoard(gameBoard);
        System.out.println(newBoard);
        return newBoard;
    }

}
