package detector;

import com.github.javaparser.utils.Pair;
import model.ClonePair;
import model.ICodeBlock;
import model.StringHash;
import utils.HashUtil;
import utils.IHash;
import utils.Logger;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Collections.sort;

public class CloneDetector implements ICloneDetector {

    int q, e;
    double theta;
    IHash hash = new HashUtil();

    public CloneDetector(int q, int e, double theta) {
        this.q = q;
        this.e = e;
        this.theta = theta;
    }

    @Override
    public Stream<ClonePair> getClonePairs(List<ICodeBlock> Source) {
        Map<Integer, HashSet<Integer>> candMap = new HashMap<>();
        List<List<Integer>> subsequences = SubsequenceGenerator.generateSubsequences(q, q - e);
        List<Set<Pair<Integer, Integer>>> hashSets = new ArrayList<>();
        List<Integer> setSize = new ArrayList<>();
        int blockId = -1, blockCount = Source.size();
        for (ICodeBlock block : Source) {
            ++blockId;
            Set<Pair<Integer, Integer>> hashSubset = new HashSet<>();
            List<StringHash> strings = block.getStringHashes();
            int l = strings.size();
            for (int i = 0; i <= l - q; ++i) {
                int finalIndex = i;
                for (List<Integer> subsequence : subsequences) {
                    List<StringHash> combination = subsequence.stream().map(offset -> strings.get(finalIndex + offset)).toList();
                    int k = hash.getHash(combination);
                    hashSubset.add(new Pair<>(k, i));
                    if (candMap.containsKey(k)) {
                        candMap.get(k).add(blockId);
                    } else {
                        HashSet<Integer> element = new HashSet<>();
                        element.add(blockId);
                        candMap.put(k, element);
                    }
                }
            }
            hashSets.add(hashSubset);
            setSize.add(hashSubset.size());
        }
        Logger.log("End of phase 1");

        List<List<Pair<Integer, Integer>>> appearances = new ArrayList<>();
        Map<Integer, List<Integer>> sortedLists = new HashMap<>();
        for (int i = 0; i < blockCount; ++i) {
            appearances.add(new ArrayList<>());
        }
        for (Map.Entry<Integer, HashSet<Integer>> entry : candMap.entrySet()) {
            List<Integer> value = new ArrayList<>(entry.getValue());
            if (value.size() < 2)
                continue;
            int key = entry.getKey();
            sort(value);
            for (int i = 0; i < value.size(); ++i) {
                appearances.get(value.get(i)).add(new Pair<>(key, i));
            }
            sortedLists.put(key, value);
        }
        Logger.log("End of phase 2");

        Iterator<ClonePair> pairIterator = new Iterator<>() {
            private int first = 0, groupIndex = 0, indexOfSecond = 0;
            private Boolean onNew = false;
            private final HashSet<Integer> processed = new HashSet<>();

            private boolean _hasNext() {
                if (onNew)
                    return true;
                for (; first < blockCount; ++first, groupIndex = 0, processed.clear()) {
                    List<Pair<Integer, Integer>> groupList = appearances.get(first);
                    for (; groupIndex < groupList.size(); ++groupIndex, indexOfSecond = 0) {
                        Pair<Integer, Integer> group = groupList.get(groupIndex);
                        for (indexOfSecond = Math.max(indexOfSecond + 1, group.b + 1);
                             indexOfSecond < sortedLists.get(group.a).size();
                             ++indexOfSecond) {
                            int second = sortedLists.get(group.a).get(indexOfSecond);
                            if (processed.contains(second))
                                continue;
                            processed.add(second);
                            Set<Pair<Integer, Integer>> intersection = new HashSet<>(hashSets.get(first));
                            intersection.retainAll(hashSets.get(second));
                            if (intersection.size() >= theta
                                    * (Math.min(setSize.get(first), setSize.get(second)))) {
                                onNew = true;
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean hasNext() {
                return _hasNext();
            }

            @Override
            public ClonePair next() {
                boolean has = onNew || _hasNext();
                assert has;
                onNew = false;
                Pair<Integer, Integer> group = appearances.get(first).get(groupIndex);
                return new ClonePair(Source.get(first), Source.get(sortedLists.get(group.a).get(indexOfSecond)));
            }
        };

        return StreamSupport.stream(
                ((Iterable<ClonePair>) () -> pairIterator).spliterator(),
                false
        );
    }

    static class SubsequenceGenerator {
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
}