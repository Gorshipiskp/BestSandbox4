public class Main {

    public static void main(String[] args) {
        int numIters = 15000;

        PhysicsModel Model = new PhysicsModel(250, 250, 0);

        double start = System.nanoTime();
        for (int i = 0; i < numIters; i++) {
            Model.IterateMatrixPhysics();

//            if (i % 500 == 0) {
//                Model.DrawRect(250, 0, 499, 499, 0);
//                Model.DrawRect(10, 10, 250, 240, 1);
//            }
        }

        System.out.printf("AVG: %s FPS\n\n", Math.round(numIters / (System.nanoTime() - start) * Math.pow(10, 9) * 100.0) / 100.0);
        Model.PrintMatrix();
    }
}
