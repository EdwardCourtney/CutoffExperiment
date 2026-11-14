import java.util.Random;
public class ArrayManager {
    public static void shuffle(int[] A){
        shuffle(A, new Random());
    }

    public static void shuffle(int[] A, Random rand){
        for (int i = 0; i<A.length; i++){
            int j = rand.nextInt(i+1);

            int temp = A[i];
            A[i] = A[j];
            A[j] = temp;
        }
    }

    public static int[] createArray(int n, Type t){
        return createArray(n, t, new Random());
    }

    public static int[] createArray(int n, Type t, Random rand){
        int[] A = new int[n];

        return switch (t) {
            case RANDOM -> {
                for (int i = 0; i < n; i++) {
                    A[i] = i;
                }
                shuffle(A, rand);
                yield A;
            }

            case SORTED -> {
                for (int i = 0; i < n; i++) {
                    A[i] = i;
                }
                yield A;
            }

            case REVERSED -> {
                int j = 0;
                for (int i = n-1; i>=0; i--){
                    A[j++] = i;
                }
                yield A;
            }

            case NEARLY -> {
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
                yield A;
            }
        };
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
