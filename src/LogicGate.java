public abstract class LogicGate extends Component{
    protected final Pin inOne;
    protected final Pin inTwo;
    protected final Pin out;
    protected final Pin supply;

    protected LogicGate() {
        inOne = new Pin();
        inTwo = new Pin();
        out = new Pin();
        supply = new Pin();
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
