package serializationTest;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import serialization.RecordManager;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class RecordManagerTest {

    private static final String FILE_NAME = "record.dat";

    @BeforeEach
    void setUp() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @AfterEach
    void end() {
        File file = new File(FILE_NAME);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testSaveAndLoadRecord() {
        RecordManager.saveRecord(10, 2);

        RecordManager.loadRecord();

        assertEquals(10, RecordManager.getBestWave());
        assertEquals(2, RecordManager.getDifficulty());
    }

    @Test
    void testSaveRecordOnlyIfBetter() {
        RecordManager.saveRecord(20, 3);

        RecordManager.saveRecord(3, 3);

        RecordManager.loadRecord();

        assertEquals(20, RecordManager.getBestWave());
        assertEquals(3, RecordManager.getDifficulty());
    }
}
