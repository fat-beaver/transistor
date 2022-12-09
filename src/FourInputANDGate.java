public class FourInputANDGate extends Component {
    public final static int NUMBER_OF_INPUTS = 4;
    private final Pin[] inputs;
    private final Pin supply;
    private final Pin out;
    public FourInputANDGate() {
        inputs = new Pin[NUMBER_OF_INPUTS];
        supply = new Pin(this);
        out = new Pin(this);

        for (int i = 0; i < NUMBER_OF_INPUTS; i++) {
            inputs[i] = new Pin(this);
        }

        ANDGate andOne = new ANDGate();
        subComponents.add(andOne);
        andOne.getSupply().addWithoutCheck(supply);
        andOne.getInputOne().addConnection(inputs[0]);
        andOne.getInputTwo().addConnection(inputs[1]);

        ANDGate andTwo = new ANDGate();
        subComponents.add(andTwo);
        andTwo.getSupply().addWithoutCheck(supply);
        andTwo.getInputOne().addConnection(inputs[2]);
        andTwo.getInputTwo().addConnection(inputs[3]);

        ANDGate andThree = new ANDGate();
        subComponents.add(andThree);
        andThree.getSupply().addWithoutCheck(supply);
        andThree.getOutput().addConnection(out);
        andThree.getInputOne().addConnection(andOne.getOutput());
        andThree.getInputTwo().addConnection(andTwo.getOutput());
    }
    public Pin getIn(int i) {
        return inputs[i];
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOut() {
        return out;
    }
}
