import javax.swing.*;
import java.awt.*;

public class ControlPanel extends Component {
    private final Pin[] in;
    private final Pin supply;
    private final Pin[] addressOut;
    private final Pin[] wordOut;
    private final Pin write;
    public ControlPanel(Container pane, GridBagConstraints constraints, int addressSize, int wordSize) {
        in = new Pin[wordSize];
        supply = new Pin(this);
        addressOut = new Pin[addressSize];
        wordOut = new Pin[wordSize];
        write = new Pin(this);
        
        Switch override = new Switch();
        subComponents.add(override);
        override.getIn().addConnection(supply);
        constraints.gridx = wordSize;
        constraints.gridy = 2;
        pane.add(override.getVisuals(), constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 0;
        pane.add(new JLabel("Address"), constraints);
        Switch[] addressSwitches = new Switch[addressSize];
        ANDGate[] addressSelectors = new ANDGate[addressSize];
        for (int i = 0; i < addressSize; i++) {
            addressOut[i] = new Pin(this);
            addressSwitches[i] = new Switch();
            subComponents.add(addressSwitches[i]);
            addressSwitches[i].getIn().addConnection(supply);
            addressSelectors[i] = new ANDGate();
            subComponents.add(addressSelectors[i]);
            addressSelectors[i].getSupply().addConnection(supply);
            addressSelectors[i].getInputOne().addConnection(addressSwitches[i].getOut());
            addressSelectors[i].getInputTwo().addConnection(override.getOut());
            addressSelectors[i].getOutput().addConnection(addressOut[i]);
            constraints.gridx = addressSize - i;
            pane.add(addressSwitches[i].getVisuals(), constraints);
        }

        constraints.gridx = 0;
        constraints.gridy = 1;
        pane.add(new JLabel("Input"), constraints);
        constraints.gridy = 2;
        pane.add(new JLabel("Save"), constraints);
        constraints.gridx = wordSize - 1;
        pane.add(new JLabel("Override"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        pane.add(new JLabel("Output"), constraints);

        Switch[] inputSwitches = new Switch[wordSize];
        ANDGate[] inputSelectors = new ANDGate[wordSize];
        Light[] outputLights = new Light[wordSize];

        for (int i = 0; i < wordSize; i++) {
            wordOut[i] = new Pin(this);
            inputSwitches[i] = new Switch();
            subComponents.add(inputSwitches[i]);
            inputSwitches[i].getIn().addConnection(supply);
            inputSelectors[i] = new ANDGate();
            subComponents.add(inputSelectors[i]);
            inputSelectors[i].getSupply().addConnection(supply);
            inputSelectors[i].getInputOne().addConnection(inputSwitches[i].getOut());
            inputSelectors[i].getInputTwo().addConnection(override.getOut());
            wordOut[i].addConnection(inputSelectors[i].getOutput());
            constraints.gridy = 1;
            constraints.gridx = wordSize - i;
            pane.add(inputSwitches[i].getVisuals(), constraints);

            in[i] = new Pin(this);
            outputLights[i] = new Light();
            subComponents.add(outputLights[i]);
            outputLights[i].getInput().addConnection(in[i]);
            constraints.gridy = 3;
            constraints.gridx = wordSize - i;
            pane.add(outputLights[i].getVisuals(), constraints);
        }
        constraints.gridy = 2;
        Switch saveSwitch = new Switch();
        subComponents.add(saveSwitch);
        saveSwitch.getIn().addConnection(supply);
        ANDGate saveSelector = new ANDGate();
        subComponents.add(saveSelector);
        saveSelector.getSupply().addConnection(supply);
        saveSelector.getInputOne().addConnection(saveSwitch.getOut());
        saveSelector.getInputTwo().addConnection(override.getOut());
        saveSelector.getOutput().addConnection(write);
        constraints.gridx = 1;
        pane.add(saveSwitch.getVisuals(), constraints);
    }

    public Pin getIn(int i) {
        return in[i];
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
    public Pin getWrite() {
        return write;
    }
}
