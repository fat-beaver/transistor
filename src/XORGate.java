public class XORGate extends LogicGate{
    public XORGate() {
        super();

        ORGate orGate = new ORGate();
        subComponents.add(orGate);
        orGate.getSupply().addConnection(supply);
        orGate.getInputOne().addConnection(inOne);
        orGate.getInputTwo().addConnection(inTwo);

        NANDGate nandGate = new NANDGate();
        subComponents.add(nandGate);
        nandGate.getSupply().addConnection(supply);
        nandGate.getInputOne().addConnection(inOne);
        nandGate.getInputTwo().addConnection(inTwo);

        ANDGate andGate = new ANDGate();
        subComponents.add(andGate);
        andGate.getSupply().addConnection(supply);
        andGate.getInputOne().addConnection(orGate.getOutput());
        andGate.getInputTwo().addConnection(nandGate.getOutput());
        andGate.getOutput().addConnection(out);
    }
}
