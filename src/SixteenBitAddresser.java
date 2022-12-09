public class SixteenBitAddresser extends Component {
    public static final int NUMBER_OF_BITS = 16;
    public static final int ADDRESS_SIZE = 4; //log base two of the number of values
    private final Pin[] in;
    private final Pin[] address;
    private final Pin supply;
    private final Pin[] outputs;
    public SixteenBitAddresser() {
        in = new Pin[NUMBER_OF_BITS];
        address = new Pin[ADDRESS_SIZE];
        supply = new Pin(this);
        outputs = new Pin[NUMBER_OF_BITS];

        FourInputANDGate[] fourInputAndGates = new FourInputANDGate[NUMBER_OF_BITS];
        ANDGate[] andGates = new ANDGate[NUMBER_OF_BITS];

        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            outputs[i] = new Pin(this);
            in[i] = new Pin(this);
            fourInputAndGates[i] = new FourInputANDGate();
            subComponents.add(fourInputAndGates[i]);
            fourInputAndGates[i].getSupply().addWithoutCheck(supply);
            andGates[i] = new ANDGate();
            subComponents.add(andGates[i]);
            andGates[i].getSupply().addWithoutCheck(supply);
            andGates[i].getInputOne().addConnection(in[i]);
            andGates[i].getInputTwo().addConnection(fourInputAndGates[i].getOut());
            andGates[i].getOutput().addConnection(outputs[i]);
        }

        Inverter[] addressInverters = new Inverter[ADDRESS_SIZE];

        for (int i = 0; i < ADDRESS_SIZE; i++) {
            address[i] = new Pin(this);
            addressInverters[i] = new Inverter();
            subComponents.add(addressInverters[i]);
            addressInverters[i].getSupply().addWithoutCheck(supply);
            addressInverters[i].getIn().addConnection(address[i]);
        }

        for (int i = 0; i < NUMBER_OF_BITS; i++) {
            int modifiedI = i;
            for (int j = ADDRESS_SIZE - 1; j >= 0; j--) {
                if (modifiedI >= Math.pow(2, j)) {
                    fourInputAndGates[i].getIn(j).addConnection(address[j]);
                    modifiedI -= (int) (Math.pow(2, j));
                } else {
                    fourInputAndGates[i].getIn(j).addConnection(addressInverters[j].getOut());
                }
            }
        }

    }
    public Pin getIn(int i) {
        return in[i];
    }
    public Pin getAddress(int i) {
        return address[i];
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOut(int i) {
        return outputs[i];
    }
}
