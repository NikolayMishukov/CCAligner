package detector;

import com.github.javaparser.utils.Pair;
import model.ClonePair;
import model.ICodeBlock;
import model.StringHash;
import utils.HashUtil;
import utils.IHash;

import java.util.*;

import static java.util.Collections.sort;

public class CloneDetector implements ICloneDetector{

    int q, e;
    double theta;
    IHash hash = new HashUtil();

    public CloneDetector(int q, int e, double theta) {
        this.q = q;
        this.e = e;
        this.theta = theta;
    }

    @Override
    public ArrayList<ClonePair> getClonePairs(List<ICodeBlock> Source) {
        HashMap<Integer, HashSet<Integer>> candMap = new HashMap<>();
        HashMap<Integer, HashSet<Pair<Integer, Integer>>> hashSets = new HashMap<>();
        List<List<Integer>> subsequences = SubsequenceGenerator.generateSubsequences(q, q-e);
        ArrayList<Integer> blockSize = new ArrayList<>();
        int blockId = -1;
        for (ICodeBlock block : Source) {
            ++blockId;
            HashSet<Pair<Integer, Integer>> hashSubset = new HashSet<>();
            List<StringHash> strings = block.getStringHashes();
            int l = strings.size();
            blockSize.add(l);
            for (int i = 0; i <= l - q; ++i) {
                int finalIndex = i;
                for (List<Integer> subsequence : subsequences) {
                    List<StringHash> combination = subsequence.stream().map(offset -> strings.get(finalIndex + offset)).toList();
                    int k = hash.getHash(combination);
                    hashSubset.add(new Pair<>(k, i));
                    if(candMap.containsKey(k)) {
                        candMap.get(k).add(blockId);
                    }
                    else{
                        HashSet<Integer> element = new HashSet<>();
                        element.add(blockId);
                        candMap.put(k, element);
                    }
                }
            }
            hashSets.put(blockId, hashSubset);
        }

        HashSet<Pair<Integer, Integer>> candPair = new HashSet<>();
        for (Map.Entry<Integer, HashSet<Integer>> entry : candMap.entrySet()){
            ArrayList<Integer> value = new ArrayList<>(entry.getValue());
            if(value.size() >= 2) {
                sort(value);
                for (int first = 0; first < value.size(); ++first) {
                    for (int second = first + 1; second < value.size(); ++second) {
                        candPair.add(new Pair<>(value.get(first), value.get(second)));
                    }
                }
            }
        }

        ArrayList<ClonePair> clonePairs = new ArrayList<>();
        for(Pair<Integer, Integer> pair : candPair){
            HashSet<Pair<Integer, Integer>> intersection = new HashSet<>(hashSets.get(pair.a));
            intersection.retainAll(hashSets.get(pair.b));
            if(intersection.size() >= theta * subsequences.size()
                    * (Math.min(blockSize.get(pair.a), blockSize.get(pair.b)) - q + 1))
            {
                clonePairs.add(new ClonePair(Source.get(pair.a), Source.get(pair.b)));
            }
        }
        return clonePairs;
    }
}


class SubsequenceGenerator {
    public static void generateSubsequences(int sequenceLength, int targetLength, int startIndex,
                                            List<Integer> current, List<List<Integer>> result) {
        if (current.size() == targetLength) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = startIndex; i < sequenceLength; i++) {
            current.add(i);
            generateSubsequences(sequenceLength, targetLength, i + 1, current, result);
            current.removeLast();
        }
    }

    public static List<List<Integer>> generateSubsequences(int sequenceLength, int targetLength) {
        List<List<Integer>> result = new ArrayList<>();
        generateSubsequences(sequenceLength, targetLength, 0, new ArrayList<>(), result);
        return result;
    }
}