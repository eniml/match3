/**
 * Created by eniml on 05.06.2016.
 */
class MassBox {
    private int numberOfCells;
    private int lastBoxX;
    private int lastBoxY;

    public MassBox(int numberOfCells, int lastBoxX, int lastBoxY) {
        this.numberOfCells = numberOfCells;
        this.lastBoxX = lastBoxX;
        this.lastBoxY = lastBoxY;
    }


    public int getNumberOfCells() {
        return numberOfCells;
    }

    public void setNumberOfCells(int numberOfCells) {
        this.numberOfCells = numberOfCells;
    }

    public int getLastBoxX() {
        return lastBoxX;
    }

    public void setLastBoxX(int lastBoxX) {
        this.lastBoxX = lastBoxX;
    }

    public int getLastBoxY() {
        return lastBoxY;
    }

    public void setLastBoxY(int lastBoxY) {
        this.lastBoxY = lastBoxY;
    }
}

