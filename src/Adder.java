public class Adder extends Component {
    private final Pin inOne;
    private final Pin inTwo;
    private final Pin carryIn;
    private final Pin supply;
    private final Pin sumOut;
    private final Pin carryOut;
    private static class HalfAdder extends Component{
        private final Pin inOne;
        private final Pin inTwo;
        private final Pin supply;
        private final Pin sumOut;
        private final Pin carryOut;
        private HalfAdder() {
            inOne = new Pin(this);
            inTwo = new Pin(this);
            supply = new Pin(this);
            sumOut = new Pin(this);
            carryOut = new Pin(this);

            XORGate xorGate = new XORGate();
            subComponents.add(xorGate);
            xorGate.getSupply().addConnection(supply);
            xorGate.getInputOne().addConnection(inOne);
            xorGate.getInputTwo().addConnection(inTwo);
            xorGate.getOutput().addConnection(sumOut);

            ANDGate andGate = new ANDGate();
            subComponents.add(andGate);
            andGate.getSupply().addConnection(supply);
            andGate.getInputOne().addConnection(inOne);
            andGate.getInputTwo().addConnection(inTwo);
            andGate.getOutput().addConnection(carryOut);
        }
    }
    public Adder() {
        inOne = new Pin(this);
        inTwo = new Pin(this);
        carryIn = new Pin(this);
        supply = new Pin(this);
        sumOut = new Pin(this);
        carryOut = new Pin(this);

        HalfAdder adderOne = new HalfAdder();
        subComponents.add(adderOne);
        adderOne.supply.addConnection(supply);
        adderOne.inOne.addConnection(inOne);
        adderOne.inTwo.addConnection(inTwo);

        HalfAdder adderTwo = new HalfAdder();
        subComponents.add(adderTwo);
        adderTwo.supply.addConnection(supply);
        adderTwo.inOne.addConnection(carryIn);
        adderTwo.inTwo.addConnection(adderOne.sumOut);
        adderTwo.sumOut.addConnection(sumOut);

        ORGate orGate = new ORGate();
        subComponents.add(orGate);
        orGate.getSupply().addConnection(supply);
        orGate.getInputOne().addConnection(adderOne.carryOut);
        orGate.getInputTwo().addConnection(adderTwo.carryOut);
        orGate.getOutput().addConnection(carryOut);
    }
    public Pin getInOne() {
        return inOne;
    }
    public Pin getInTwo() {
        return inTwo;
    }
    public Pin getCarryIn() {
        return carryIn;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getSumOut() {
        return sumOut;
    }
    public Pin getCarryOut() {
        return carryOut;
    }
}
