public class Mergesort {
    public int CUTOFF;
    public int[] A = null;

    public Mergesort(int[] A, int CUTOFF){
        this.A = A;
        this.CUTOFF = CUTOFF;
    }

    //Merge Sort

    public void merge(int lo, int mid, int hi){

        int[] aux = new int[hi-lo+1];

        int i = lo;
        int j = mid+1;
        int k = 0;

        while (i <= mid && j <=hi){
            if (A[i] <= A[j]){
                aux[k++] = A[i++];
            } else{
                aux[k++] = A[j++];
            }

        }

        while (i<=mid){
            aux[k++] = A[i++];
        }
        while (j<=hi){
            aux[k++] = A[j++];
        }

        for (int t = 0; t<aux.length; t++){
            A[lo+t] = aux[t];
        }
    }

    public void mergeSort(int lo, int hi){
        if (lo >= hi){
            return;
        }

        if (hi - lo + 1 <= CUTOFF){
            insertionSort(lo, hi);
            return;
        }

        int mid = lo + (hi-lo)/2;

        mergeSort(lo, mid);
        mergeSort(mid+1, hi);

        merge(lo, mid, hi);
    }

    //Insertion Sort

    public void swap(int[] A, int i, int j){
        int temp = A[i];
        A[i] = A[j];
        A[j] = temp;
    }

    public void insertionSort(int lo, int hi){
        if (A.length == 1){
            return;
        }

        for (int i = lo+1; i<=hi; i++){
            int j = i;
            while (j-1>=lo && A[j] < A[j-1]){
                swap(A, j, j-1);
                j--;
            }
        }
    }
}
