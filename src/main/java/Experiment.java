public class Experiment {
    public static int WARMUP = 30;
    public static int BIGWARMUP = 500;
    public static int MAX_CUTOFF = 150;
    public static int TRIAL = 3;
    public static int DATASET = 20;

    private static int datasetIndex = 0;
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
        datasetIndex++;

        //Warm up
        for (int i = 0; i<WARMUP; i++){
            int[] A = ArrayManager.clone(original);
            measureEach(A, order[i % MAX_CUTOFF] + 1);
        }

        //Measurement
        for (int trial = 0; trial<TRIAL; trial++){
            ArrayManager.shuffle(order);
            for (int i = 0; i<MAX_CUTOFF; i++) {
                int[] A = ArrayManager.clone(original);
                int cutoff = order[i] + 1;
                long timeNs = measureEach(A, cutoff);

                DataManager.record(n, t, datasetIndex,cutoff, trial, timeNs);
            }
        }

    }

    public static void measureAll(){
        //Big warm up
        int[] warmUpOriginal = ArrayManager.createArray(100000,Type.RANDOM);
        int[] warmUpOrder  = ArrayManager.createArray(MAX_CUTOFF, Type.RANDOM);
        for (int i = 0; i<BIGWARMUP; i++){
            int[] warmUp = ArrayManager.clone(warmUpOriginal);
            measureEach(warmUp, warmUpOrder[i % MAX_CUTOFF] + 1);
        }

        //Experiment

        //Random
        for (int i = 0; i<DATASET; i++){
            measureSet(1000, Type.RANDOM);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(10000, Type.RANDOM);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(100000, Type.RANDOM);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(1000000, Type.RANDOM);
        }

        //Nearly
        for (int i = 0; i<DATASET; i++){
            measureSet(1000, Type.NEARLY);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(10000, Type.NEARLY);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(100000, Type.NEARLY);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(1000000, Type.NEARLY);
        }

        //Reversed
        for (int i = 0; i<DATASET; i++){
            measureSet(1000, Type.REVERSED);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(10000, Type.REVERSED);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(100000, Type.REVERSED);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(1000000, Type.REVERSED);
        }

        //Sorted
        for (int i = 0; i<DATASET; i++){
            measureSet(1000, Type.SORTED);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(10000, Type.SORTED);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(100000, Type.SORTED);
        }
        for (int i = 0; i<DATASET; i++){
            measureSet(1000000, Type.SORTED);
        }
    }
}
