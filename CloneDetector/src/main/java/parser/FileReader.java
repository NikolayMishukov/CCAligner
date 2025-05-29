package parser;


import model.SourceFile;
import utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileReader implements IFileReader {

    @Override
    public List<SourceFile> Read(String sourceDirectory) {
        ArrayList<SourceFile> data = new ArrayList<>();
        Path startDir = Paths.get(sourceDirectory);

        try (Stream<Path> stream = Files.walk(startDir)) {
            stream
                    .filter(Files::isRegularFile)
                    .filter(name -> name.toString().endsWith(".java"))
                    .forEach(name -> {
                                try {
                                    String content = String.join("\n", Files.readAllLines(name));
                                    data.add(new SourceFile(startDir.relativize(name), content));
                                    Logger.log("File " + name + " successfully read");
                                } catch (java.io.IOException e) {
                                    Logger.log("Failed to read " + name + " : " + e.getMessage());
                                }
                            }
                    );
        } catch (IOException e) {
            System.err.println("Error walking through files: " + e.getMessage());
        }
        return data;
    }
}

