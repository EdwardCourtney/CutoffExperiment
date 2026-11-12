import java.util.Random;
public class Helper {
    public static void shuffle(int[] A){
        Random rand = new Random();
        for (int i = 0; i<A.length; i++){
            int j = rand.nextInt(i+1);

            int temp = A[i];
            A[i] = A[j];
            A[j] = temp;
        }
    }

    public static int[] createArray(int n, Type t){
        int[] A = new int[n];

        switch (t) {
            case RANDOM -> {
                for (int i = 0; i < n; i++) {
                    A[i] = i;
                }
                shuffle(A);
                return A;
            }

            case SORTED -> {
                for (int i = 0; i < n; i++) {
                    A[i] = i;
                }
                return A;
            }

            case REVERSED -> {
                int j = 0;
                for (int i = n-1; i>=0; i--){
                    A[j++] = i;
                }
                return A;
            }

            case NEARLY -> {
                Random rand = new Random();
                for (int i = 0; i < n; i++) {
                    A[i] = i;
                }

                for (int i = 0; i<n/10; i++){
                    int a = rand.nextInt(n);
                    int b = rand.nextInt(n);

                    int temp = A[a];
                    A[a] = A[b];
                    A[b] = temp;
                }
                return A;
            }
        }
        return null;
    }

    public static int[] clone(int[] A){
        int[] B = new int[A.length];
        for (int i = 0; i<A.length; i++){
            B[i] = A[i];
        }
        return B;
    }

    public static void print(int[] A){
        for (int i = 0; i<A.length; i++){
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
}
