import java.io.*;
import java.util.ArrayList;

public class DataManager {
    public static ArrayList<Long[]> data = new ArrayList<>();
    public static long datasetIndex = 0;

    public static void export(){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("result.csv"))) {
            writer.write("size,id_type,dataset,cutoff,trial,time_ns");
            writer.newLine();
            for (int row = 0; row < data.size(); row++){
                Long[] observation = data.get(row);
                for (int column = 0; column < 6; column++){
                    writer.write(String.valueOf(observation[column]));
                    if (column < 5){
                        writer.write(",");
                    }
                }
                writer.newLine();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    //idType: 1 = RANDOM, 2 = NEARLY, 3 = REVERSED, 4 = SORTED

    public static void record(int size, Type type, int cutoff, int trial, long timeNs){
        long idType = 0;
        switch (type){
            case RANDOM -> idType = 1;
            case NEARLY -> idType = 2;
            case REVERSED -> idType = 3;
            case SORTED -> idType = 4;
        }

        Long[] observation = new Long[6];
        observation[0] = (long) size;
        observation[1] = idType;
        observation[2] = datasetIndex;
        observation[3] = (long) cutoff;
        observation[4] = (long) trial;
        observation[5] = timeNs;

        datasetIndex++;
        data.add(observation);
    }
}
