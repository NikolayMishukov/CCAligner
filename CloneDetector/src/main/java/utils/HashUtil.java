package utils;

import model.StringHash;

import java.util.List;

public class HashUtil implements IHash {

    @Override
    public int getHash(List<StringHash> data) {
        int m = 0x5bd1e995, seed = 0;
        int r = 24;
        int h = seed ^ data.size();

        for (StringHash value : data) {
            int k = value.getValue();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return h;
    }

    @Override
    public StringHash getHash(String data) {
        byte[] bytes = data.getBytes();
        int length = bytes.length;
        int m = 0x5bd1e995, seed = 0;
        int r = 24;
        int h = seed ^ length;

        int offset = 0;
        while (length >= 4) {
            int k = ((bytes[offset] & 0xFF)) |
                    ((bytes[offset + 1] & 0xFF) << 8) |
                    ((bytes[offset + 2] & 0xFF) << 16) |
                    ((bytes[offset + 3] & 0xFF) << 24);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;

            offset += 4;
            length -= 4;
        }

        switch (length) {
            case 3:
                h ^= (bytes[offset + 2] & 0xFF) << 16;
            case 2:
                h ^= (bytes[offset + 1] & 0xFF) << 8;
            case 1:
                h ^= (bytes[offset] & 0xFF);
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return new StringHash(h);
    }

}
