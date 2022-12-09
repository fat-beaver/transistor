public class SixteenBitLatch extends Component {
    public static final int WORD_LENGTH = 8;
    private final Pin[] in;
    private final Pin clock;
    private final Pin clear;
    private final Pin supply;
    private final Pin[] out;
    public SixteenBitLatch() {
        in = new Pin[WORD_LENGTH];
        clock = new Pin(this);
        clear = new Pin(this);
        supply = new Pin(this);
        out = new Pin[WORD_LENGTH];

        for (int i = 0; i < WORD_LENGTH; i++) {
            in[i] = new Pin(this);
            out[i] = new Pin(this);
            EdgeFlipFlopWithClear flipFlop = new EdgeFlipFlopWithClear();
            subComponents.add(flipFlop);
            flipFlop.getSupply().addConnection(supply);
            flipFlop.getData().addConnection(in[i]);
            flipFlop.getOut().addConnection(out[i]);
            flipFlop.getClock().addConnection(clock);
            flipFlop.getClear().addConnection(clear);
        }
    }
    public Pin getIn(int i) {
        return in[i];
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
    public Pin getOut(int i) {
        return out[i];
    }
}
