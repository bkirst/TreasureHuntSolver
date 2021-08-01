import java.awt.*;

public class Move {
    Gem gem1;
    Gem gem2;
    int numCleared;
    int levelCleared;
    Point gem1Position;
    Point gem2Position;
    GameBoard finalBoard;

    public GameBoard getFinalBoard() {
        return finalBoard;
    }

    public void setFinalBoard(GameBoard finalBoard) {
        this.finalBoard = finalBoard;
    }

    public Move(Gem gem1, Gem gem2) {
        this.gem1 = gem1;
        this.gem2 = gem2;
    }

    public Point getGem1Position() {
        return gem1Position;
    }

    public void setGem1Position(Point gem1Position) {
        this.gem1Position = gem1Position;
    }

    public Point getGem2Position() {
        return gem2Position;
    }

    public void setGem2Position(Point gem2Position) {
        this.gem2Position = gem2Position;
    }

    public Gem getGem1() {
        return gem1;
    }

    public void setGem1(Gem gem1) {
        this.gem1 = gem1;
    }

    public Gem getGem2() {
        return gem2;
    }

    public void setGem2(Gem gem2) {
        this.gem2 = gem2;
    }

    public int getNumCleared() {
        return numCleared;
    }

    public void setNumCleared(int numCleared) {
        this.numCleared = numCleared;
    }

    public int getLevelCleared() {
        return levelCleared;
    }

    public void setLevelCleared(int levelCleared) {
        this.levelCleared = levelCleared;
    }
}
