package utils;

import model.StringHash;

import java.util.List;

public interface IHash {
    int getHash(List<StringHash> Data);

    StringHash getHash(String Data);
}
