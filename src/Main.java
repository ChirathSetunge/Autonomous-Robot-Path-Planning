import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome, Your about to witness An autonomous robot navigate through a grid-based\n" +
                "environment with obstacles, and outputs the most optimal path from starting cell to goal cell.");
        System.out.println();
        System.out.println("""
                NOTE: If you request 10x10 grid it means your requesting 10x10 area to robot to move,
                NOTE: For better readability and user understanding,
                      the grid indexing is starting from 1 to n, its not like the conventional ways of indexing 0 to n-1""");
        System.out.println();
        int rows = 0, cols = 0;
        boolean validInput = false;


        do {
            System.out.print("Enter the number of rows (must be an integer): ");

            try {
                rows = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter an integer for the number of rows.");
                scanner.nextLine(); // Consume invalid input
            }
        } while (!validInput);

        validInput = false; // Reset for column validation

        do {
            System.out.print("Enter the number of columns (must be an integer): ");

            try {
                cols = scanner.nextInt();
                validInput = true;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input: Please enter an integer for the number of columns.");
                scanner.nextLine(); // Consume invalid input
            }
        } while (!validInput);

        Grid grid = new Grid(rows + 2, cols + 2);
        grid.initializeGrid();
        grid.display();

        grid.getStartCellFromUser(scanner);
        grid.getGoalCellFromUser(scanner);

        System.out.print("Do you want to add obstacles randomly (y/n)?: ");
        String choice = scanner.next();

        if (choice.equalsIgnoreCase("y")) {
            grid.addObstaclesRandomly();  // Call the method for random obstacles
        } else {
            grid.addObstacles(scanner);   // Call the method for user-defined obstacles
        }


        grid.display();
        long startTime = System.currentTimeMillis();

        // Find the most optimal path
        CustomArrayList<Cell> optimalPath = grid.findOptimalPathAStar();
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        // Display the optimal path
        grid.displayOptimalPath(optimalPath);

        // Display the optimal path coordinates and orientations
        System.out.println("The robot is starting his journey facing \"North\"");
        grid.displayOrientation(optimalPath);
        System.out.println();
        // Display the optimal path time
        System.out.println("Time elapsed for the algorithm: " + elapsedTime/1000.0+"s");

        scanner.close();
    }
}
