public class ByteAdder extends Component{
    public static final int WORD_SIZE = 8;
    protected final Pin[] oneInputs;
    protected final Pin[] twoInputs;
    protected final Pin[] sumOutputs;
    protected final Pin carryIn;
    protected final Pin carryOut;
    protected final Pin supply;
    protected final Adder[] adders;
    public ByteAdder() {
        oneInputs = new Pin[WORD_SIZE];
        twoInputs = new Pin[WORD_SIZE];
        sumOutputs = new Pin[WORD_SIZE];
        carryIn = new Pin(this);
        carryOut = new Pin(this);
        supply = new Pin(this);
        adders = new Adder[WORD_SIZE];

        for (int i = 0; i < WORD_SIZE; i++) {
            oneInputs[i] = new Pin(this);
            twoInputs[i] = new Pin(this);
            sumOutputs[i] = new Pin(this);
            adders[i] = new Adder();
            subComponents.add(adders[i]);

            adders[i].getSupply().addConnection(supply);
            adders[i].getInOne().addConnection(oneInputs[i]);
            adders[i].getInTwo().addConnection(twoInputs[i]);
            adders[i].getSumOut().addConnection(sumOutputs[i]);
        }
        for (int i = 0; i < WORD_SIZE - 1; i++) {
            adders[i].getCarryOut().addConnection(adders[i + 1].getCarryIn());
        }
        adders[0].getCarryIn().addConnection(carryIn);
        adders[WORD_SIZE - 1].getCarryOut().addConnection(carryOut);
    }
    public Pin getInOne(int i) {
        return oneInputs[i];
    }
    public Pin getInTwo(int i) {
        return twoInputs[i];
    }
    public Pin getCarryIn() {
        return carryIn;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getSumOut(int i) {
        return sumOutputs[i];
    }
    public Pin getCarryOut() {
        return carryOut;
    }
}
