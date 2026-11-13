public class Experiment {
    public static int WARMUP = 300;
    public static int TEST_RANGE = 150;

    public static long measureEach(int[] A, int CUTOFF) {
        long start = System.nanoTime();
        Sort sort = new Sort(A, CUTOFF);
        long end = System.nanoTime();
        return end - start;
    }

    public static void measureSet(int n, Type t){
        int[] original = ArrayManager.createArray(n,t);
        int[] order  = ArrayManager.createArray(TEST_RANGE, Type.RANDOM);

        //Warm up
        for (int i = 0; i<WARMUP; i++){
            int[] A = ArrayManager.clone(original);
            measureEach(A, order[i % TEST_RANGE] + 1);
        }

        //Measurement
        int bestCutoff = -1;
        long bestTime = Long.MAX_VALUE;
        for (int i = 0; i<TEST_RANGE; i++){
            int[] A = ArrayManager.clone(original);
            int cutoff = order[i]+1;
            long currentTime = measureEach(A,cutoff);

            System.out.println("Cutoff: " + cutoff + "; Time elapsed: " + currentTime + "ns");
            if (currentTime < bestTime){
                bestCutoff = cutoff;
                bestTime = currentTime;
            }
        }
        System.out.println("Best cutoff: " + bestCutoff + "; Best time: " + bestTime + "ns");
    }

    public static void main(String[] args) {
        measureSet(1000000, Type.RANDOM);
    }
}