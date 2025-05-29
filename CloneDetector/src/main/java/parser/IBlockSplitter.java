package parser;

import model.ICodeBlock;
import model.SourceFile;

import java.util.List;

public interface IBlockSplitter {
    List<ICodeBlock> Split(SourceFile file);
}
