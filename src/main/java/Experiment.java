public class Experiment {
    public int WARMUP = 300;
    public static void main(String[] args){

        // n = 25, RANDOM

        Sort sort = new Sort();
        int[] A = Helper.createArray(1000, Type.REVERSED);

        long current;
        long ans = Long.MAX_VALUE;
        long ansCF = 0;
        for (int i = 5; i<=150; i++){
            int[] B = Helper.clone(A);
            current = sort.measure(B, i);
            System.out.println("Cutoff: " + i + "; Time elapsed: " + current);

            if (current < ans){
                ansCF = i;
                ans = current;
            }
        }
        System.out.println("Best Cutoff: " + ansCF + "; Time elapsed: " + ans);
    }
}
