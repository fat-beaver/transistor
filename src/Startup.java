import java.util.ArrayList;
import java.util.Scanner;

public class Startup {
    boolean running = true;
    ArrayList<Switch> switches = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<>();
    ArrayList<Component> components = new ArrayList<>();
    public static void main(String[] args) {
        new Startup();
    }
    public Startup() {
        Thread input = new Thread(() -> {
            Scanner input1 = new Scanner(System.in);
            while (running) {
                String userInput = input1.nextLine();
                String[] userInputParts = userInput.toLowerCase().split(" ");
                switch (userInputParts[0]) {
                    case "stop":
                        running = false;
                        break;
                    case "switch":
                        if (userInputParts.length != 2) {
                            System.out.println("incorrect number of arguments");
                        } else {
                            try {
                                int switchID = Integer.parseInt(userInputParts[1]);
                                if (switches.size() <= switchID) {
                                    System.out.println("Switch ID out of bounds");
                                } else {
                                    switches.get(switchID).flip();
                                    System.out.println("flipped switch");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("argument must be a number");
                            }
                        }
                        break;
                    case "light":
                        if (userInputParts.length != 2) {
                            System.out.println("incorrect number of arguments");
                        } else {
                            try {
                                int lightID = Integer.parseInt(userInputParts[1]);
                                if (lights.size() <= lightID) {
                                    System.out.println("Light ID out of bounds");
                                } else {
                                    lights.get(lightID).showState();
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("argument must be a number");
                            }
                        }
                        break;
                    default:
                        System.out.println("command not found");
                        break;
                }
            }
        });
        setUpComponents();

        input.start();

        while (running) {
            for (Component component : components) {
                component.doCycle();
            }
        }
    }
    private void setUpComponents() {
        //create the parts
        Light lightOne = new Light();
        lights.add(lightOne);
        components.add(lightOne);

        Light lightTwo = new Light();
        lights.add(lightTwo);
        components.add(lightTwo);

        SupplyPin supply = new SupplyPin();

        Switch switchOne = new Switch();
        switches.add(switchOne);
        components.add(switchOne);

        Switch switchTwo = new Switch();
        switches.add(switchTwo);
        components.add(switchTwo);

        OrGate orGate = new OrGate();
        components.add(orGate);

        AndGate andGate = new AndGate();
        components.add(andGate);

        //connect them together
        switchOne.getIn().addConnection(supply);
        switchOne.getOut().addConnection(orGate.getInputOne());
        switchOne.getOut().addConnection(andGate.getInputOne());

        switchTwo.getIn().addConnection(supply);
        switchTwo.getOut().addConnection(orGate.getInputTwo());
        switchTwo.getOut().addConnection(andGate.getInputTwo());

        orGate.getSupply().addConnection(supply);
        orGate.getOutput().addConnection(lightOne.getInput());

        andGate.getSupply().addConnection(supply);
        andGate.getOutput().addConnection(lightTwo.getInput());
    }
}
