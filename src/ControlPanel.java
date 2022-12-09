import javax.swing.*;
import java.awt.*;

public class ControlPanel extends Component {
    private final Pin[] addressDisplay;
    private final Pin[] accumulatorDisplay;
    private final Pin[] wordDisplay;
    private final Pin[] instructionDisplay;
    private final Pin supply;
    private final Pin[] address;
    private final Pin[] word;
    private final Pin RAMWrite;
    private final Pin programWrite;
    private final Pin clear;
    private final Pin error;
    private final Pin override;
    public ControlPanel(Container pane, GridBagConstraints constraints, int addressSize, int wordSize) {
        addressDisplay = new Pin[addressSize];
        accumulatorDisplay = new Pin[wordSize];
        wordDisplay = new Pin[wordSize];
        instructionDisplay = new Pin[wordSize];
        supply = new Pin(this);
        address = new Pin[addressSize];
        word = new Pin[wordSize];
        RAMWrite = new Pin(this);
        programWrite = new Pin(this);
        clear = new Pin(this);
        error = new Pin(this);
        override = new Pin(this);

        Switch overrideSwitch = new Switch();
        subComponents.add(overrideSwitch);
        overrideSwitch.getIn().addWithoutCheck(supply);
        overrideSwitch.getOut().addConnection(override);
        constraints.gridx = wordSize - 2;
        constraints.gridy = 3;
        pane.add(overrideSwitch.getVisuals(), constraints);

        Switch clearSwitch = new Switch();
        subComponents.add(clearSwitch);
        clearSwitch.getIn().addWithoutCheck(supply);
        clearSwitch.getOut().addConnection(clear);
        constraints.gridx = wordSize - 3;
        pane.add(clearSwitch.getVisuals(), constraints);

        Light errorLight = new Light();
        subComponents.add(errorLight);
        constraints.gridx = wordSize - 4;
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
        constraints.gridy = 2;
        constraints.gridx = wordSize;
        pane.add(new JLabel("Write to program"), constraints);
        constraints.gridx = wordSize - 1;
        pane.add(new JLabel("Write to RAM"), constraints);
        constraints.gridx = wordSize - 2;
        pane.add(new JLabel("Override"), constraints);
        constraints.gridx = wordSize - 3;
        pane.add(new JLabel("Clear"), constraints);
        constraints.gridx = wordSize - 4;
        pane.add(new JLabel("ERROR"), constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        pane.add(new JLabel("Accumulator Value"), constraints);
        constraints.gridy = 5;
        pane.add(new JLabel("Current Address"), constraints);
        constraints.gridy = 6;
        pane.add(new JLabel("Word at Address"), constraints);
        constraints.gridy = 7;
        pane.add(new JLabel("Instruction at Address"), constraints);

        Switch[] addressSwitches = new Switch[addressSize];
        Light[] addressOutputLights = new Light[addressSize];

        for (int i = 0; i < addressSize; i++) {
            address[i] = new Pin(this);
            addressSwitches[i] = new Switch();
            subComponents.add(addressSwitches[i]);
            addressSwitches[i].getIn().addWithoutCheck(supply);
            addressSwitches[i].getOut().addConnection(address[i]);
            addressDisplay[i] = new Pin(this);
            addressOutputLights[i] = new Light();
            subComponents.add(addressOutputLights[i]);
            addressOutputLights[i].getInput().addConnection(addressDisplay[i]);

            constraints.gridx = Math.max(wordSize, addressSize) - i;
            constraints.gridy = 0;
            pane.add(addressSwitches[i].getVisuals(), constraints);
            constraints.gridy = 5;
            pane.add(addressOutputLights[i].getVisuals(), constraints);
        }

        Switch[] inputSwitches = new Switch[wordSize];
        Light[] accumulatorOutputLights = new Light[wordSize];
        Light[] wordOutputLights = new Light[wordSize];
        Light[] instructionOutputLights = new Light[wordSize];

        for (int i = 0; i < wordSize; i++) {
            accumulatorDisplay[i] = new Pin(this);
            wordDisplay[i] = new Pin(this);
            instructionDisplay[i] = new Pin(this);

            word[i] = new Pin(this);
            inputSwitches[i] = new Switch();
            subComponents.add(inputSwitches[i]);
            inputSwitches[i].getIn().addWithoutCheck(supply);
            inputSwitches[i].getOut().addConnection(word[i]);
            constraints.gridy = 1;
            constraints.gridx = wordSize - i;
            pane.add(inputSwitches[i].getVisuals(), constraints);

            accumulatorOutputLights[i] = new Light();
            subComponents.add(accumulatorOutputLights[i]);
            accumulatorOutputLights[i].getInput().addConnection(accumulatorDisplay[i]);
            constraints.gridy = 4;
            pane.add(accumulatorOutputLights[i].getVisuals(), constraints);

            wordOutputLights[i] = new Light();
            subComponents.add(wordOutputLights[i]);
            wordOutputLights[i].getInput().addConnection(wordDisplay[i]);
            constraints.gridy = 6;
            pane.add(wordOutputLights[i].getVisuals(), constraints);

            instructionOutputLights[i] = new Light();
            subComponents.add(instructionOutputLights[i]);
            instructionOutputLights[i].getInput().addConnection(instructionDisplay[i]);
            constraints.gridy = 7;
            pane.add(instructionOutputLights[i].getVisuals(), constraints);
        }
        Switch RAMWriteSwitch = new Switch();
        subComponents.add(RAMWriteSwitch);
        RAMWriteSwitch.getIn().addWithoutCheck(supply);
        RAMWriteSwitch.getOut().addConnection(RAMWrite);
        constraints.gridx = wordSize - 1;
        constraints.gridy = 3;
        pane.add(RAMWriteSwitch.getVisuals(), constraints);

        Switch programWriteSwitch = new Switch();
        subComponents.add(programWriteSwitch);
        programWriteSwitch.getIn().addWithoutCheck(supply);
        programWriteSwitch.getOut().addConnection(programWrite);
        constraints.gridx = wordSize;
        constraints.gridy = 3;
        pane.add(programWriteSwitch.getVisuals(), constraints);
    }
    public Pin getAddressDisplay(int i) {return addressDisplay[i];}
    public Pin getAccumulatorDisplay(int i) {return accumulatorDisplay[i];}
    public Pin getWordDisplay(int i) {return wordDisplay[i];}
    public Pin getInstructionDisplay(int i) {return instructionDisplay[i];}
    public Pin getSupply() {return supply;}
    public Pin getAddress(int i) {return address[i];}
    public Pin getWord(int i) {return word[i];}
    public Pin getRAMWrite() {return RAMWrite;}
    public Pin getProgramWrite() {return programWrite;}
    public Pin getClear() {return clear;}
    public Pin getError() {return error;}
    public Pin getOverride() {return override;}
}
