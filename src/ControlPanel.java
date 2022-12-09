import javax.swing.*;
import java.awt.*;

public class ControlPanel extends Component {
    private final Pin[] display;
    private final Pin supply;
    private final Pin[] address;
    private final Pin[] word;
    private final Pin write;
    private final Pin clear;
    private final Pin error;
    private final Pin override;
    public ControlPanel(Container pane, GridBagConstraints constraints, int addressSize, int wordSize) {
        display = new Pin[wordSize];
        supply = new Pin(this);
        address = new Pin[addressSize];
        word = new Pin[wordSize];
        write = new Pin(this);
        clear = new Pin(this);
        error = new Pin(this);
        override = new Pin(this);

        Switch overrideSwitch = new Switch();
        subComponents.add(overrideSwitch);
        overrideSwitch.getIn().addWithoutCheck(supply);
        overrideSwitch.getOut().addConnection(override);
        constraints.gridx = wordSize - 1;
        constraints.gridy = 3;
        pane.add(overrideSwitch.getVisuals(), constraints);

        Switch clearSwitch = new Switch();
        subComponents.add(clearSwitch);
        clearSwitch.getIn().addWithoutCheck(supply);
        clearSwitch.getOut().addConnection(clear);
        constraints.gridx = wordSize - 2;
        pane.add(clearSwitch.getVisuals(), constraints);

        Light errorLight = new Light();
        subComponents.add(errorLight);
        constraints.gridx = wordSize - 3;
        pane.add(errorLight.getVisuals(), constraints);

        RSLatch errorLatch = new RSLatch();
        subComponents.add(errorLatch);
        errorLatch.getSupply().addWithoutCheck(supply);
        errorLatch.getSet().addConnection(error);
        errorLatch.getReset().addConnection(clear);
        errorLatch.getOut().addConnection(errorLight.getInput());

        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(new JLabel("Address"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(new JLabel("Input"), constraints);
        constraints.gridx = wordSize;
        constraints.gridy = 2;
        pane.add(new JLabel("Write"), constraints);
        constraints.gridx = wordSize - 1;
        pane.add(new JLabel("Override"), constraints);
        constraints.gridx = wordSize - 2;
        pane.add(new JLabel("Clear"), constraints);
        constraints.gridx = wordSize - 3;
        pane.add(new JLabel("ERROR"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        pane.add(new JLabel("Output"), constraints);

        Switch[] addressSwitches = new Switch[addressSize];

        constraints.gridy = 0;
        for (int i = 0; i < addressSize; i++) {
            address[i] = new Pin(this);
            addressSwitches[i] = new Switch();
            subComponents.add(addressSwitches[i]);
            addressSwitches[i].getIn().addWithoutCheck(supply);
            addressSwitches[i].getOut().addConnection(address[i]);
            constraints.gridx = Math.max(wordSize, addressSize) - i;
            pane.add(addressSwitches[i].getVisuals(), constraints);
        }

        Switch[] inputSwitches = new Switch[wordSize];
        Light[] outputLights = new Light[wordSize];

        for (int i = 0; i < wordSize; i++) {
            display[i] = new Pin(this);
            word[i] = new Pin(this);
            inputSwitches[i] = new Switch();
            subComponents.add(inputSwitches[i]);
            inputSwitches[i].getIn().addWithoutCheck(supply);
            inputSwitches[i].getOut().addConnection(word[i]);
            constraints.gridy = 1;
            constraints.gridx = wordSize - i;
            pane.add(inputSwitches[i].getVisuals(), constraints);

            outputLights[i] = new Light();
            subComponents.add(outputLights[i]);
            outputLights[i].getInput().addConnection(display[i]);
            constraints.gridy = 4;
            constraints.gridx = wordSize - i;
            pane.add(outputLights[i].getVisuals(), constraints);
        }
        Switch writeSwitch = new Switch();
        subComponents.add(writeSwitch);
        writeSwitch.getIn().addWithoutCheck(supply);
        writeSwitch.getOut().addConnection(write);
        constraints.gridx = wordSize;
        constraints.gridy = 3;
        pane.add(writeSwitch.getVisuals(), constraints);
    }
    public Pin getDisplay(int i) {
        return display[i];
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getAddress(int i) {
        return address[i];
    }
    public Pin getWord(int i) {
        return word[i];
    }
    public Pin getWrite() {
        return write;
    }
    public Pin getClear() {
        return clear;
    }
    public Pin getError() {
        return error;
    }
    public Pin getOverride() {
        return override;
    }
}
