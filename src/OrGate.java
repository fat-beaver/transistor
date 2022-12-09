public class OrGate extends LogicGate {
    public OrGate() {
        super();

        Transistor transistorOne = new Transistor();
        subComponents.add(transistorOne);

        Transistor transistorTwo = new Transistor();
        subComponents.add(transistorTwo);

        transistorOne.getCollector().addConnection(supply);
        transistorTwo.getCollector().addConnection(supply);

        transistorOne.getBase().addConnection(inOne);
        transistorTwo.getBase().addConnection(inTwo);

        transistorOne.getEmitter().addConnection(out);
        transistorTwo.getEmitter().addConnection(out);
    }
}
