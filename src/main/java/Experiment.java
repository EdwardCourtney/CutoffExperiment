import java.util.Random;

public class Experiment {
    public static long SEED = 20261114L;

    public static int WARMUP = 30;
    public static int BIGWARMUP = 500;
    public static int MAX_CUTOFF = 150;
    public static int TRIAL = 10;
    public static int DATASET = 30;

    private static int datasetIndex = 0;
    private static Random dataRandom = new Random(SEED);
    private static Random orderRandom = new Random(SEED ^ 0x5DEECE66DL);
    private static Random scheduleRandom = new Random(SEED ^ 0xB10CL);

    public static long measureEach(int[] A, int CUTOFF) {
        Sort sort = new Sort(A, CUTOFF);
        long start = System.nanoTime();
        sort.mergeSort(0, A.length-1);
        long end = System.nanoTime();
        // Validation is outside the timed region, including during warmup.
        for (int i = 1; i < A.length; i++) {
            if (A[i - 1] > A[i]) {
                throw new IllegalStateException("Unsorted output: cutoff=" + CUTOFF + ", index=" + i);
            }
        }
        return end - start;
    }

    public static void measureSet(int n, Type t){
        int[] original = ArrayManager.createArray(n, t, dataRandom);
        int[] order = ArrayManager.createArray(MAX_CUTOFF, Type.RANDOM, orderRandom);
        datasetIndex++;

        //Warm up
        for (int i = 0; i < Math.max(WARMUP, MAX_CUTOFF); i++){
            int[] A = ArrayManager.clone(original);
            measureEach(A, order[i % MAX_CUTOFF] + 1);
        }

        //Measurement
        for (int trial = 0; trial<TRIAL; trial++){
            ArrayManager.shuffle(order, orderRandom);
            for (int i = 0; i<MAX_CUTOFF; i++) {
                int[] A = ArrayManager.clone(original);
                int cutoff = order[i] + 1;
                long timeNs = measureEach(A, cutoff);

                DataManager.record(n, t, datasetIndex,cutoff, trial, timeNs);
            }
        }

    }

    public static void measureAll(){
        measureAll(SEED);
    }

    public static void measureAll(long seed){
        if (MAX_CUTOFF < 1 || TRIAL < 1 || DATASET < 0 || WARMUP < 0 || BIGWARMUP < 0) {
            throw new IllegalArgumentException("Invalid experiment parameters");
        }
        dataRandom = new Random(seed);
        orderRandom = new Random(seed ^ 0x5DEECE66DL);
        scheduleRandom = new Random(seed ^ 0xB10CL);
        datasetIndex = 0;
        DataManager.data.clear();
        //Big warm up
        int[] warmUpOriginal = ArrayManager.createArray(100000, Type.RANDOM, dataRandom);
        int[] warmUpOrder = ArrayManager.createArray(MAX_CUTOFF, Type.RANDOM, orderRandom);
        for (int i = 0; i < Math.max(BIGWARMUP, MAX_CUTOFF); i++){
            int[] warmUp = ArrayManager.clone(warmUpOriginal);
            measureEach(warmUp, warmUpOrder[i % MAX_CUTOFF] + 1);
        }

        // Each block contains all sizes and all Types, in seeded random order.
        for (int block = 0; block < DATASET; block++) {
            int[] sizes = {1000, 10000, 100000, 1000000};
            ArrayManager.shuffle(sizes, scheduleRandom);
            for (int size : sizes) {
                int[] types = ArrayManager.createArray(Type.values().length, Type.RANDOM, scheduleRandom);
                for (int type : types) {
                    measureSet(size, Type.values()[type]);
                }
            }
        }
    }
}
