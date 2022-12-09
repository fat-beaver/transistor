public class NANDGate extends LogicGate {
    public NANDGate() {
        super();

        ANDGate andGate = new ANDGate();
        subComponents.add(andGate);
        andGate.getSupply().addConnection(supply);
        inOne.addConnection(andGate.getInputOne());
        inTwo.addConnection(andGate.getInputTwo());

        Inverter inverter = new Inverter();
        subComponents.add(inverter);
        inverter.getSupply().addConnection(supply);
        inverter.getIn().addConnection(andGate.getOutput());
        inverter.getOut().addConnection(out);
    }
}
