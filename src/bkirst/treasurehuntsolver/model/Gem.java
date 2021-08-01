package bkirst.treasurehuntsolver.model;

import java.awt.*;

public class Gem implements Comparable {
    public int screenX;
    public int screenY;
    public int level;

    public Gem() {
    }

    public Gem(Point screenPoint, int level) {
        this.level = level;
        this.screenX = (int) screenPoint.getX();
        this.screenY = (int) screenPoint.getY();
    }

    public void setScreenX(int screenX) {
        this.screenX = screenX;
    }

    public void setScreenY(int screenY) {
        this.screenY = screenY;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int compareTo(Object o) {
        if (!(o instanceof Gem)) {
            throw new ClassCastException();
        }
        Gem otherGem = (Gem) o;
        if (this.screenY > otherGem.getScreenY())
            return 1;
        if (this.screenY < otherGem.getScreenY())
            return -1;
        if (this.screenX < otherGem.getScreenX())
            return -1;
        if (this.screenX > otherGem.getScreenX())
            return 1;
        return 0;
    }

    public Gem copyDeep() {
        Gem gem = new Gem();
        gem.setLevel(this.getLevel());
        gem.setScreenX(this.getScreenX());
        gem.setScreenY(this.getScreenY());
        return gem;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Gem))
            return false;

        return o.toString().equals(this.toString());
    }

    @Override
    public String toString() {
        return "(" + this.getScreenX() + ", " + this.getScreenY() + ") Level " + this.level;
    }


}
