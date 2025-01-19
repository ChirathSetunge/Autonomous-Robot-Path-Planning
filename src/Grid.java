import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Grid {
    private int rows;
    private int cols;
    private Cell[][] grid;
    private Cell startCell;
    private Cell goalCell;

    public Grid(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];

    }
    //getters ans setters

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getRows() {
        return rows;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public int getCols() {
        return cols;
    }


    public Cell[][] getGrid() {
        return grid;
    }


    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }
    // Method to initialize the grid
    public void initializeGrid() {
        // Loop through each cell in the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                String cellStatus = "Clear"; // Initially all cells are clear
                // Set obstacle status for boundary cells
                if (i == 0 || j == 0 || i == rows - 1 || j == cols - 1) {
                    cellStatus = "Obstacle";
                }
                grid[i][j] = new Cell(i, j, cellStatus);
            }
        }
        linkCells();  // Link cells to each other
    }
    // Method to link cells to their neighboring cells
    public void linkCells() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i > 0)
                    grid[i][j].setUp(grid[i - 1][j]);
                if (i < rows - 1)
                    grid[i][j].setDown(grid[i + 1][j]);
                if (j > 0)
                    grid[i][j].setLeft(grid[i][j - 1]);
                if (j < cols - 1)
                    grid[i][j].setRight(grid[i][j + 1]);
            }
        }
    }
    //ANSI color codes
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_RESET = "\u001B[0m";

    // Display the grid
    public void display() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j].getCellStatus().equals("Robot")) {
                    System.out.print(ANSI_GREEN +"R " + ANSI_RESET);
                } else if (grid[i][j].getCellStatus().equals("Start")){
                    System.out.print(ANSI_RED +"S " + ANSI_RESET);
                } else if (grid[i][j].getCellStatus().equals("Goal")) {
                    System.out.print(ANSI_RED +"G " + ANSI_RESET);
                } else {
                    System.out.print(grid[i][j].getCellStatus().charAt(0) + " ");
                }
            }
            System.out.println();
        }
    }
    // Get start cell from user input
    public void getStartCellFromUser(Scanner scanner) {
        // Variables to store user input
        int startX ;
        int startY;
        boolean validInput = false;
        // Loop until valid input is provided
        do {
            System.out.println("Enter the starting position (within boundaries: X -> (1 to " + (rows - 2) + ") " + "and Y -> (1 to " + (cols - 2) + ")");
            System.out.print("X coordinate: ");

            try {
                startX = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter an integer between 1 and " + (rows - 2));
                scanner.nextLine();
                continue;
            }
            System.out.print("Y coordinate: ");

            try {
                startY = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter an integer between 1 and " + (cols - 2));
                scanner.nextLine();
                continue;
            }
            if (startX < 1 || startX > rows - 2 || startY < 1 || startY > cols - 2) {
                System.out.println("Invalid Start position. Coordinates are out of grid boundaries.");
            } else {
                startCell = grid[startX][startY];
                startCell.setCellStatus("Start"); // Update CellStatus to "Start"
                System.out.println("Start position set to (" + startX + ", " + startY + ")");
                validInput = true;
            }
        } while(!validInput);

    }

    // Get goal cell from user input
    public void getGoalCellFromUser(Scanner scanner) {
        // Variables to store user input
        int goalX;
        int goalY;
        boolean validInput = false;
        // Loop until valid input is provided
        do {
            System.out.println("Enter the goal position (within boundaries: X -> (1 to " + (rows - 2) + ") " + "and Y -> (1 to " + (cols - 2) + ")");
            System.out.print("X coordinate: ");

            try {
                goalX = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter an integer between 1 and " + (rows - 2));
                scanner.nextLine();
                continue;
            }

            System.out.print("Y coordinate: ");

            try {
                goalY = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter an integer between 1 and " + (cols - 2));
                scanner.nextLine();
                continue;
            }

            if (goalX < 1 || goalX > rows - 2 || goalY < 1 || goalY > cols - 2) {
                System.out.println("Invalid Goal position. Coordinates are out of grid boundaries.");
            } else if (goalX == startCell.getX() && goalY == startCell.getY()) {
                System.out.println("Invalid Goal position. Goal cannot be placed on the Start position.");
            } else {
                goalCell = grid[goalX][goalY];
                goalCell.setCellStatus("Goal"); // Update  CellStatus to "Goal"
                System.out.println("Goal position set to (" + goalX + ", " + goalY + ")");
                validInput = true;
            }
        } while (!validInput);
    }

    // Find optimal path using A* algorithm
    public CustomArrayList<Cell> findOptimalPathAStar() {
        CustomPriorityQueue<Cell> openSet = new CustomPriorityQueue<>();

        // Initialize scores for all Cells

        startCell.setGScore(0);
        startCell.setFScore(heuristic(startCell, goalCell));
        openSet.offer(startCell);

        while (!openSet.isEmpty()) {
            Cell current = openSet.poll();
            if (current == goalCell) {
                return reconstructPath(current);
            }

            for (int i = 0; i < getNeighbors(current).size(); i++) {
                Cell neighbor = getNeighbors(current).get(i);
                double tentativeGScore = current.getGScore() + 1; // Assuming each step cost is 1
                if (tentativeGScore < neighbor.getGScore()) { //comparing tentative G
                    neighbor.setGScore(tentativeGScore);
                    double fScores = tentativeGScore + heuristic(neighbor, goalCell);
                    neighbor.setFScore(fScores);
                    neighbor.setParent(current);
                    if (!openSet.contains(neighbor))
                        openSet.offer(neighbor);
                }
            }

        }

        System.out.println("No optimal path found.");
        return new CustomArrayList<>(); // Return an empty list indicating no valid path
    }

    // Heuristic function for A* algorithm
    private double heuristic(Cell a, Cell b) {
        int rowValue = a.getX() - b.getX();
        int colValue = a.getY() - b.getX();
        return rowValue + colValue;
    }
    // Get neighboring cells
    private CustomArrayList<Cell> getNeighbors(Cell cell) {
        CustomArrayList<Cell> neighbors = new CustomArrayList<>();
        if (cell.getUp() != null && !cell.getUp().getCellStatus().equals("Obstacle")) {
            neighbors.add(cell.getUp());
        }
        if (cell.getDown() != null && !cell.getDown().getCellStatus().equals("Obstacle")) {
            neighbors.add(cell.getDown());
        }
        if (cell.getLeft() != null && !cell.getLeft().getCellStatus().equals("Obstacle")) {
            neighbors.add(cell.getLeft());
        }
        if (cell.getRight() != null && !cell.getRight().getCellStatus().equals("Obstacle")) {
            neighbors.add(cell.getRight());
        }
        return neighbors;
    }
    // Reconstruct path from goal to start
    private CustomArrayList<Cell> reconstructPath(Cell current) {
        CustomArrayList<Cell> path = new CustomArrayList<>();
        int count = 0;
        while (current != null) {
            path.add(current);
            current = current.getParent();
            count++;
        }
        System.out.println("Reached the Goal position in "+count +" steps");
        return path;

    }
    // Display optimal path on grid
    public void displayOptimalPath(CustomArrayList<Cell> optimalPath) {
        System.out.println("Optimal Path:");
        if (optimalPath != null) {
            // Update the grid with the optimal path
            for (int i = 0; i < optimalPath.size(); i++) {
                Cell cell = optimalPath.get(i);
                if (i != 0 && i != optimalPath.size() - 1) {
                    cell.setCellStatus("Robot"); // Update block status to 'R' for robot's position
                }
            }

            // Print the updated grid
            display();
        } else {
            System.out.println("No optimal path found.");
        }
    }

    private String getOrientation(Cell currentCell, Cell previousCell) {
        if (previousCell == null) {  // Start cell, assume North
            return "North";
        } else {
            int xDiff = currentCell.getX() - previousCell.getX();
            int yDiff = currentCell.getY() - previousCell.getY();

            if (xDiff > 0) {
                return "South";
            } else if (xDiff < 0) {
                return "North";
            } else if (yDiff > 0) {
                return "East";
            } else if (yDiff < 0) {
                return "West";
            } else {
                // Handle case where cells have the same coordinates (shouldn't happen)
                return "?";
            }
        }
    }

    public void displayOrientation(CustomArrayList<Cell> optimalPath) {
        System.out.println("Optimal Path: Robot's Coordinates & Orientation");
        if (optimalPath != null) {
            Cell previousCell = null;  // Track the previous cell for orientation

            // Iterate in reverse order (goal to start)
            for (int i = optimalPath.size() - 1; i >= 0; i--) {
                Cell cell = optimalPath.get(i);
                String orientation = getOrientation(cell, previousCell);
                previousCell = cell;

                System.out.println("(" + cell.getX() + ", " + cell.getY() + ") " + " The robot is facing: " + orientation);
            }
        } else {
            System.out.println("No optimal path found.");
            // ... handle no path found ...
        }
    }


    public void addObstacles(Scanner scanner) {
        System.out.print("Enter the number of obstacles: ");
        int numObstacles = scanner.nextInt();

        for (int i = 0; i < numObstacles; i++) {
            boolean validObstacle = false;
            int obstacleX, obstacleY;

            do {
                System.out.println("Enter the coordinates for obstacle " + (i + 1) + ":");
                System.out.println("Notice: Start cell is at position (" + startCell.getX() + ", " + startCell.getY() + ")");
                System.out.println("Notice: Goal cell is at position (" + goalCell.getX() + ", " + goalCell.getY() + ")");
                System.out.print("X coordinate: ");

                try {
                    obstacleX = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input: Please enter an integer between 1 and " + (rows - 2) );
                    scanner.nextLine(); // Consume invalid input
                    continue; // Skip to the next iteration of the do-while loop
                }

                System.out.print("Y coordinate: ");

                try {
                    obstacleY = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input: Please enter an integer between 1 and " + (cols - 2));
                    scanner.nextLine(); // Consume invalid input
                    continue; // Skip to the next iteration of the do-while loop
                }

                // Combined validation for invalid placement and out-of-range coordinates
                if ((obstacleX < 1 || obstacleX > rows - 2 || obstacleY < 1 || obstacleY > cols - 2) ||
                        (obstacleX == startCell.getX() && obstacleY == startCell.getY()) ||
                        (obstacleX == goalCell.getX() && obstacleY == goalCell.getY())) {
                    if (obstacleX < 1 || obstacleX > rows - 2 || obstacleY < 1 || obstacleY > cols - 2) {
                        System.out.println("Invalid obstacle placement. Coordinates are out of grid boundaries.");
                    } else {
                        System.out.println("Invalid obstacle placement. Cannot place an obstacle on the start or goal cell.");
                    }
                } else {
                    grid[obstacleX][obstacleY].setCellStatus("Obstacle");
                    validObstacle = true; // Indicate valid obstacle placement
                }
            } while (!validObstacle);
        }
    }

    // In your Grid class:
    public void addObstaclesRandomly() {
        Random random = new Random();
        int numObstacles = (int) ((rows -2) * (cols - 2) * 0.3); // Calculate 30% of total cells

        for (int i = 0; i < numObstacles; i++) {
            int obstacleX, obstacleY;

            do {
                obstacleX = random.nextInt(rows - 2) + 1; // Generate random coordinates within grid boundaries
                obstacleY = random.nextInt(cols - 2) + 1;
            } while (grid[obstacleX][obstacleY].getCellStatus().equals("Start") ||
                    grid[obstacleX][obstacleY].getCellStatus().equals("Goal") ||
                    grid[obstacleX][obstacleY].getX() == startCell.getX() + 1 && grid[obstacleX][obstacleY].getY() == startCell.getY() ||
                    grid[obstacleX][obstacleY].getX() == startCell.getX() - 1 && grid[obstacleX][obstacleY].getY() == startCell.getY() ||
                    grid[obstacleX][obstacleY].getX() == startCell.getX()  && grid[obstacleX][obstacleY].getY() == startCell.getY() + 1 ||
                    grid[obstacleX][obstacleY].getX() == startCell.getX()  && grid[obstacleX][obstacleY].getY() == startCell.getY() - 1 ||
                    grid[obstacleX][obstacleY].getX() == goalCell.getX() + 1 && grid[obstacleX][obstacleY].getY() == goalCell.getY() ||
                    grid[obstacleX][obstacleY].getX() == goalCell.getX() - 1 && grid[obstacleX][obstacleY].getY() == goalCell.getY() ||
                    grid[obstacleX][obstacleY].getX() == goalCell.getX()  && grid[obstacleX][obstacleY].getY() == goalCell.getY() + 1 ||
                    grid[obstacleX][obstacleY].getX() == goalCell.getX()  && grid[obstacleX][obstacleY].getY() == goalCell.getY() - 1 ||
                    grid[obstacleX][obstacleY].getCellStatus().equals("Obstacle")); // Ensure not start, goal and also not around them, or already an obstacle

            grid[obstacleX][obstacleY].setCellStatus("Obstacle");
        }
    }





}
