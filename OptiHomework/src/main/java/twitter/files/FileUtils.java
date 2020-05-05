package twitter.files;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static void writeToFile(File file, WriteToFileOperation doWrite, Runnable doFinally) {
        if (file.canWrite()) {
            try (FileWriter writer = new FileWriter(file)) {
                doWrite.write(writer);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                doFinally.run();
            }
        }
    }

    public static File createFile(String path) {
        File resultFile = new File(path);
        try {
            resultFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return resultFile;
    }
}

