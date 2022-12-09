public class TwoFiftySixBitCell extends Component {
    public static final int NUMBER_OF_CELLS = 16;
    public static final int ADDRESS_SIZE = 8; //log base two number of cells multiplied by bits per cell (16 * 16)
    public static final int ADDRESS_PARTS = 2;
    public static final int WORD_SIZE = 1;
    private final Pin[] address;
    private final Pin in;
    private final Pin write;
    private final Pin supply;
    private final Pin out;
    public TwoFiftySixBitCell() {
        address = new Pin[ADDRESS_SIZE];
        in = new Pin(this);
        write = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);

        SixteenBitAddresser decoder = new SixteenBitAddresser();
        subComponents.add(decoder);
        decoder.getSupply().addWithoutCheck(supply);

        SixteenBitAddresser selector = new SixteenBitAddresser();
        subComponents.add(selector);
        selector.getSupply().addWithoutCheck(supply);

        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            decoder.getIn(i).addConnection(write);
            selector.getOut(i).addConnection(out);
        }

        for (int i = 0; i < ADDRESS_SIZE / ADDRESS_PARTS; i++) {
            address[i] = new Pin(this);
            address[i].addConnection(decoder.getAddress(i));
            address[i].addConnection(selector.getAddress(i));
        }

        for (int i = ADDRESS_SIZE / ADDRESS_PARTS; i < ADDRESS_SIZE; i++) {
            address[i] = new Pin(this);
        }

        SixteenBitCell[] cells = new SixteenBitCell[NUMBER_OF_CELLS];

        for (int i = 0; i < NUMBER_OF_CELLS; i++) {
            cells[i] = new SixteenBitCell();
            subComponents.add(cells[i]);
            cells[i].getSupply().addWithoutCheck(supply);
            cells[i].getIn().addConnection(in);
            cells[i].getWrite().addConnection(decoder.getOut(i));
            cells[i].getOut().addConnection(selector.getIn(i));

            for (int j = ADDRESS_SIZE / ADDRESS_PARTS; j < ADDRESS_SIZE; j++) {
                cells[i].getAddress(j - ADDRESS_SIZE / ADDRESS_PARTS).addConnection(address[j]);
            }
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
