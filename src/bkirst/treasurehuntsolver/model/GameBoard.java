package bkirst.treasurehuntsolver.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class GameBoard {
    List<List<Gem>> boardState = new ArrayList<>();

    private int numCleared = 0;
    private int levelCleared;
    private static boolean simulationMode;

    enum EnumDirection {
        UP, DOWN, LEFT, RIGHT;
    }

    public GameBoard() {
        simulationMode = true;
        generateRandomBoard();
        // Generate random boards until we get one with no matches
        while (executeClear())
            generateRandomBoard();

    }

    public GameBoard(List<BufferedImage> imageRanks, Map<BufferedImage, List<Point>> locations) {
        simulationMode = false;
        for (int y = 0; y < 8; y++) {
            boardState.add(new ArrayList<>());
        }

        List<Gem> semiSortedPoints = new ArrayList<>();
        for (Map.Entry entry : locations.entrySet()) {
            for (Point screenPoint : (List<Point>) entry.getValue()) {
                semiSortedPoints.add(new Gem(screenPoint, imageRanks.indexOf(entry.getKey())));
            }
        }

        Collections.sort(semiSortedPoints);
        // Every 8 values should be a row, but each row is out of order;
        for (int y = 0; y < 8; y++) {
            List<Gem> row = new ArrayList<>();
            // Get our row that needs to be sorted by screenX value
            for (int i = y * 8; i < y * 8 + 8; i++) {
                row.add(semiSortedPoints.get(i));
            }
            for (int i = 0; i < 8; i++) {
                // Find the lowest X Value in the row and add it to the row on the board
                Gem minXGem = null;
                for (Gem gem : row) {
                    if (minXGem == null || gem.getScreenX() < minXGem.getScreenX())
                        minXGem = gem;
                }
                row.remove(minXGem);
                boardState.get(y).add(minXGem);
            }

        }
    }

    public GameBoard(List<List<Gem>> boardState) {
        this.boardState = boardState;
    }

    private void generateRandomBoard() {
        boardState = new ArrayList<>();
        for (int y = 0; y < 8; y++) {
            boardState.add(new ArrayList<>());
            for (int x = 0; x < 8; x++) {
                boardState.get(y).add(new Gem(new Point(x, y), new Random().nextInt(4)));
            }
        }
    }

    private void calculateClear() {
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                int numChained = this.countChain(x, y);
                int newLevelCleared = getGem(x, y).getLevel();
                if (numChained > numCleared || (numChained == numCleared && newLevelCleared > levelCleared)) {
                    numCleared = numChained;
                    levelCleared = newLevelCleared;
                }
            }
        }
        if (numCleared <=2)
            numCleared=0;
    }

    // Fills in holes in the board with random gems
    public void populateBoard()
    {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (getGem(x, y).getLevel() < 0) {
                    boardState.get(y).get(x).setLevel(new Random().nextInt(4));
                }
            }
        }
    }


    public boolean executeClear() {
        boolean gemsCleared = false;
        for (int y = 7; y >= 0; y--) {
            for (int x = 0; x < 8; x++) {
                if (getGem(x, y).getLevel() >= 0)
                    if (this.clearGems(x, y))
                        gemsCleared = true;
            }
        }
        return gemsCleared;
    }

    public void executeDrop() {
        while (!isStable()) {
            for (int y = 7; y > 0; y--) {
                for (int x = 0; x < 8; x++) {
                    if (getGem(x, y).getLevel() < 0) {
                        if (getGem(x, y-1).getLevel() != -1)
                        {
                            Gem emptySlot = getGem(x, y);
                            Gem fallingGem = getGem(x, y-1);
                            boardState.get(y-1).set(x, emptySlot);
                            boardState.get(y).set(x, fallingGem);
                        }
                    }
                }
            }
        }
    }

    public boolean isStable() {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (getGem(x, y).getLevel() < 0) {
                    for (int newY = y; newY >= 0; newY--) {
                        if (getGem(x, newY).getLevel() != -1)
                            return false;
                    }
                }
            }
        }
        return true;
    }


    private int countChain(int x, int y) {
        int level = getGem(x, y).getLevel();
        // Can't match vaults or holes
        if (level >=7 || level < 0)
        {
            return 0;
        }
        int upCount = countChain(0, EnumDirection.UP, x, y, level);
        if (upCount >= 3) {
            int extraMatches = 0;
            for (int i = 0; i < upCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.LEFT, x, y - i, level);
                if (potentialMatches > extraMatches)
                    extraMatches = potentialMatches;
                int potentialMatches2 = countChain(0, EnumDirection.RIGHT, x, y - i, level);
                if (potentialMatches2 > extraMatches)
                    extraMatches = potentialMatches2;
            }
            if (extraMatches >= 3)
                upCount += extraMatches - 1;
        }
        int downCount = countChain(0, EnumDirection.DOWN, x, y, level);
        if (downCount >= 3) {
            int extraMatches = 0;
            for (int i = 0; i < downCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.LEFT, x, y + i, level);
                if (potentialMatches > extraMatches)
                    extraMatches = potentialMatches;
                int potentialMatches2 = countChain(0, EnumDirection.RIGHT, x, y + i, level);
                if (potentialMatches2 > extraMatches)
                    extraMatches = potentialMatches2;
            }
            if (extraMatches >= 3)
                downCount += extraMatches - 1;
        }
        int leftCount = countChain(0, EnumDirection.LEFT, x, y, level);
        if (leftCount >= 3) {
            int extraMatches = 0;
            for (int i = 0; i < leftCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.DOWN, x - i, y, level);
                if (potentialMatches > extraMatches)
                    extraMatches = potentialMatches;
                int potentialMatches2 = countChain(0, EnumDirection.UP, x - i, y, level);
                if (potentialMatches2 > extraMatches)
                    extraMatches = potentialMatches2;
            }
            if (extraMatches >= 3)
                leftCount += extraMatches - 1;
        }
        int rightCount = countChain(0, EnumDirection.RIGHT, x, y, level);
        if (rightCount >= 3) {
            int extraMatches = 0;
            for (int i = 0; i < rightCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.DOWN, x + i, y, level);
                if (potentialMatches > extraMatches)
                    extraMatches = potentialMatches;
                int potentialMatches2 = countChain(0, EnumDirection.UP, x + i, y + i, level);
                if (potentialMatches2 > extraMatches)
                    extraMatches = potentialMatches2;
            }
            if (extraMatches >= 3)
                rightCount += extraMatches - 1;
        }
        ArrayList<Integer> chainCounts = new ArrayList<>();
        chainCounts.add(upCount);
        chainCounts.add(downCount);
        chainCounts.add(leftCount);
        chainCounts.add(rightCount);
        return Collections.max(chainCounts);
    }

    private boolean clearGems(int x, int y) {
        boolean gemsCleared = false;
        int level = getGem(x, y).getLevel();
        int upCount = countChain(0, EnumDirection.UP, x, y, level);
        if (upCount >= 3) {
            gemsCleared = true;
            for (int i = 0; i < upCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.LEFT, x, y - i, level);
                if (potentialMatches >= 3) {
                    for (int j = 0; j < potentialMatches; j++) {
                        boardState.get(y - i).get(x - j).setLevel(-1);
                    }
                }
                int potentialMatches2 = countChain(0, EnumDirection.RIGHT, x, y - i, level);
                if (potentialMatches2 >= 3) {
                    for (int j = 0; j < potentialMatches2; j++) {
                        boardState.get(y - i).get(x + j).setLevel(-1);
                    }
                }
            }
            for (int j = 0; j < upCount; j++) {
                boardState.get(y - j).get(x).setLevel(-1);
            }
        }
        int downCount = countChain(0, EnumDirection.DOWN, x, y, level);
        if (downCount >= 3) {
            gemsCleared = true;

            int extraMatches = 0;
            for (int i = 0; i < downCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.LEFT, x, y + i, level);
                if (potentialMatches >= 3) {
                    for (int j = 0; j < potentialMatches; j++) {
                        boardState.get(y + i).get(x - j).setLevel(-1);
                    }
                }
                int potentialMatches2 = countChain(0, EnumDirection.RIGHT, x, y + i, level);
                if (potentialMatches2 >= 3) {
                    for (int j = 0; j < potentialMatches2; j++) {
                        boardState.get(y + i).get(x + j).setLevel(-1);
                    }
                }
                for (int j = 0; j < downCount; j++) {
                    boardState.get(y + j).get(x).setLevel(-1);
                }
            }
        }
        int leftCount = countChain(0, EnumDirection.LEFT, x, y, level);
        if (leftCount >= 3) {
            gemsCleared = true;

            int extraMatches = 0;
            for (int i = 0; i < leftCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.DOWN, x - i, y, level);
                if (potentialMatches >= 3) {
                    for (int j = 0; j < potentialMatches; j++) {
                        boardState.get(y + i).get(x - i).setLevel(-1);
                    }
                }
                int potentialMatches2 = countChain(0, EnumDirection.UP, x - i, y, level);
                if (potentialMatches2 >= 3) {
                    for (int j = 0; j < potentialMatches2; j++) {
                        boardState.get(y - i).get(x - i).setLevel(-1);
                    }
                }
                for (int j = 0; j < leftCount; j++) {
                    boardState.get(y).get(x - j).setLevel(-1);
                }
            }
        }
        int rightCount = countChain(0, EnumDirection.RIGHT, x, y, level);
        if (rightCount >= 3) {
            gemsCleared = true;

            int extraMatches = 0;
            for (int i = 0; i < rightCount; i++) {
                int potentialMatches = countChain(0, EnumDirection.DOWN, x + i, y, level);
                if (potentialMatches >= 3) {
                    for (int j = 0; j < potentialMatches; j++) {
                        boardState.get(y + i).get(x + i).setLevel(-1);
                    }
                }
                int potentialMatches2 = countChain(0, EnumDirection.UP, x + i, y + i, level);
                if (potentialMatches2 >= 3) {
                    for (int j = 0; j < potentialMatches2; j++) {
                        boardState.get(y - i).get(x + i).setLevel(-1);
                    }
                }
                for (int j = 0; j < rightCount; j++) {
                    boardState.get(y).get(x + j).setLevel(-1);
                }
            }
        }
        return gemsCleared;
    }

    private int countChain(int currentCount, EnumDirection direction, int x, int y, int gemLevel) {
        Gem currentGem = getGem(x, y);
        if (currentGem == null || currentGem.getLevel() != gemLevel)
            return currentCount;
        switch (direction) {
            case UP:
                return countChain(currentCount + 1, direction, x, y - 1, gemLevel);
            case DOWN:
                return countChain(currentCount + 1, direction, x, y + 1, gemLevel);
            case LEFT:
                return countChain(currentCount + 1, direction, x - 1, y, gemLevel);
            case RIGHT:
            default:
                return countChain(currentCount + 1, direction, x + 1, y, gemLevel);
        }
    }

    public Gem getGem(Point gemIndex) {
        int y = (int) gemIndex.getY();
        int x = (int) gemIndex.getX();
        if (x > 7 || x < 0 || y > 7 || y < 0)
            return null;
        return boardState.get(y).get(x);
    }

    public Gem getGem(int x, int y) {
        return getGem(new Point(x, y));
    }

    public Point getIndexPosition(Gem gem) {
        for (int y = 0; y < 8; y++) {
            if (boardState.get(y).indexOf(gem) != -1) {
                return new Point(boardState.get(y).indexOf(gem), y);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        String toReturn = new String();
        for (List<Gem> row : boardState) {
            toReturn += "{";
            for (Gem gem : row) {
                toReturn += gem.getLevel() + ", ";
            }
            toReturn = toReturn.substring(0, toReturn.length() - 2);
            toReturn += ("},\n");
        }
        return toReturn;
    }

    public List<List<Gem>> getBoardState() {
        return boardState;
    }

    public GameBoard copyDeep() {
        List<List<Gem>> newBoard = new ArrayList<>();
        for (List<Gem> oldRow : boardState) {
            List<Gem> newRow = new ArrayList<Gem>();
            for (Gem gem : oldRow) {
                newRow.add(gem.copyDeep());
            }
            newBoard.add(newRow);
        }
        return new GameBoard(newBoard);
    }

    public void executeMove(Move move) {
        // Swap the gems on the board
        Point gem1Location = this.getIndexPosition(move.getGem1());
        int gem1Level = move.getGem1().getLevel();
        Point gem2Location = this.getIndexPosition(move.getGem2());
        int gem2Level = move.getGem2().getLevel();
        // You can't move vaults
        if (gem1Level == 7 || gem2Level == 7)
        {
            return;
        }
        boardState.get((int) gem1Location.getY()).set((int) gem1Location.getX(), move.getGem2());
        boardState.get((int) gem2Location.getY()).set((int) gem2Location.getX(), move.getGem1());

        // Count the clears for the original swap
        calculateClear();


        // Clear the matching gems
        executeClear();

        // Put back the gem we moved that matched at the proper level
        if (getGem(gem1Location).getLevel() == -1)
            getGem(gem1Location).setLevel(gem2Level + 1);
        if (getGem(gem2Location).getLevel() == -1)
            getGem(gem2Location).setLevel(gem1Level + 1);
        // Count the clears again in case our new gem caused another clear
        calculateClear();

        // First drop
        executeClear();
        executeDrop();
        calculateClear();

        Integer numClearedFromOriginalMove = this.numCleared;
        Integer levelClearedFromOriginalMove = this.levelCleared;
        Integer maxNumClearedFromCascade = this.numCleared;

        if (simulationMode) {
            populateBoard();
            calculateClear();
            // Keep executing clears and drops until the known board is done cascading
            // This is just for the simulation so we have a valid next board state
            while (executeClear()) {
                executeDrop();
                populateBoard();
                calculateClear();
                maxNumClearedFromCascade = Math.max(this.numCleared, maxNumClearedFromCascade);
            }
        }

        move.setLevelCleared(levelClearedFromOriginalMove);
        move.setNumClearedWithCascade(maxNumClearedFromCascade);
        move.setNumCleared(numClearedFromOriginalMove);
        move.setFinalBoard(this);
        if (!simulationMode) {
            if (levelClearedFromOriginalMove < this.numCleared && this.numCleared > 3) {
                System.out.println("Fancy move found " + gem1Location + " " + gem2Location + " for " + numCleared);
            } else if (this.numCleared > 3) {
                System.out.println("Good move found " + gem1Location + " " + gem2Location + " for " + numCleared);
            }
        }
    }
}
