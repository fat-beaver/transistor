import java.util.ArrayList;
import java.util.Scanner;

public class Testing {
    boolean running = true;
    ArrayList<Component> components = new ArrayList<>();
    ArrayList<Switch> switches = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<>();
    SupplyPin supply;
    public Testing() {


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
                    case "lights":
                        for (Light light : lights) {
                            light.showState();
                        }
                        break;
                    default:
                        System.out.println("command not found");
                        break;
                }
            }
        });

        supply = new SupplyPin();

        Switch switchOne = new Switch();
        components.add(switchOne);
        switches.add(switchOne);
        switchOne.getIn().addConnection(supply);

        Switch switchTwo = new Switch();
        components.add(switchTwo);
        switches.add(switchTwo);
        switchTwo.getIn().addConnection(supply);

        Light lightOne = new Light();
        components.add(lightOne);
        lights.add(lightOne);

        Light lightTwo = new Light();
        components.add(lightTwo);
        lights.add(lightTwo);

        Adder adder = new Adder();
        components.add(adder);
        adder.getSupply().addConnection(supply);
        adder.getInOne().addConnection(switchOne.getOut());
        adder.getInTwo().addConnection(switchTwo.getOut());
        adder.getSumOut().addConnection(lightOne.getInput());
        adder.getCarryOut().addConnection(lightTwo.getInput());
        input.start();

        while (running) {
            for (Component component : components) {
                component.doCycle();
            }
        }
    }
    public static void main(String[] args) {
        new Testing();
    }
}
