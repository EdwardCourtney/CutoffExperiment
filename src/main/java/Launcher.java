public class Launcher {
    public static void main(String[] args) {
        System.out.println("Good evening");
        Experiment.measureAll();
        DataManager.export();
        System.out.println("The experiment is completed");
    }
}