import java.awt.*;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class PhysicsModel {
    private final Color[] COLORS;
    private final double[] DENSITIES;
    private final double[] DEFAULT_TEMPERATURES;
    private final String[] NAMES;
    private final short[] SUBS;
    private final double[] HEAT_CAPACITIES;
    private final double[] HEAT_COEFFICIENTS;
    private final int[][] TRANSFORMATIONS_TO_IDS;
    private final double[][] TRANSFORMATIONS_TEMPERATURES;
    private final int[][] Matrix;
    private final int ROWS;
    private final int COLS;

    private static double CtoK(int tempC) {
        return tempC + 273.15;
    }

    public PhysicsModel(int rows, int cols, int idFill) {
        this.ROWS = rows;
        this.COLS = cols;
        this.Matrix = new int[cols][rows];
        this.COLORS = new Color[cols * rows];
        this.DENSITIES = new double[cols * rows];
        this.DEFAULT_TEMPERATURES = new double[cols * rows];
        this.NAMES = new String[cols * rows];
        this.SUBS = new short[cols * rows];
        this.HEAT_CAPACITIES = new double[cols * rows];
        this.HEAT_COEFFICIENTS = new double[cols * rows];
        this.TRANSFORMATIONS_TO_IDS = new int[cols * rows][];
        this.TRANSFORMATIONS_TEMPERATURES = new double[cols * rows][];

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                this.Matrix[i][j] = idFill;
            }
        }

        DrawRect(100, 50, 150, 150, 1);
    }

    private void addPix(short R, short G, short B, double density, double defTemp, String name, short sub,
                        double heatCap, double heatCoef, int[] transfrmsToIDS, double[] transfrmsTemps) {
        int curID = NAMES.length;

        this.COLORS[curID] = new Color(R, G, B);
        this.DENSITIES[curID] = density;
        this.DEFAULT_TEMPERATURES[curID] = defTemp;
        this.NAMES[curID] = name;
        this.SUBS[curID] = sub;
        this.HEAT_CAPACITIES[curID] = heatCap;
        this.HEAT_COEFFICIENTS[curID] = heatCoef;
        this.TRANSFORMATIONS_TO_IDS[curID] = transfrmsToIDS;
        this.TRANSFORMATIONS_TEMPERATURES[curID] = transfrmsTemps;
    }

    private boolean MovePixIfCan(int x, int y, int ax, int ay) {
        if (CanMoveBorders(x, y, ax, ay) && CanMovePhysics(x, y, ax, ay)) {
            MovePix(x, y, ax, ay);
            return true;
        }
        return false;
    }

    private void MovePix(int x, int y, int ax, int ay) {
        int temp = this.Matrix[x][y];
        this.Matrix[x][y] = this.Matrix[x + ax][y + ay];
        this.Matrix[x + ax][y + ay] = temp;
    }

    private boolean CanMove(int x, int y, int ax, int ay) {
        return (CanMoveBorders(x, y, ax, ay)) && (CanMovePhysics(x, y, ax, ay));
    }

    private boolean CanMoveBorders(int x, int y, int ax, int ay) {
        return 0 <= x + ax && x + ax < this.ROWS && 0 <= y + ay && y + ay < this.COLS;
    }

    private boolean CanMovePhysics(int x, int y, int ax, int ay) {
        return this.Matrix[x][y] > this.Matrix[x + ax][y + ay];
    }

    public void IterateMatrixPhysics() {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1);

        for (int x = this.ROWS - 1; x >= 0; x--) {
            final int finalX = x;
            executor.submit(() -> processRow(finalX));
        }
        executor.shutdown();
    }

    private void processRow(int x) {
        Random rand = new Random();
        for (int y = 0; y < this.COLS; y++) {
            if (!MovePixIfCan(x, y, 1, 0)) {
                boolean canMoveRight = CanMove(x, y, 1, 1);
                boolean canMoveLeft = CanMove(x, y, 1, -1);

                if (canMoveRight && canMoveLeft) {
                    if (rand.nextFloat() <= 0.5) {
                        MovePix(x, y, 1, 1);
                    } else {
                        MovePix(x, y, 1, -1);
                    }
                } else if (canMoveRight) {
                    MovePix(x, y, 1, 1);
                } else if (canMoveLeft) {
                    MovePix(x, y, 1, -1);
                }
            }
        }
    }

    public void PrintMatrix() {
        for (int[] ints : this.Matrix) {
            for (int el : ints) {
                System.out.print(el + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public void DrawRect(int x0, int y0, int x1, int y1, int pixtype) {
        for (int x = x0; x < x1; x++) {
            for (int y = y0; y < y1; y++) {
                this.Matrix[x][y] = pixtype;
            }
        }
    }
}
