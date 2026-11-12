public class Main {
    public static void print(int[] A){
        for (int i = 0; i<A.length; i++){
            System.out.print(A[i] + " ");
        }
        System.out.println();
    }
    public static void main(String[] args){
        int[] A = {0,1,2,3,4,5};
        TestMaker.shuffle(A);
        print(A);
    }
}
