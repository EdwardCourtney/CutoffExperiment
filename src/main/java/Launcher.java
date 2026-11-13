public class Launcher {
    public static void main(String[] args) {
        Experiment.measureAll();
        DataManager.export();
    }
}