package model;

import utils.BlockInfo;

import java.util.List;

public interface ICodeBlock {
    List<StringHash> getStringHashes();

    BlockInfo getInfo();
}
