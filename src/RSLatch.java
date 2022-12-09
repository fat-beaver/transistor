public class RSLatch extends Component {
    protected final Pin set;
    protected final Pin reset;
    protected final Pin supply;
    protected final Pin out;
    protected final Pin oppositeOut;
    public RSLatch() {
        set = new Pin(this);
        reset = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);
        oppositeOut = new Pin(this);

        NORGate gateOne = new NORGate();
        subComponents.add(gateOne);
        gateOne.getSupply().addConnection(supply);
        gateOne.getInputOne().addConnection(set);
        gateOne.getOutput().addConnection(oppositeOut);

        NORGate gateTwo = new NORGate();
        subComponents.add(gateTwo);
        gateTwo.getSupply().addConnection(supply);
        gateTwo.getInputOne().addConnection(reset);
        gateTwo.getInputTwo().addConnection(gateOne.getOutput());
        gateTwo.getOutput().addConnection(gateOne.getInputTwo());
        gateTwo.getOutput().addConnection(out);
    }
    public Pin getSet() {
        return set;
    }
    public Pin getReset() {
        return reset;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOut() {
        return out;
    }
    public Pin getOppositeOut() {
        return oppositeOut;
    }
}
