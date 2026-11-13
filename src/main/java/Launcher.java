public class Launcher {
    public static void main(String[] args) {
        Experiment.measureSet(100000, Type.RANDOM);
        Experiment.measureSet(10000, Type.RANDOM);
        DataManager.export();
    }
}