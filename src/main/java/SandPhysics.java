import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SandPhysics {
    public static boolean MovePixIfCan(int[][] matrix, int x, int y, int ax, int ay) {
        if (CanMoveBorders(matrix, x, y, ax, ay) && CanMovePhysics(matrix, x, y, ax, ay)) {
            MovePix(matrix, x, y, ax, ay);
            return true;
        }
        return false;
    }

    public static void MovePix(int[][] matrix, int x, int y, int ax, int ay) {
        int temp = matrix[x][y];
        matrix[x][y] = matrix[x + ax][y + ay];
        matrix[x + ax][y + ay] = temp;
    }

    public static boolean CanMove(int[][] matrix, int x, int y, int ax, int ay) {
        return (CanMoveBorders(matrix, x, y, ax, ay)) && (CanMovePhysics(matrix, x, y, ax, ay));
    }

    public static boolean CanMoveBorders(int[][] matrix, int x, int y, int ax, int ay) {
        return 0 <= x + ax && x + ax < matrix.length && 0 <= y + ay && y + ay < matrix[0].length;
    }

    public static boolean CanMovePhysics(int[][] matrix, int x, int y, int ax, int ay) {
        return matrix[x][y] > matrix[x + ax][y + ay];
    }

    public static void IterateMatrixPhysics(int[][] matrix) {
        int lenX = matrix.length;
        int lenY = matrix[0].length;
        Random rand = new Random();

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int x = lenX - 1; x >= 0; x--) {
            final int finalX = x;
            executor.submit(() -> processRow(matrix, finalX, lenY, rand));
        }

        executor.shutdown();
    }

    private static void processRow(int[][] matrix, int x, int lenY, Random rand) {
        for (int y = 0; y < lenY; y++) {
            if (!MovePixIfCan(matrix, x, y, 1, 0)) {
                boolean canMoveRight = CanMove(matrix, x, y, 1, 1);
                boolean canMoveLeft = CanMove(matrix, x, y, 1, -1);

                if (canMoveRight && canMoveLeft) {
                    if (rand.nextFloat() <= 0.5) {
                        MovePix(matrix, x, y, 1, 1);
                    } else {
                        MovePix(matrix, x, y, 1, -1);
                    }
                } else if (canMoveRight) {
                    MovePix(matrix, x, y, 1, 1);
                } else if (canMoveLeft) {
                    MovePix(matrix, x, y, 1, -1);
                }
            }
        }
    }

    public static void PrintMatrix(int[][] matrix) {
        for (int[] ints : matrix) {
            for (int el : ints) {
                System.out.print(el + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[][] GenerateNewMatrix(int cols, int rows, int fillValue) {
        int[][] matrix = new int[cols][rows];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                matrix[i][j] = fillValue;
            }
        }
        return matrix;
    }

    public static void DrawRect(int[][] matrix, int x0, int y0, int x1, int y1, int pixtype) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                matrix[x][y] = pixtype;
            }
        }
    }
}
