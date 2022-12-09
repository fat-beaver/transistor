public abstract class LogicGate extends Component {
    protected final Pin inOne;
    protected final Pin inTwo;
    protected final Pin out;
    protected final Pin supply;

    protected LogicGate() {
        inOne = new Pin(this);
        inTwo = new Pin(this);
        out = new Pin(this);
        supply = new Pin(this);
    }
    public Pin getInputOne() {
        return inOne;
    }
    public Pin getInputTwo() {
        return inTwo;
    }
    public Pin getOutput() {
        return out;
    }
    public Pin getSupply() {
        return supply;
    }
}
