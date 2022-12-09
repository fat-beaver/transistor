public class TwoFiftySixByteCell extends Component {
    public static final int ADDRESS_SIZE = 8; //log base two of the number of values
    public static final int WORD_SIZE = 8;
    private final Pin[] address;
    private final Pin[] in;
    private final Pin write;
    private final Pin supply;
    private final Pin[] out;
    public TwoFiftySixByteCell() {
        address = new Pin[ADDRESS_SIZE];
        in = new Pin[WORD_SIZE];
        write = new Pin(this);
        supply = new Pin(this);
        out = new Pin[WORD_SIZE];

        for (int i = 0; i < ADDRESS_SIZE; i++) {
            address[i] = new Pin(this);
        }

        TwoFiftySixWordCell[] cells = new TwoFiftySixWordCell[WORD_SIZE];

        for (int i = 0; i < WORD_SIZE; i++) {
            in[i] = new Pin(this);
            out[i] = new Pin(this);

            cells[i] = new TwoFiftySixWordCell();
            subComponents.add(cells[i]);
            cells[i].getSupply().addConnection(supply);

            for (int j = 0; j < ADDRESS_SIZE; j++) {
                cells[i].getAddress(j).addConnection(address[j]);
            }
            cells[i].getIn().addConnection(in[i]);
            cells[i].getWrite().addConnection(write);
            cells[i].getOut().addConnection(out[i]);
        }

    }
    public Pin getAddress(int i) {
        return address[i];
    }
    public Pin getIn(int i) {
        return in[i];
    }
    public Pin getWrite() {
        return write;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOut(int i) {
        return out[i];
    }
}
