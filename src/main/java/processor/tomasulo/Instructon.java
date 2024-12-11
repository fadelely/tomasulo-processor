package processor.tomasulo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class Instructon {
    private final SimpleStringProperty instruction;
    private final SimpleBooleanProperty current;

    public Instructon(String instruction, boolean current) {
        this.instruction = new SimpleStringProperty(instruction);
        this.current = new SimpleBooleanProperty(current);
    }

    public String getInstruction() {
        return instruction.get();
    }

    public void setInstruction(String instruction) {
        this.instruction.set(instruction);
    }

    public boolean getCurrent() {
        return current.get();
    }

    public void setCurrent(boolean current) {
        this.current.set(current);
    }

    public SimpleStringProperty instructionProperty() {
        return instruction;
    }

    public SimpleBooleanProperty currentProperty() {
        return current;
    }
}
