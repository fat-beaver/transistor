import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Testing {
    boolean running = true;
    ArrayList<Component> subComponents = new ArrayList<>();
    ArrayList<Switch> switches = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<>();
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
                                    if (lights.get(lightID).getState()) {
                                        System.out.println("ON");
                                    } else {
                                        System.out.println("OFF");
                                    }
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("argument must be a number");
                            }
                        }
                        break;
                    case "lights":
                        for (Light light : lights) {
                            if (light.getState()) {
                                System.out.print("ON | ");
                            } else {
                                System.out.print("OFF | ");
                            }
                        }
                        System.out.println();
                        break;
                    default:
                        System.out.println("command not found");
                        break;
                }
            }
        });
        long startTime = System.currentTimeMillis();

        SupplyPin supply = new SupplyPin();

        SixteenBitLatch latch = new SixteenBitLatch();
        subComponents.add(latch);
        latch.getSupply().addWithoutCheck(supply);

        Switch clockSwitch = new Switch();
        subComponents.add(clockSwitch);
        switches.add(clockSwitch);
        clockSwitch.getIn().addWithoutCheck(supply);
        clockSwitch.getOut().addConnection(latch.getClock());
        for (int i = 0; i < SixteenBitLatch.WORD_LENGTH; i++) {
            Switch inputSwitch = new Switch();
            subComponents.add(inputSwitch);
            switches.add(inputSwitch);
            inputSwitch.getIn().addWithoutCheck(supply);
            inputSwitch.getOut().addConnection(latch.getIn(i));

            Light outputLight = new Light();
            subComponents.add(outputLight);
            lights.add(outputLight);
            outputLight.getInput().addConnection(latch.getOut(i));

        }

        for (int i = 0; i < SixteenBitLatch.WORD_LENGTH; i++) {
            Light inputLight = new Light();
            subComponents.add(inputLight);
            lights.add(inputLight);
            inputLight.getInput().addConnection(latch.getIn(i));
        }

        System.out.println(System.currentTimeMillis() - startTime);
        input.start();

        while (running) {
            for (Component component : subComponents) {
                component.doCycle();
            }
        }
    }
    public static void main(String[] args) {
        new Testing();
    }
}
