public class EightInputANDGate extends Component {
    public final static int NUMBER_OF_INPUTS = 8;
    private final Pin[] inputs;
    private final Pin supply;
    private final Pin out;
    public EightInputANDGate() {
        inputs = new Pin[NUMBER_OF_INPUTS];
        supply = new Pin(this);
        out = new Pin(this);

        for (int i = 0; i < NUMBER_OF_INPUTS; i++) {
            inputs[i] = new Pin(this);
        }

        FourInputANDGate[] mainGates = new FourInputANDGate[NUMBER_OF_INPUTS / FourInputANDGate.NUMBER_OF_INPUTS];

        for (int i = 0; i < mainGates.length; i++) {
            mainGates[i] = new FourInputANDGate();
            subComponents.add(mainGates[i]);
            mainGates[i].getSupply().addWithoutCheck(supply);
            for (int j = 0; j < FourInputANDGate.NUMBER_OF_INPUTS; j++) {
                mainGates[i].getIn(j).addConnection(inputs[i * FourInputANDGate.NUMBER_OF_INPUTS + j]);
            }
        }

        ANDGate finalAnd = new ANDGate();
        subComponents.add(finalAnd);
        finalAnd.getSupply().addWithoutCheck(supply);
        finalAnd.getOutput().addConnection(out);
        finalAnd.getInputOne().addConnection(mainGates[0].getOut());
        finalAnd.getInputTwo().addConnection(mainGates[1].getOut());
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
