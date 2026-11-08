package serialization;
import java.io.*;

/**
 * Manages saving and loading the player's game records (the best wave and difficulty).
 */
public class RecordManager {
    /** File name for storing records. */
    private static final String FILE_NAME = "record.dat";

    /** Best wave reached by the player. */
    private static int bestWave = 0;

    /** Difficulty level at which the best wave was achieved. */
    private static int difficulty = 1;

    /**
     * Saves the current wave and difficulty if it beats the existing record.
     * @param wave the current wave
     * @param diff the current difficulty
     */
    public static void saveRecord(int wave, int diff) {
        if (wave > bestWave) {
            bestWave = wave;
            difficulty = diff;
            try (DataOutputStream out = new DataOutputStream(new FileOutputStream(FILE_NAME))) {
                out.writeInt(bestWave);
                out.writeInt(difficulty);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Loads the saved the best wave and difficulty from the file.
     */
    public static void loadRecord() {
        try (DataInputStream in = new DataInputStream(new FileInputStream(FILE_NAME))) {
            bestWave = in.readInt();
            difficulty = in.readInt();
        } catch (IOException e) {
        }
    }

    /**
     * @return the best wave achieved
     */
    public static int getBestWave() {
        return bestWave;
    }

    /**
     * @return the difficulty level of the best wave
     */
    public static int getDifficulty() {
        return difficulty;
    }

}
