public class SixteenBitCell extends Component{
    public static final int NUMBER_OF_BITS = 16;
    public static final int ADDRESS_SIZE = 4; //log base two of the number of values
    private final Pin[] address;
    private final Pin in;
    private final Pin write;
    private final Pin supply;
    private final Pin out;
    public SixteenBitCell() {
        address = new Pin[ADDRESS_SIZE];
        in = new Pin(this);
        write = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);

        SixteenBitAddresser decoder = new SixteenBitAddresser();
        subComponents.add(decoder);
        decoder.getSupply().addConnection(supply);

        SixteenBitAddresser selector = new SixteenBitAddresser();
        subComponents.add(selector);
        selector.getSupply().addConnection(supply);

        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            decoder.getIn(i).addConnection(write);
            selector.getOut(i).addConnection(out);
        }



        for (int i = 0; i < ADDRESS_SIZE; i++) {
            address[i] = new Pin(this);
            address[i].addConnection(decoder.getAddress(i));
            address[i].addConnection(selector.getAddress(i));
        }

        ClockedFlipFlop[] flipFlops = new ClockedFlipFlop[NUMBER_OF_BITS];

        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            flipFlops[i] = new ClockedFlipFlop();
            subComponents.add(flipFlops[i]);
            flipFlops[i].getSupply().addConnection(supply);
            flipFlops[i].getIn().addConnection(in);
            flipFlops[i].getWrite().addConnection(decoder.getOut(i));
            flipFlops[i].getOut().addConnection(selector.getIn(i));
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
