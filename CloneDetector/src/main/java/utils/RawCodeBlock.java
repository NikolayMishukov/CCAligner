package utils;

import com.github.javaparser.ast.stmt.BlockStmt;

public class RawCodeBlock {
    BlockStmt code;
    BlockInfo info;

    public RawCodeBlock(BlockStmt code, BlockInfo info){
        this.code = code;
        this.info = info;
    }

    public String getCode(){
        return code.toString();
    }

    public BlockInfo getInfo(){
        return info;
    }
}
