package main;


import detector.CloneDetector;
import detector.ICloneDetector;
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

public class Main {
    static IFileReader reader = new FileReader();
    static IBlockSplitter splitter = new BlockSplitter();
    static ICloneDetector detector = new CloneDetector(6, 1, 0.7);


    public static void main(String[] args) {
        String sourceDirectory = args[0], resultFile = args[1];

        List<SourceFile> files = reader.Read(sourceDirectory);
        Logger.log("There are " + files.size() + " files in total");

        List<ICodeBlock> blocks = files.stream().flatMap(file ->
                splitter.Split(file).stream()
        ).toList();
        Logger.log("There are " + blocks.size() + " blocks in total");

        List<ClonePair> clones = detector.getClonePairs(blocks);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFile))) {

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
