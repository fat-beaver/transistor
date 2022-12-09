import javax.swing.*;
import java.awt.*;

public class ControlPanel extends Component {
    private final Pin[] display;
    private final Pin[] addressIn;
    private final Pin[] wordIn;
    private final Pin writeIn;
    private final Pin supply;
    private final Pin[] addressOut;
    private final Pin[] wordOut;
    private final Pin writeOut;
    private final Pin clear;
    private final Pin error;
    public ControlPanel(Container pane, GridBagConstraints constraints, int addressSize, int wordSize) {
        display = new Pin[wordSize];
        addressIn = new Pin[addressSize];
        wordIn = new Pin[wordSize];
        writeIn = new Pin(this);
        supply = new Pin(this);
        addressOut = new Pin[addressSize];
        wordOut = new Pin[wordSize];
        writeOut = new Pin(this);
        clear = new Pin(this);
        error = new Pin(this);

        Switch override = new Switch();
        subComponents.add(override);
        override.getIn().addConnection(supply);
        constraints.gridx = wordSize;
        constraints.gridy = 2;
        pane.add(override.getVisuals(), constraints);

        Switch clearSwitch = new Switch();
        subComponents.add(clearSwitch);
        clearSwitch.getIn().addConnection(supply);
        clearSwitch.getOut().addConnection(clear);
        constraints.gridx = wordSize - 2;
        pane.add(clearSwitch.getVisuals(), constraints);

        Light errorLight = new Light();
        subComponents.add(errorLight);
        constraints.gridx = wordSize - 4;
        pane.add(errorLight.getVisuals(), constraints);

        RSLatch errorLatch = new RSLatch();
        subComponents.add(errorLatch);
        errorLatch.getSupply().addConnection(supply);
        errorLatch.getSet().addConnection(error);
        errorLatch.getReset().addConnection(clear);
        errorLatch.getOut().addConnection(errorLight.getInput());

        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(new JLabel("Address"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(new JLabel("Input"), constraints);
        constraints.gridy = 2;
        pane.add(new JLabel("Write"), constraints);
        constraints.gridx = wordSize - 1;
        pane.add(new JLabel("Override"), constraints);
        constraints.gridx = wordSize - 3;
        pane.add(new JLabel("Clear"), constraints);
        constraints.gridx = wordSize - 5;
        pane.add(new JLabel("ERROR"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        pane.add(new JLabel("Output"), constraints);

        Switch[] addressSwitches = new Switch[addressSize];
        TwoToOneSelector[] addressSelectors = new TwoToOneSelector[addressSize];

        constraints.gridy = 0;
        for (int i = 0; i < addressSize; i++) {
            addressIn[i] = new Pin(this);
            addressOut[i] = new Pin(this);
            addressSwitches[i] = new Switch();
            subComponents.add(addressSwitches[i]);
            addressSwitches[i].getIn().addConnection(supply);
            addressSelectors[i] = new TwoToOneSelector();
            subComponents.add(addressSelectors[i]);
            addressSelectors[i].getSupply().addConnection(supply);
            addressSelectors[i].getInOne().addConnection(addressIn[i]);
            addressSelectors[i].getInTwo().addConnection(addressSwitches[i].getOut());
            addressSelectors[i].getSelect().addConnection(override.getOut());
            addressSelectors[i].getOut().addConnection(addressOut[i]);
            constraints.gridx = addressSize - i;
            pane.add(addressSwitches[i].getVisuals(), constraints);
        }

        Switch[] inputSwitches = new Switch[wordSize];
        TwoToOneSelector[] inputSelectors = new TwoToOneSelector[wordSize];
        Light[] outputLights = new Light[wordSize];

        for (int i = 0; i < wordSize; i++) {
            display[i] = new Pin(this);
            wordIn[i] = new Pin(this);
            wordOut[i] = new Pin(this);
            inputSwitches[i] = new Switch();
            subComponents.add(inputSwitches[i]);
            inputSwitches[i].getIn().addConnection(supply);
            inputSelectors[i] = new TwoToOneSelector();
            subComponents.add(inputSelectors[i]);
            inputSelectors[i].getSupply().addConnection(supply);
            inputSelectors[i].getInOne().addConnection(wordIn[i]);
            inputSelectors[i].getInTwo().addConnection(inputSwitches[i].getOut());
            inputSelectors[i].getSelect().addConnection(override.getOut());
            wordOut[i].addConnection(inputSelectors[i].getOut());
            constraints.gridy = 1;
            constraints.gridx = wordSize - i;
            pane.add(inputSwitches[i].getVisuals(), constraints);

            outputLights[i] = new Light();
            subComponents.add(outputLights[i]);
            outputLights[i].getInput().addConnection(display[i]);
            constraints.gridy = 3;
            constraints.gridx = wordSize - i;
            pane.add(outputLights[i].getVisuals(), constraints);
        }
        constraints.gridy = 2;
        Switch saveSwitch = new Switch();
        subComponents.add(saveSwitch);
        saveSwitch.getIn().addConnection(supply);
        TwoToOneSelector writeSelector = new TwoToOneSelector();
        subComponents.add(writeSelector);
        writeSelector.getSupply().addConnection(supply);
        writeSelector.getInOne().addConnection(writeIn);
        writeSelector.getInTwo().addConnection(saveSwitch.getOut());
        writeSelector.getSelect().addConnection(override.getOut());
        writeSelector.getOut().addConnection(writeOut);
        constraints.gridx = 1;
        pane.add(saveSwitch.getVisuals(), constraints);
    }
    public Pin getDisplay(int i) {
        return display[i];
    }
    public Pin getAddressIn(int i) {
        return addressIn[i];
    }
    public Pin getWordIn(int i) {
        return wordIn[i];
    }
    public Pin getWriteIn() {
        return writeIn;
    }
    public Pin getSupply() {
        return supply;
    }
    public Pin getAddressOut(int i) {
        return addressOut[i];
    }
    public Pin getWordOut(int i) {
        return wordOut[i];
    }
    public Pin getWriteOut() {
        return writeOut;
    }
    public Pin getClear() {
        return clear;
    }
    public Pin getError() {
        return error;
    }
}
