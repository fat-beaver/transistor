public class MemoryByte extends Component {
    public static final int WORD_SIZE = 8;
    public static final int ADDRESS_SIZE = 3;
    private final Pin[] address;
    private final Pin in;
    private final Pin write;
    private final Pin supply;
    private final Pin out;
    public MemoryByte() {
        address = new Pin[ADDRESS_SIZE];
        in = new Pin(this);
        write = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);

        Decoder decoder = new Decoder();
        subComponents.add(decoder);
        decoder.getSupply().addConnection(supply);
        decoder.getData().addConnection(write);

        Selector selector = new Selector();
        subComponents.add(selector);
        selector.getSupply().addConnection(supply);
        selector.getOutput().addConnection(out);

        for (int i = 0; i < ADDRESS_SIZE; i++) {
            address[i] = new Pin(this);
            address[i].addConnection(decoder.getAddress(i));
            address[i].addConnection(selector.getAddress(i));
        }

        ClockedFlipFlop[] flipFlops = new ClockedFlipFlop[WORD_SIZE];

        for (int i = 0; i < WORD_SIZE; i++) {
            flipFlops[i] = new ClockedFlipFlop();
            subComponents.add(flipFlops[i]);
            flipFlops[i].getSupply().addConnection(supply);
            flipFlops[i].getIn().addConnection(in);
            flipFlops[i].getWrite().addConnection(decoder.getOutput(i));
            flipFlops[i].getOut().addConnection(selector.getData(i));
        }
    }
    public Pin getAddress(int i) {
        return address[i];
    }
    public Pin getIn() {
        return in;
    }
    public Pin getWrite() {
        return write;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOut() {
        return out;
    }
}
