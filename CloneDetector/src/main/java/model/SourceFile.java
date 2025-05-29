package model;

import java.nio.file.Path;

public class SourceFile {
    Path path;
    String code;

    public SourceFile(Path path, String code){
        this.path = path;
        this.code = code;
    }

    public Path getPath(){
        return path;
    }

    public String getCode(){
        return code;
    }
}
