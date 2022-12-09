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
                            System.out.println();
                        }
                        break;
                    default:
                        System.out.println("command not found");
                        break;
                }
            }
        });
        long startTime = System.nanoTime();

        SupplyPin supply = new SupplyPin();

        TwoFiftySixWordCell cell = new TwoFiftySixWordCell();
        subComponents.add(cell);
        cell.getSupply().addConnection(supply);

        Switch[] addressSwitches = new Switch[TwoFiftySixWordCell.ADDRESS_SIZE];
        for (int i = 0; i < TwoFiftySixWordCell.ADDRESS_SIZE; i++) {
            addressSwitches[i] = new Switch();
            subComponents.add(addressSwitches[i]);
            addressSwitches[i].getIn().addConnection(supply);
            addressSwitches[i].getOut().addConnection(cell.getAddress(i));
            switches.add(addressSwitches[i]);
        }

        Switch[] inputSwitches = new Switch[TwoFiftySixWordCell.WORD_SIZE];
        Light[] outputLights = new Light[TwoFiftySixWordCell.WORD_SIZE];

        for (int i = 0; i < TwoFiftySixWordCell.WORD_SIZE; i++) {
            inputSwitches[i] = new Switch();
            subComponents.add(inputSwitches[i]);
            inputSwitches[i].getIn().addConnection(supply);
            inputSwitches[i].getOut().addConnection(cell.getIn());

            outputLights[i] = new Light();
            subComponents.add(outputLights[i]);
            outputLights[i].getInput().addConnection(cell.getOut());
            lights.add(outputLights[i]);
        }

        Switch saveSwitch = new Switch();
        subComponents.add(saveSwitch);
        saveSwitch.getIn().addConnection(supply);
        saveSwitch.getOut().addConnection(cell.getWrite());

        switches.add(inputSwitches[0]);
        switches.add(saveSwitch);



        System.out.println(System.nanoTime() - startTime);
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
