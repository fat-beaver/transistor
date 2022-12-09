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
