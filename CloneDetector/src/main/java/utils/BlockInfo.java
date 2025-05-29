package utils;

public class BlockInfo {
    String file, directory;
    int startLine, endLine;

    public BlockInfo(String directory, String file, int startLine, int endLine){
        this.directory = directory;
        this.file = file;
        this.startLine = startLine;
        this.endLine = endLine;
    }

    @Override
    public String toString(){
        return String.join(",",
                directory,
                file,
                String.valueOf(startLine),
                String.valueOf(endLine)
        );
    }
}
