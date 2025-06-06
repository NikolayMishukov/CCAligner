package parser;


import model.SourceFile;
import utils.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FileReader implements IFileReader {

    @Override
    public Stream<SourceFile> Read(String sourceDirectory) {
        Path startDir = Paths.get(sourceDirectory);

        try (Stream<Path> stream = Files.walk(startDir)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(name -> name.toString().endsWith(".java"))
                    .toList().stream()
                    .flatMap(name -> {
                                try {
                                    String content = String.join("\n", Files.readAllLines(name));
                                    return Stream.of(new SourceFile(startDir.relativize(name), content));
                                } catch (java.io.IOException e) {
                                    Logger.log("Failed to read " + name + " : " + e.getMessage());
                                    return Stream.empty();
                                }
                            }
                    );
        } catch (IOException e) {
            Logger.log("Error walking through files: " + e.getMessage());
            return Stream.empty();
        }
    }
}

