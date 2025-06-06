package detector;


import model.ClonePair;
import model.ICodeBlock;
import model.SourceFile;
import parser.BlockSplitter;
import parser.FileReader;
import parser.IBlockSplitter;
import parser.IFileReader;
import utils.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Main {
    static IFileReader reader = new FileReader();
    static IBlockSplitter splitter = new BlockSplitter();
    static ICloneDetector detector = new CloneDetector(6, 1, 0.7);


    public static void main(String[] args) {
        String sourceDirectory = args[0], resultFile = args[1];
        boolean append = args.length > 2 && Objects.equals(args[2], "-ap");

        Stream<SourceFile> files = reader.Read(sourceDirectory);

        List<ICodeBlock> blocks = files.flatMap(file ->
                splitter.Split(file).stream()
        ).toList();
        Logger.log("There are " + blocks.size() + " blocks in total");

        Stream<ClonePair> clones = detector.getClonePairs(blocks);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile, append))) {

            clones.forEach(clone -> {
                        try {
                            writer.write(String.join(",",
                                    clone.getFirst().getInfo().toString(),
                                    clone.getSecond().getInfo().toString()
                            ));
                            writer.newLine();
                        } catch (IOException e) {
                            Logger.log("Error while writing results: " + e.getMessage());
                        }
                    }
            );

            Logger.log("Results written successfully");
        } catch (IOException e) {
            Logger.log("Failed to write results: " + e.getMessage());
        }
    }
}
