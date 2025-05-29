package model;

public class ClonePair {
    ICodeBlock first, second;

    public ClonePair(ICodeBlock first, ICodeBlock second){
        this.first = first;
        this.second = second;
    }

    public ICodeBlock getFirst() {
        return first;
    }

    public ICodeBlock getSecond() {
        return second;
    }
}


