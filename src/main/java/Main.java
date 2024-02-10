import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<Integer, Map<String, Integer>> PIXELS = new HashMap<>();

    private static void addPix(String[] keys, int[] values) {
        HashMap<String, Integer> toPut = new HashMap<String, Integer>();

        for (int i = 0; i < keys.length; i++) {
            toPut.put(keys[i], values[i]);
        }

        PIXELS.put(PIXELS.size(), toPut);
    }

    public static void main(String[] args) {
        int numIters = 5000;
        int[][] Matrix = SandPhysics.GenerateNewMatrix(500, 500, 0);
        SandPhysics.DrawRect(Matrix, 10, 10, 50, 40, 1);

        float start = System.nanoTime();
        for (int i = 0; i < numIters; i++) {
            SandPhysics.IterateMatrixPhysics(Matrix);

            if (i % 200 == 0) {
                SandPhysics.DrawRect(Matrix, 250, 0, 499, 499, 0);
                SandPhysics.DrawRect(Matrix, 10, 10, 250, 240, 1);
            }
        }

//        SandPhysics.PrintMatrix(Matrix);
        System.out.printf("AVG: %s FPS", Math.round(numIters / (System.nanoTime() - start) * Math.pow(10, 9) * 100.0) / 100.0);
    }
}
