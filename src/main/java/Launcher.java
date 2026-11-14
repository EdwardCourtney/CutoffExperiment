public class Launcher {
    public static void main(String[] args) {
        System.out.println("Good evening");
        long seed = args.length == 0 ? Experiment.SEED : Long.parseLong(args[0]);
        System.out.println("Seed: " + seed);
        Experiment.measureAll(seed);
        DataManager.export();
        System.out.println("The experiment is completed");
    }
}
