public class Cell {
    private int x;
    private int y;
    private String cellStatus;
    private Cell up;
    private Cell down;
    private Cell left;
    private Cell right;
    private double fScore;
    private double gScore;
    private Cell parent;

    public Cell(int x, int y, String cellStatus) {
        this.x = x;
        this.y = y;
        this.cellStatus  = cellStatus;
        this.up = null;
        this.down = null;
        this.left = null;
        this.right = null;
        this.fScore = Double.POSITIVE_INFINITY;
        this.gScore = Double.POSITIVE_INFINITY;
        this.parent = null;
    }
    // Getters and setters
    public void setX (int x) {
        this.x = x;
    }
    public int getX() {
        return x;
    }
    public void setY (int y) {
        this.y = y;
    }
    public int getY() {
        return y;
    }
    public void setCellStatus (String cellStatus) {
        this.cellStatus = cellStatus;
    }
    public String getCellStatus() {
        return cellStatus;
    }
    public Cell getUp() {
        return up;
    }

    public void setUp(Cell up) {
        this.up = up;
    }

    public Cell getDown() {
        return down;
    }

    public void setDown(Cell down) {
        this.down = down;
    }

    public Cell getRight() {
        return right;
    }

    public void setRight(Cell right) {
        this.right = right;
    }

    public Cell getLeft() {
        return left;
    }

    public void setLeft(Cell left) {
        this.left = left;
    }

    public void setFScore (double fScore) {
        this.fScore = fScore;
    }
    public double getFScore() {
        return fScore;
    }
    public void setGScore (double gScore) {
        this.gScore = gScore;
    }
    public double getGScore() {
        return gScore;
    }
    public void setParent (Cell parent) {
        this.parent = parent;
    }
    public Cell getParent() {
        return parent;
    }

}
