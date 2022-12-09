public class NORGate extends LogicGate {
    public NORGate() {
        super();

        ORGate orGate = new ORGate();
        subComponents.add(orGate);
        orGate.getSupply().addConnection(supply);
        inOne.addConnection(orGate.getInputOne());
        inTwo.addConnection(orGate.getInputTwo());

        Inverter inverter = new Inverter();
        subComponents.add(inverter);
        inverter.getSupply().addConnection(supply);
        inverter.getIn().addConnection(orGate.getOutput());
        inverter.getOut().addConnection(out);


    }
}
