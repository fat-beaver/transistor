public class EdgeFlipFlopWithClear extends Component{
    private final Pin preset;
    private final Pin clear;
    private final Pin clock;
    private final Pin data;
    private final Pin supply;
    private final Pin out;
    private final Pin oppositeOut;
    private static class ThreeInputNOR extends Component{
        private final Pin inOne;
        private final Pin inTwo;
        private final Pin inThree;
        private final Pin supply;
        private final Pin out;
        private ThreeInputNOR() {
            inOne = new Pin(this);
            inTwo = new Pin(this);
            inThree = new Pin(this);
            supply = new Pin(this);
            out = new Pin(this);

            ORGate orGate = new ORGate();
            subComponents.add(orGate);
            orGate.getSupply().addConnection(supply);
            orGate.getInputOne().addConnection(inOne);
            orGate.getInputTwo().addConnection(inTwo);

            NORGate norGate = new NORGate();
            subComponents.add(norGate);
            norGate.getSupply().addConnection(supply);
            norGate.getInputOne().addConnection(orGate.getOutput());
            norGate.getInputTwo().addConnection(inThree);
            norGate.getOutput().addConnection(out);
        }
    }

    public EdgeFlipFlopWithClear() {
        preset = new Pin(this);
        clear = new Pin(this);
        clock = new Pin(this);
        data = new Pin(this);
        supply = new Pin(this);
        out = new Pin(this);
        oppositeOut = new Pin(this);

        ThreeInputNOR[] norGates = new ThreeInputNOR[6];
        for (int i = 0; i < norGates.length; i++) {
            norGates[i] = new ThreeInputNOR();
            subComponents.add(norGates[i]);
            norGates[i].supply.addConnection(supply);
        }

        Inverter clockInverter = new Inverter();
        subComponents.add(clockInverter);
        clockInverter.getSupply().addConnection(supply);
        clockInverter.getIn().addConnection(clock);

        norGates[0].inOne.addConnection(clear);
        norGates[0].inTwo.addConnection(norGates[3].out);
        norGates[0].inThree.addConnection(norGates[1].out);

        norGates[1].inOne.addConnection(norGates[0].out);
        norGates[1].inTwo.addConnection(preset);
        norGates[1].inThree.addConnection(clockInverter.getOut());

        norGates[2].inOne.addConnection(norGates[1].out);
        norGates[2].inTwo.addConnection(clockInverter.getOut());
        norGates[2].inThree.addConnection(norGates[3].out);

        norGates[3].inOne.addConnection(norGates[2].out);
        norGates[3].inTwo.addConnection(preset);
        norGates[3].inThree.addConnection(data);

        norGates[4].inOne.addConnection(clear);
        norGates[4].inTwo.addConnection(norGates[1].out);
        norGates[4].inThree.addConnection(norGates[5].out);

        norGates[5].inOne.addConnection(norGates[4].out);
        norGates[5].inTwo.addConnection(preset);
        norGates[5].inThree.addConnection(norGates[2].out);

        out.addConnection(norGates[4].out);
        oppositeOut.addConnection(norGates[5].out);
    }
    public Pin getPreset() {
        return preset;
    }
    public Pin getClear() {
        return clear;
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
