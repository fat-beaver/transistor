public class ClockedFlipFlop extends Component {
    private final Pin in;
    private final Pin write;
    private final Pin supply;
    private final Pin out;
    public ClockedFlipFlop() {
        in = new Pin(this);
        write = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);

        Inverter inverter = new Inverter();
        subComponents.add(inverter);
        inverter.getSupply().addWithoutCheck(supply);
        inverter.getIn().addConnection(in);

        ANDGate andGateOne = new ANDGate();
        subComponents.add(andGateOne);
        andGateOne.getSupply().addWithoutCheck(supply);
        andGateOne.getInputOne().addConnection(write);
        andGateOne.getInputTwo().addConnection(inverter.getOut());

        ANDGate andGateTwo = new ANDGate();
        subComponents.add(andGateTwo);
        andGateTwo.getSupply().addWithoutCheck(supply);
        andGateTwo.getInputOne().addConnection(write);
        andGateTwo.getInputTwo().addConnection(in);

        RSLatch latch = new RSLatch();
        subComponents.add(latch);
        latch.getSupply().addWithoutCheck(supply);
        latch.getReset().addConnection(andGateOne.getOutput());
        latch.getSet().addConnection(andGateTwo.getOutput());
        latch.getOut().addConnection(out);


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
