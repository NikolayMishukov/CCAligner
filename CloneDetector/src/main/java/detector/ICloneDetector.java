package detector;

import model.ClonePair;
import model.ICodeBlock;

import java.util.List;
import java.util.stream.Stream;

public interface ICloneDetector {
    Stream<ClonePair> getClonePairs(List<ICodeBlock> Source);
}
