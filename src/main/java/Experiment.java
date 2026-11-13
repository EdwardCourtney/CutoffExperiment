public class Experiment {
    public static int WARMUP = 300;
    public static int MAX_CUTOFF = 40;
    public static int TRIAL = 5;

    public static long measureEach(int[] A, int CUTOFF) {
        Sort sort = new Sort(A, CUTOFF);
        long start = System.nanoTime();
        sort.mergeSort(0, A.length-1);
        long end = System.nanoTime();
        return end - start;
    }

    public static void measureSet(int n, Type t){
        int[] original = ArrayManager.createArray(n,t);
        int[] order  = ArrayManager.createArray(MAX_CUTOFF, Type.RANDOM);

        //Warm up
        for (int i = 0; i<WARMUP; i++){
            int[] A = ArrayManager.clone(original);
            measureEach(A, order[i % MAX_CUTOFF] + 1);
        }

        //Measurement
        for (int trial = 0; trial<TRIAL; trial++){
            for (int i = 0; i<MAX_CUTOFF; i++) {
                int[] A = ArrayManager.clone(original);
                int cutoff = order[i] + 1;
                long timeNs = measureEach(A, cutoff);

                DataManager.record(n, t, cutoff, trial, timeNs);
            }
        }

    }
}