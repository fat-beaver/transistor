public class TwoToOneSelector extends Component {
    private final Pin inOne;
    private final Pin inTwo;
    private final Pin select;
    private final Pin supply;
    private final Pin out;
    public TwoToOneSelector() {
        inOne = new Pin(this);
        inTwo = new Pin(this);
        select = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);

        ANDGate andOne = new ANDGate();
        subComponents.add(andOne);
        andOne.getSupply().addWithoutCheck(supply);

        ANDGate andTwo = new ANDGate();
        subComponents.add(andTwo);
        andTwo.getSupply().addWithoutCheck(supply);

        Inverter inverter = new Inverter();
        subComponents.add(inverter);
        inverter.getSupply().addWithoutCheck(supply);

        inOne.addConnection(andOne.getInputOne());
        inTwo.addConnection(andTwo.getInputOne());

        select.addConnection(inverter.getIn());
        inverter.getOut().addConnection(andOne.getInputTwo());
        select.addConnection(andTwo.getInputTwo());

        out.addConnection(andOne.getOutput());
        out.addConnection(andTwo.getOutput());
    }
    public Pin getInOne() {
        return inOne;
    }
    public Pin getInTwo() {
        return inTwo;
    }
    public Pin getSelect() {
        return select;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getOut() {
        return out;
    }
}
