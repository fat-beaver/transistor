public class AndGate extends LogicGate{
    public AndGate() {
        super();

        Transistor transistorOne = new Transistor();
        subComponents.add(transistorOne);

        Transistor transistorTwo = new Transistor();
        subComponents.add(transistorTwo);

        transistorOne.getCollector().addConnection(supply);
        transistorTwo.getCollector().addConnection(transistorOne.getEmitter());

        transistorOne.getBase().addConnection(inOne);
        transistorTwo.getBase().addConnection(inTwo);

        transistorTwo.getEmitter().addConnection(out);
    }
}
