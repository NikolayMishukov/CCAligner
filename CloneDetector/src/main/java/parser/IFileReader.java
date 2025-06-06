package parser;


import model.SourceFile;

import java.util.stream.Stream;

public interface IFileReader {
    Stream<SourceFile> Read(String sourceDirectory);
}
