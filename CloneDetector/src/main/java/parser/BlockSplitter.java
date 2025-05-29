package parser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.stmt.BlockStmt;
import model.CodeBlock;
import model.ICodeBlock;
import model.SourceFile;
import utils.*;

import java.nio.file.Path;
import java.util.*;

public class BlockSplitter implements IBlockSplitter {
    static JavaParser parser = new JavaParser();
    static TokenNormalizer normalizer = new TokenNormalizer();
    static IHash hash = new HashUtil();

    @Override
    public List<ICodeBlock> Split(SourceFile file) {
        Path path = file.getPath();
        CompilationUnit cu = parser.parse(file.getCode()).getResult().orElseThrow();
        normalizer.normalize(cu);
        ArrayList<RawCodeBlock> rawBlocks = new ArrayList<>();
        cu.getTypes().forEach(type ->
            type.getMethods().forEach(method -> {
                try {
                    BlockStmt body = method.getBody().orElseThrow();
                    int begin = method.getBegin().orElseThrow().line;
                    int end = method.getEnd().orElseThrow().line;
                    BlockInfo info = new BlockInfo(
                            (path.getParent() != null) ? path.getParent().toString() : "",
                            path.getFileName().toString(),
                            begin,
                            end
                    );
                    rawBlocks.add(new RawCodeBlock(body, info));
                } catch (NoSuchElementException e) {
                    Logger.log("Exception in method processing: " + e.getMessage());
                }
            })
        );
        return rawBlocks.stream().map(rawBlock ->
                (ICodeBlock) new CodeBlock(
                        Arrays.stream(rawBlock.getCode().split("\n")).map(hash::getHash).toList(),
                        rawBlock.getInfo()
                )
        ).toList();
    }
}
