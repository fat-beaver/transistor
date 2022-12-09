public class EightBitCounter extends Component {
    public static final int WORD_SIZE = 8;
    private final Pin clock;
    private final Pin clear;
    private final Pin supply;
    private final Pin[] outputs;
    public EightBitCounter() {
        clock = new Pin(this);
        clear = new Pin(this);
        supply = new Pin(this);
        outputs = new Pin[WORD_SIZE];

        EdgeFlipFlopWithClear[] flipFlops = new EdgeFlipFlopWithClear[WORD_SIZE];

        for (int i = 0; i < flipFlops.length; i++) {
            outputs[i] = new Pin(this);
            flipFlops[i] = new EdgeFlipFlopWithClear();
            subComponents.add(flipFlops[i]);
            flipFlops[i].getSupply().addConnection(supply);
            flipFlops[i].getOut().addConnection(outputs[i]);
            flipFlops[i].getOppositeOut().addConnection(flipFlops[i].getData());
            flipFlops[i].getClear().addConnection(clear);
            if (i == 0) {
                flipFlops[i].getClock().addConnection(clock);
            } else {
                flipFlops[i].getClock().addConnection(flipFlops[i - 1].getOppositeOut());
            }
        }
    }
    public Pin getClock() {
        return clock;
    }
    public Pin getClear() {
        return clear;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOutput(int i) {
        return outputs[i];
    }
}
