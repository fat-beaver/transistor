public class DualSourceMemory extends Component {
    private final Pin[] wordOne;
    private final Pin[] addressOne;
    private final Pin writeOne;
    private final Pin[] wordTwo;
    private final Pin[] addressTwo;
    private final Pin writeTwo;
    private final Pin select;
    private final Pin supply;
    private final Pin[] wordOut;
    private final Pin[] addressOut;
    public DualSourceMemory() {
        wordOne = new Pin[TwoFiftySixWordCell.WORD_SIZE];
        addressOne = new Pin[TwoFiftySixWordCell.ADDRESS_SIZE];
        writeOne = new Pin(this);
        wordTwo = new Pin[TwoFiftySixWordCell.WORD_SIZE];
        addressTwo = new Pin[TwoFiftySixWordCell.ADDRESS_SIZE];
        writeTwo = new Pin(this);
        select = new Pin(this);
        supply = new Pin(this);
        wordOut = new Pin[TwoFiftySixWordCell.WORD_SIZE];
        addressOut = new Pin[TwoFiftySixWordCell.ADDRESS_SIZE];

        TwoFiftySixWordCell cell = new TwoFiftySixWordCell();
        subComponents.add(cell);

        for (int i = 0; i < TwoFiftySixWordCell.WORD_SIZE; i++) {
            wordOne[i] = new Pin(this);
            wordTwo[i] = new Pin(this);
            wordOut[i] = new Pin(this);

            TwoToOneSelector selector = new TwoToOneSelector();
            subComponents.add(selector);
            selector.getSupply().addWithoutCheck(supply);
            selector.getSelect().addConnection(select);
            selector.getInOne().addConnection(wordOne[i]);
            selector.getInTwo().addConnection(wordTwo[i]);
            selector.getOut().addConnection(cell.getIn(i));

            cell.getOut(i).addConnection(wordOut[i]);
        }

        for (int i = 0; i < TwoFiftySixWordCell.ADDRESS_SIZE; i++) {
            addressOne[i] = new Pin(this);
            addressTwo[i] = new Pin(this);
            addressOut[i] = new Pin(this);

            TwoToOneSelector selector = new TwoToOneSelector();
            subComponents.add(selector);
            selector.getSupply().addWithoutCheck(supply);
            selector.getSelect().addConnection(select);
            selector.getInOne().addConnection(addressOne[i]);
            selector.getInTwo().addConnection(addressTwo[i]);
            selector.getOut().addConnection(cell.getAddress(i));
            selector.getOut().addConnection(addressOut[i]);
        }

        TwoToOneSelector writeSelector = new TwoToOneSelector();
        subComponents.add(writeSelector);
        writeSelector.getSupply().addWithoutCheck(supply);
        writeSelector.getSelect().addConnection(select);
        writeSelector.getInOne().addConnection(writeOne);
        writeSelector.getInTwo().addConnection(writeTwo);
        writeSelector.getOut().addConnection(cell.getWrite());

        cell.getSupply().addWithoutCheck(supply); //speeds up running by ~25%
    }
    public Pin getWordOne(int i) {
        return wordOne[i];
    }
    public Pin getAddressOne(int i) {
        return addressOne[i];
    }
    public Pin getWriteOne() {
        return writeOne;
    }
    public Pin getWordTwo(int i) {
        return wordTwo[i];
    }
    public Pin getAddressTwo(int i) {
        return addressTwo[i];
    }
    public Pin getWriteTwo() {
        return writeTwo;
    }
    public Pin getSelect() {
        return select;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getWordOut(int i) {
        return wordOut[i];
    }
    public Pin getAddressOut(int i) {
        return addressOut[i];
    }
}
