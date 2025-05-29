package parser;


import model.SourceFile;

import java.util.List;

public interface IFileReader {
    List<SourceFile> Read(String sourceDirectory);
}
