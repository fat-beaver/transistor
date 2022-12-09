public class Decoder extends Component {
    public static final int WORD_SIZE = 8;
    public static final int ADDRESS_SIZE = 3;
    private final Pin data;
    private final Pin[] address;
    private final Pin supply;
    private final Pin[] outputs;
    private static class FourInputANDGate extends Component {
        private final Pin inOne;
        private final Pin inTwo;
        private final Pin inThree;
        private final Pin inFour;
        private final Pin supply;
        private final Pin out;
        public FourInputANDGate() {
            inOne = new Pin(this);
            inTwo = new Pin(this);
            inThree = new Pin(this);
            inFour = new Pin(this);
            supply = new Pin(this);
            out = new Pin(this);

            ANDGate andOne = new ANDGate();
            subComponents.add(andOne);
            andOne.getSupply().addConnection(supply);
            andOne.getInputOne().addConnection(inOne);
            andOne.getInputTwo().addConnection(inTwo);

            ANDGate andTwo = new ANDGate();
            subComponents.add(andTwo);
            andTwo.getSupply().addConnection(supply);
            andTwo.getInputOne().addConnection(inThree);
            andTwo.getInputTwo().addConnection(inFour);

            ANDGate andThree = new ANDGate();
            subComponents.add(andThree);
            andThree.getSupply().addConnection(supply);
            andThree.getOutput().addConnection(out);
            andThree.getInputOne().addConnection(andOne.getOutput());
            andThree.getInputTwo().addConnection(andTwo.getOutput());
        }
    }
    public Decoder() {
        data = new Pin(this);
        address = new Pin[ADDRESS_SIZE];
        supply = new Pin(this);
        outputs = new Pin[WORD_SIZE];

        FourInputANDGate[] andGates = new FourInputANDGate[WORD_SIZE];

        for (int i = 0; i < WORD_SIZE; i++) {
            outputs[i] = new Pin(this);
            andGates[i] = new FourInputANDGate();
            subComponents.add(andGates[i]);
            andGates[i].supply.addConnection(supply);
            andGates[i].out.addConnection(outputs[i]);
            andGates[i].inOne.addConnection(data);
        }

        Inverter[] addressInverters = new Inverter[ADDRESS_SIZE];

        for (int i = 0; i < ADDRESS_SIZE; i++) {
            address[i] = new Pin(this);
            addressInverters[i] = new Inverter();
            subComponents.add(addressInverters[i]);
            addressInverters[i].getSupply().addConnection(supply);
            addressInverters[i].getIn().addConnection(address[i]);
        }

        andGates[0].inTwo.addConnection(addressInverters[0].getOut());
        andGates[0].inThree.addConnection(addressInverters[1].getOut());
        andGates[0].inFour.addConnection(addressInverters[2].getOut());

        andGates[1].inTwo.addConnection(address[0]);
        andGates[1].inThree.addConnection(addressInverters[1].getOut());
        andGates[1].inFour.addConnection(addressInverters[2].getOut());

        andGates[2].inTwo.addConnection(address[1]);
        andGates[2].inThree.addConnection(addressInverters[0].getOut());
        andGates[2].inFour.addConnection(addressInverters[2].getOut());

        andGates[3].inTwo.addConnection(address[0]);
        andGates[3].inThree.addConnection(address[1]);
        andGates[3].inFour.addConnection(addressInverters[2].getOut());

        andGates[4].inTwo.addConnection(address[2]);
        andGates[4].inThree.addConnection(addressInverters[0].getOut());
        andGates[4].inFour.addConnection(addressInverters[1].getOut());

        andGates[5].inTwo.addConnection(address[0]);
        andGates[5].inThree.addConnection(address[2]);
        andGates[5].inFour.addConnection(addressInverters[1].getOut());

        andGates[6].inTwo.addConnection(address[1]);
        andGates[6].inThree.addConnection(address[2]);
        andGates[6].inFour.addConnection(addressInverters[0].getOut());

        andGates[7].inTwo.addConnection(address[0]);
        andGates[7].inThree.addConnection(address[1]);
        andGates[7].inFour.addConnection(address[2]);
    }
    public Pin getData() {
        return data;
    }
    public Pin getAddress(int i) {
        return address[i];
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOutput(int i) {
        return outputs[i];
    }
}
