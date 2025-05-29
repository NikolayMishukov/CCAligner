package detector;

import model.ClonePair;
import model.ICodeBlock;

import java.util.List;

public interface ICloneDetector {
    List<ClonePair> getClonePairs(List<ICodeBlock> Source);
}
