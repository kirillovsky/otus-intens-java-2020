package twitter.files;

import java.io.FileWriter;
import java.io.IOException;

@FunctionalInterface
public interface WriteToFileOperation {
    void write(FileWriter writer) throws Exception;
}