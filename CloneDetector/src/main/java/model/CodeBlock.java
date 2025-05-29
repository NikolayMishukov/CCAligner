package model;

import utils.BlockInfo;

import java.util.List;

public class CodeBlock implements ICodeBlock{
    List<StringHash> lines;
    BlockInfo info;

    public CodeBlock(List<StringHash> lines, BlockInfo info){
        this.lines = lines;
        this.info = info;
    }

    @Override
    public List<StringHash> getStringHashes() {
        return lines;
    }

    @Override
    public BlockInfo getInfo() {
        return info;
    }
}
