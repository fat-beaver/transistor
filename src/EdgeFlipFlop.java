public class EdgeFlipFlop extends Component {
    protected final Pin clock;
    protected final Pin data;
    protected final Pin supply;
    protected final Pin out;
    protected final Pin oppositeOut;
    public EdgeFlipFlop() {
        clock = new Pin(this);
        data = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);
        oppositeOut = new Pin(this);

        ANDGate andOne = new ANDGate();
        subComponents.add(andOne);
        andOne.getSupply().addConnection(supply);
        andOne.getInputOne().addConnection(clock);

        ANDGate andTwo = new ANDGate();
        subComponents.add(andTwo);
        andTwo.getSupply().addConnection(supply);
        andTwo.getInputOne().addConnection(clock);

        RSLatch latchOne = new RSLatch();
        subComponents.add(latchOne);
        latchOne.getSupply().addConnection(supply);
        latchOne.getOut().addConnection(out);
        latchOne.getOppositeOut().addConnection(oppositeOut);
        latchOne.getReset().addConnection(andOne.getOutput());
        latchOne.getSet().addConnection(andTwo.getOutput());

        Inverter clockInverter = new Inverter();
        subComponents.add(clockInverter);
        clockInverter.getSupply().addConnection(supply);
        clockInverter.getIn().addConnection(clock);

        ANDGate andThree = new ANDGate();
        subComponents.add(andThree);
        andThree.getSupply().addConnection(supply);
        andThree.getInputOne().addConnection(clockInverter.getOut());
        andThree.getInputTwo().addConnection(data);

        Inverter dataInverter = new Inverter();
        subComponents.add(dataInverter);
        dataInverter.getSupply().addConnection(supply);

        ANDGate andFour = new ANDGate();
        subComponents.add(andFour);
        andFour.getSupply().addConnection(supply);
        andFour.getInputOne().addConnection(clockInverter.getOut());
        andFour.getInputTwo().addConnection(dataInverter.getOut());

        RSLatch latchTwo = new RSLatch();
        subComponents.add(latchTwo);
        latchTwo.getSupply().addConnection(supply);
        latchTwo.getOut().addConnection(andOne.getInputTwo());
        latchTwo.getOppositeOut().addConnection(andTwo.getInputTwo());
        latchTwo.getReset().addConnection(andThree.getOutput());
        latchTwo.getSet().addConnection(andFour.getOutput());
    }
    public Pin getClock() {
        return clock;
    }
    public Pin getData() {
        return data;
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
