package processor.tomasulo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


@SuppressWarnings("unchecked")
public class App extends Application{

    public static Tomasulo tomasulo=new Tomasulo();
    
    
    public static int cellSize = 20;
    public static int maxRows = 10;


    private TableView<RegisterFile.IntegerRegister> integerRegisterTable=new TableView<>();
    private TableView<RegisterFile.FloatingRegister> floatingRegisterTable = new TableView<>();

    private void setupTable(TableView<Tomasulo.ReservationStation> table) {
        // Define columns
        TableColumn<Tomasulo.ReservationStation, Integer> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<Tomasulo.ReservationStation, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.ReservationStation, String> opcodeColumn = new TableColumn<>("OpCode");
        opcodeColumn.setCellValueFactory(new PropertyValueFactory<>("opcode"));

        TableColumn<Tomasulo.ReservationStation, Double> vjColumn = new TableColumn<>("Vj");
        vjColumn.setCellValueFactory(new PropertyValueFactory<>("vj"));

        TableColumn<Tomasulo.ReservationStation, Double> vkColumn = new TableColumn<>("Vk");
        vkColumn.setCellValueFactory(new PropertyValueFactory<>("vk"));

        TableColumn<Tomasulo.ReservationStation, String> qjColumn = new TableColumn<>("Qj");
        qjColumn.setCellValueFactory(cellData -> {
            Tomasulo.ReservationStation reservationStation = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(reservationStation.getQj()), reservationStation.qjProperty());
        });

        TableColumn<Tomasulo.ReservationStation, String> qkColumn = new TableColumn<>("Qk");
        qkColumn.setCellValueFactory(cellData -> {
            Tomasulo.ReservationStation reservationStation = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(reservationStation.getQk()), reservationStation.qkProperty());
        });


        TableColumn<Tomasulo.ReservationStation, Double> executionColumn = new TableColumn<>("Cycles");
        executionColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn, busyColumn, opcodeColumn, vjColumn, vkColumn, qjColumn, qkColumn, executionColumn);

        // Set fixed cell size
        table.setFixedCellSize(cellSize); // Adjust as needed
        double tableHeaderHeight = 28; // Approximate header height
        table.setPrefHeight((maxRows * table.getFixedCellSize()) + tableHeaderHeight);

        // Set table properties
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableLoad(TableView<Tomasulo.LoadBuffer> table) {

        TableColumn<Tomasulo.LoadBuffer, Number> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<Tomasulo.LoadBuffer, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.LoadBuffer, Number> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Tomasulo.LoadBuffer, String> qaddressColumn = new TableColumn<>("QAddress");
        qaddressColumn.setCellValueFactory(cellData -> {
            Tomasulo.LoadBuffer loadBuffer = cellData.getValue();
            return Bindings.createStringBinding(
                    () -> tomasulo.getTagString(loadBuffer.getQAddress()),
                    loadBuffer.QAddressProperty()
            );
        });

        TableColumn<Tomasulo.LoadBuffer, Double> executionColumn = new TableColumn<>("Cycles");
        executionColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn, busyColumn, addressColumn, qaddressColumn, executionColumn);

        // Set fixed cell size
        table.setFixedCellSize(cellSize); // Adjust as needed
        double tableHeaderHeight = 28; // Approximate header height
        table.setPrefHeight((maxRows * table.getFixedCellSize()) + tableHeaderHeight);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupTableStore(TableView<Tomasulo.StoreBuffer> table) {

        TableColumn<Tomasulo.StoreBuffer, Number> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<Tomasulo.StoreBuffer, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.StoreBuffer, Number> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Tomasulo.StoreBuffer, String> qaddressColumn = new TableColumn<>("QAddress");
        qaddressColumn.setCellValueFactory(cellData -> {
            Tomasulo.StoreBuffer StoreBuffer = cellData.getValue();
            return Bindings.createStringBinding(
                    () -> tomasulo.getTagString(StoreBuffer.getQAddress()),
                    StoreBuffer.QAddressProperty()
            );
        });

        TableColumn<Tomasulo.StoreBuffer, Number> vColumn = new TableColumn<>("V");
        vColumn.setCellValueFactory(new PropertyValueFactory<>("V"));

        TableColumn<Tomasulo.StoreBuffer, String> qColumn = new TableColumn<>("Q");
        qColumn.setCellValueFactory(cellData -> {
            Tomasulo.StoreBuffer storeBuffer = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(storeBuffer.getQ()), storeBuffer.QProperty());
        });

        TableColumn<Tomasulo.StoreBuffer, Double> executionColumn = new TableColumn<>("Cycles");
        executionColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn,qaddressColumn, vColumn, qColumn,executionColumn);

        // Set fixed cell size
        table.setFixedCellSize(cellSize); // Adjust as needed
        double tableHeaderHeight = 28; // Approximate header height
        table.setPrefHeight((maxRows * table.getFixedCellSize()) + tableHeaderHeight);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupImmediateTable(TableView<Tomasulo.IntegerReservationStation> table) {

        TableColumn<Tomasulo.IntegerReservationStation, Number> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<Tomasulo.IntegerReservationStation, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.IntegerReservationStation, String> opcodeColumn = new TableColumn<>("Opcode");
        opcodeColumn.setCellValueFactory(new PropertyValueFactory<>("opcode"));

        TableColumn<Tomasulo.IntegerReservationStation, Number> vjColumn = new TableColumn<>("Vj");
        vjColumn.setCellValueFactory(new PropertyValueFactory<>("vj"));

        TableColumn<Tomasulo.IntegerReservationStation, Number> vkColumn = new TableColumn<>("Vk");
        vkColumn.setCellValueFactory(new PropertyValueFactory<>("vk"));

        TableColumn<Tomasulo.IntegerReservationStation, String> qjColumn = new TableColumn<>("Qj");
        qjColumn.setCellValueFactory(cellData -> {
            Tomasulo.IntegerReservationStation station = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(station.getQj()), station.qjProperty());
        });

        // Updated Qk column with dynamic string binding
        TableColumn<Tomasulo.IntegerReservationStation, String> qkColumn = new TableColumn<>("Qk");
        qkColumn.setCellValueFactory(cellData -> {
            Tomasulo.IntegerReservationStation station = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(station.getQk()), station.qkProperty());
        });

        TableColumn<Tomasulo.IntegerReservationStation, Number> executionTimeColumn = new TableColumn<>("Cycles");
        executionTimeColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));

        // Add columns to the table
        table.getColumns().addAll(
                tagColumn, busyColumn, opcodeColumn,
                vjColumn, vkColumn, qjColumn, qkColumn,
                 executionTimeColumn
        );

        // Set fixed cell size
        double cellSize = 25; // Adjust as needed
        table.setFixedCellSize(cellSize);
        double tableHeaderHeight = 28; // Approximate header height
        int maxRows = 10; // Adjust as needed
        table.setPrefHeight((maxRows * table.getFixedCellSize()) + tableHeaderHeight);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupIntegerRegistersTable(TableView<RegisterFile.IntegerRegister> table) {
        // Column for the register name
        TableColumn<RegisterFile.IntegerRegister, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));

        // Column for the Qi value, converted to a string dynamically using binding
        TableColumn<RegisterFile.IntegerRegister, String> qiColumn = new TableColumn<>("Qi");
        qiColumn.setCellValueFactory(cellData -> {
            RegisterFile.IntegerRegister register = cellData.getValue();
            // Use binding to convert the Qi (int) to a corresponding string
            return Bindings.createStringBinding(() -> tomasulo.getTagString(register.getQi()), register.qiProperty());
        });

        // Column for the value of the register
        TableColumn<RegisterFile.IntegerRegister, Number> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Add the columns to the table
        table.getColumns().addAll(nameColumn, qiColumn, valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupFloatingRegistersTable(TableView<RegisterFile.FloatingRegister> table) {
        // Column for the register name
        TableColumn<RegisterFile.FloatingRegister, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));

        // Column for the Qi value, dynamically updated using binding
        TableColumn<RegisterFile.FloatingRegister, String> qiColumn = new TableColumn<>("Qi");
        qiColumn.setCellValueFactory(cellData -> {
            RegisterFile.FloatingRegister register = cellData.getValue();
            // Using binding to convert the Qi value into a string
            return Bindings.createStringBinding(() -> tomasulo.getTagString(register.getQi()), register.qiProperty());
        });

        // Column for the value of the register
        TableColumn<RegisterFile.FloatingRegister, Number> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        // Add the columns to the table
        table.getColumns().addAll(nameColumn, qiColumn, valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void setupTableBranch(TableView<Tomasulo.BranchStation> table) {

        TableColumn<Tomasulo.BranchStation, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.BranchStation, String> opcodeColumn = new TableColumn<>("OpCode");
        opcodeColumn.setCellValueFactory(new PropertyValueFactory<>("opcode"));

        TableColumn<Tomasulo.BranchStation, Double> vjColumn = new TableColumn<>("Vj");
        vjColumn.setCellValueFactory(new PropertyValueFactory<>("vj"));
        
        TableColumn<Tomasulo.BranchStation, Double> vkColumn = new TableColumn<>("Vk");
        vkColumn.setCellValueFactory(new PropertyValueFactory<>("vk"));

        TableColumn<Tomasulo.BranchStation, String> qjColumn = new TableColumn<>("Qj");
        qjColumn.setCellValueFactory(cellData -> {
            Tomasulo.BranchStation station = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(station.getQj()), station.qjProperty());
        });

        // Updated Qk column with dynamic string binding
        TableColumn<Tomasulo.BranchStation, String> qkColumn = new TableColumn<>("Qk");
        qkColumn.setCellValueFactory(cellData -> {
            Tomasulo.BranchStation station = cellData.getValue();
            return Bindings.createStringBinding(() -> tomasulo.getTagString(station.getQk()), station.qkProperty());
        });

        TableColumn<Tomasulo.BranchStation, Double> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        
        TableColumn<Tomasulo.BranchStation, Double> executionColumn = new TableColumn<>("Cycles");
        executionColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));


        table.getColumns().addAll(busyColumn, opcodeColumn, vjColumn, vkColumn, qjColumn, qkColumn, addressColumn, executionColumn);
        table.setFixedCellSize(cellSize);
        double tableHeaderHeight = 28; 
        table.setPrefHeight(2*(table.getFixedCellSize()) + tableHeaderHeight);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void setupEditableIntegerRegistersTable(TableView<RegisterFile.IntegerRegister> table) {
        // Name column (non-editable)
        TableColumn<RegisterFile.IntegerRegister, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));
        nameColumn.setEditable(false); // Make the Name column non-editable

        // Qi column (editable)
        TableColumn<RegisterFile.IntegerRegister, String> qiColumn = new TableColumn<>("Qi");
        qiColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getQi())));
        qiColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        qiColumn.setOnEditCommit(event -> {
            RegisterFile.IntegerRegister register = event.getRowValue();
            try {
                register.setQi(Integer.parseInt(event.getNewValue()));
            } catch (NumberFormatException e) {
                // Handle invalid input (e.g., show an alert)
                register.setQi(register.getQi()); // Reset to previous value
            }
        });

        TableColumn<RegisterFile.IntegerRegister, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue()))); // Converts long to String
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(event -> {
            RegisterFile.IntegerRegister register = event.getRowValue();
            try {
                // Parse the input as a 64-bit long value
                System.out.println("new value is ");

                long newValue = Long.parseLong(event.getNewValue());
                System.out.println("new value is "+newValue);

                register.setValue(newValue); // Set the new value to the LongProperty
                System.out.println("new value is "+newValue);
            } catch (NumberFormatException e) {
                // Handle invalid input (e.g., show an alert or reset to previous value)
                System.out.println("Could not parse the value"+e.getMessage());
                register.setValue(register.getValue()); // Reset to the previous valid value
            }
        });

        // Add columns to the table
        table.getColumns().addAll(nameColumn, qiColumn, valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(true);
    }

    private void setupEditableFloatingRegistersTable(TableView<RegisterFile.FloatingRegister> table) {
        // Name column (non-editable)
        TableColumn<RegisterFile.FloatingRegister, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));
        nameColumn.setEditable(false); // Make the Name column non-editable

        // Qi column (editable)
        TableColumn<RegisterFile.FloatingRegister, String> qiColumn = new TableColumn<>("Qi");
        qiColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getQi())));
        qiColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        qiColumn.setOnEditCommit(event -> {
            RegisterFile.FloatingRegister register = event.getRowValue();
            try {
                register.setQi(Integer.parseInt(event.getNewValue())); // Set the Qi value
            } catch (NumberFormatException e) {
                // Handle invalid input (e.g., show an alert)
                register.setQi(register.getQi()); // Reset to previous value if invalid input
            }
        });

        // Value column (editable)
        TableColumn<RegisterFile.FloatingRegister, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getValue())));
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(event -> {
            RegisterFile.FloatingRegister register = event.getRowValue();
            try {
                register.setValue(Double.parseDouble(event.getNewValue())); // Set the Value
            } catch (NumberFormatException e) {
                // Handle invalid input (e.g., show an alert)
                register.setValue(register.getValue()); // Reset to previous value if invalid input
            }
        });

        // Add columns to the table
        table.getColumns().addAll(nameColumn, qiColumn, valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setEditable(true);
    }

    private void setupInstructionsTable(TableView<Instructon> table) {
        // Define the column for instructions
        TableColumn<Instructon, String> instructionColumn = new TableColumn<>("Instruction");
        instructionColumn.setCellValueFactory(new PropertyValueFactory<>("instruction")); // Bind directly to the String

        // Define the column for current
        TableColumn<Instructon, Boolean> currentColumn = new TableColumn<>("Current");
        currentColumn.setCellValueFactory(new PropertyValueFactory<>("current")); // Bind directly to the boolean property

        // Add the columns to the table
        table.getColumns().addAll(instructionColumn);

        // Optional: Set table properties
        table.setFixedCellSize(25); // Adjust as needed
        double tableHeaderHeight = 28;
        table.setPrefHeight(10 * table.getFixedCellSize() + tableHeaderHeight);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Set row factory to highlight rows based on the current property
        table.setRowFactory(tv -> {
            TableRow<Instructon> row = new TableRow<>();

            // Highlight the row when current is true
            row.itemProperty().addListener((obs, oldItem, newItem) -> {
                if (newItem != null) {
                    // Add a listener to the current property of the Instructon object
                    newItem.currentProperty().addListener((observable, oldValue, newValue) -> {
                        if (newValue) {
                            row.setStyle("-fx-background-color: lightblue;"); // Apply your desired style
                        } else {
                            row.setStyle(""); // Remove the style when false
                        }
                    });

                    // Initialize the style when the item is first added
                    if (newItem.getCurrent()) {
                        row.setStyle("-fx-background-color: lightblue;");
                    } else {
                        row.setStyle("");
                    }
                }
            });

            return row;
        });
    }

    private <T> void customizeTagColumn(TableView<T> table, String prefix) {
        TableColumn<T, String> tagColumn = (TableColumn<T, String>) table.getColumns()
                .stream()
                .filter(col -> col.getText().equals("Tag")) // Match column by header name
                .findFirst()
                .orElse(null);

        if (tagColumn != null) {
            tagColumn.setCellValueFactory(cellData -> {
                int rowIndex = table.getItems().indexOf(cellData.getValue()) + 1; // Get the row index (1-based)
                return new SimpleStringProperty(prefix + rowIndex); // Format with prefix + rowIndex
            });
        }
    }

    private void populateTableAdd(TableView<Tomasulo.ReservationStation> table,ObservableList<Tomasulo.ReservationStation> reservationStations) {
        table.setItems(reservationStations);
        customizeTagColumn(table, "A");


    }
    private void populateTableMultiply(TableView<Tomasulo.ReservationStation> table, ObservableList<Tomasulo.ReservationStation> reservationStations) {
        table.setItems(reservationStations);
        customizeTagColumn(table, "M");

    }
    private void populateTableLoad(TableView<Tomasulo.LoadBuffer> table,  ObservableList<Tomasulo.LoadBuffer> buffers) {
        table.setItems(buffers);
        customizeTagColumn(table, "L");

    }
    private void populateTableStore(TableView<Tomasulo.StoreBuffer> table, ObservableList<Tomasulo.StoreBuffer> buffers) {
        table.setItems(buffers);
        customizeTagColumn(table, "S");

    }

    private void populateTableImmediate(TableView<Tomasulo.IntegerReservationStation> table, ObservableList<Tomasulo.IntegerReservationStation> reservationStations) {
        table.setItems(reservationStations);
        customizeTagColumn(table, "I");
    }

    private void populateIntegerRegistersTable(TableView<RegisterFile.IntegerRegister> table, RegisterFile registerFile) {
        ObservableList<RegisterFile.IntegerRegister> data = FXCollections.observableArrayList();
        data.addAll(RegisterFile.integerRegisters);
        table.setItems(data);
    }

    private void populateFloatingRegistersTable(TableView<RegisterFile.FloatingRegister> table, RegisterFile registerFile) {
        ObservableList<RegisterFile.FloatingRegister> data = FXCollections.observableArrayList();
        data.addAll(RegisterFile.floatingRegisters);
        table.setItems(data);
    }
    private void populateTableBranch(TableView<Tomasulo.BranchStation> table, ObservableList<Tomasulo.BranchStation> branchStations) {
        table.setItems(branchStations);
        customizeTagColumn(table, "B");
    }

    private void populateInstructionsTable(TableView<Instructon> table) {
        // Fetch the instructions from Tomasulo
        try {
            tomasulo.setupInstructions();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ObservableList<Instructon> instructionsList = FXCollections.observableArrayList(Tomasulo.getInstructions());

        // Set the items in the table
        table.setItems(instructionsList);
    }

    private VBox putAddStationBox(){
        // AddReservation table
        TableView<Tomasulo.ReservationStation> addStationTable = new TableView<>();
        setupTable(addStationTable);
        Label addStationLabel = new Label("Add station");
        VBox addStationBox = new VBox(10, addStationLabel, addStationTable);
        addStationBox.setPrefWidth(300);
        populateTableAdd(addStationTable, Tomasulo.addReservationStations);
        return addStationBox;
    }
    private VBox putMultiplyStationBox(){
        // MultiplyReservation table
        TableView<Tomasulo.ReservationStation> multiplyStationTable = new TableView<>();
        setupTable(multiplyStationTable);
        Label multiplyStationLabel = new Label("Multiplication station");
        VBox multiplyStationBox = new VBox(10, multiplyStationLabel, multiplyStationTable);
        multiplyStationBox.setPrefWidth(300);
        populateTableMultiply(multiplyStationTable, Tomasulo.multiplyReservationStations);
        return multiplyStationBox;
    }
    private VBox putImmediateStationBox(){
        // ImmediateReservation table
        TableView<Tomasulo.IntegerReservationStation> immediateStationTable = new TableView<>();
        setupImmediateTable(immediateStationTable);
        Label immediateStationLabel = new Label("Immediate station");
        VBox immediateStationBox = new VBox(10, immediateStationLabel, immediateStationTable);
        immediateStationBox.setPrefWidth(400);
        populateTableImmediate(immediateStationTable, Tomasulo.integerReservationStations);
        return immediateStationBox;
    }

    private VBox putStoreStationBox(){
        // StoreReservation table
        TableView<Tomasulo.StoreBuffer> storeStationTable = new TableView<>();
        setupTableStore(storeStationTable);
        Label storeStationLabel = new Label("Store Buffer");
        VBox storeStationBox = new VBox(10, storeStationLabel, storeStationTable);
        storeStationBox.setPrefWidth(250);
        populateTableStore(storeStationTable, Tomasulo.storeBuffers);
        return storeStationBox;
    }

    private VBox putLoadStationBox(){
        // LoadReservation table
        TableView<Tomasulo.LoadBuffer> loadStationTable = new TableView<>();
        setupTableLoad(loadStationTable);
        Label loadStationLabel = new Label("Load Buffer");
        VBox loadStationBox = new VBox(10, loadStationLabel, loadStationTable);
        loadStationBox.setPrefWidth(150);
        populateTableLoad(loadStationTable, Tomasulo.loadBuffers);
        return loadStationBox;
    }


    private VBox putIntegerRegistersBox(){
        // Integer Registers table
        TableView<RegisterFile.IntegerRegister> integerTable = new TableView<>();
        setupIntegerRegistersTable(integerTable);
        populateIntegerRegistersTable(integerTable, Tomasulo.registerFile);
        Label integerLabel = new Label("Integer Registers");
        VBox integerBox = new VBox(10, integerLabel, integerTable);
        integerBox.setPrefWidth(300); // Adjust width if needed
        return integerBox;
    }

    private VBox putFloatingRegistersBox(){
        // Floating Registers table
        TableView<RegisterFile.FloatingRegister> floatingTable = new TableView<>();
        setupFloatingRegistersTable(floatingTable);
        populateFloatingRegistersTable(floatingTable, Tomasulo.registerFile);
        Label floatingLabel = new Label("Floating Registers");
        VBox floatingBox = new VBox(10, floatingLabel, floatingTable);
        floatingBox.setPrefWidth(300); // Adjust width if needed
        return floatingBox;
    }

    private VBox putBranchStationBox() {
        // BranchStation table
        TableView<Tomasulo.BranchStation> branchStationTable = new TableView<>();
        setupTableBranch(branchStationTable); // Setup table with branch-specific columns
        Label branchStationLabel = new Label("Branch station");
        VBox branchStationBox = new VBox(10, branchStationLabel, branchStationTable);
        branchStationBox.setPrefWidth(300);
        populateTableBranch(branchStationTable, Tomasulo.branchStation); // Populate the table with branch station data
        return branchStationBox;
    }
    private HBox putEditableIntegerRegistersBox(){
        // Integer Registers table
        TableView<RegisterFile.IntegerRegister> integerTable = new TableView<>();
        setupEditableIntegerRegistersTable(integerTable);
        populateIntegerRegistersTable(integerTable, Tomasulo.registerFile);
        Label integerLabel = new Label("Integer");
        HBox integerBox = new HBox(5, integerLabel, integerTable);
        integerBox.setPrefWidth(300); // Adjust width if needed
        return integerBox;
    }

    private HBox putEditableFloatingRegistersBox(){
        // Floating Registers table
        TableView<RegisterFile.FloatingRegister> floatingTable = new TableView<>();
        setupEditableFloatingRegistersTable(floatingTable);
        populateFloatingRegistersTable(floatingTable, Tomasulo.registerFile);
        Label floatingLabel = new Label("Floating ");
        HBox floatingBox = new HBox(5, floatingLabel, floatingTable);
        floatingBox.setPrefWidth(300); // Adjust width if needed
        return floatingBox;
    }

    private VBox putInstructionsBox() {
        // Create the table
        TableView<Instructon> instructionsTable = new TableView<>();

        // Set up the table
        setupInstructionsTable(instructionsTable);

        // Populate the table with instructions from Tomasulo
        populateInstructionsTable(instructionsTable);


        //  Label instructionsLabel = new Label("Instructions");


        VBox instructionsBox = new VBox(10,instructionsTable);
        instructionsBox.setPrefWidth(400);

        return instructionsBox;
    }

    private HBox takeInputs() {
        // Create the input fields for each variable, initially empty
        TextField addReservationStationsField = new TextField("");
        TextField multiplyReservationStationsField = new TextField("");
        TextField immediatesField = new TextField("");
        TextField loadBuffersField = new TextField("");
        TextField storeBuffersField = new TextField("");
        TextField blockSizeField = new TextField("");
        TextField cacheSizeField = new TextField("");




        TextField addReservationStationExecutionTimeField = new TextField("");
        TextField addImmReservationStationExecutionTimeField = new TextField("");
        TextField subReservationStationExecutionTimeField = new TextField("");
        TextField subImmReservationStationExecutionTimeField = new TextField("");
        TextField multiplyReservationStationExecutionTimeField = new TextField("");
        TextField divideReservationStationExecutionTimeField = new TextField("");
        TextField loadBufferExecutionTimeField = new TextField("");
        TextField storeBufferExecutionTimeField = new TextField("");

        // Labels for sizes
        Label addReservationStationsLabel = new Label("Add Station Size:");
        Label multiplyReservationStationsLabel = new Label("Multiplication Station Size:");
        Label immediatesLabel = new Label("Immediate Station Size:");
        Label loadBuffersLabel = new Label("Load Buffer Size:");
        Label storeBuffersLabel = new Label("Store Buffer Size:");
        Label blockSizeLabel = new Label("Block Size:");
        Label cacheSizeLabel = new Label("Cache Size:");


        Label addReservationStationExecutionTimeLabel = new Label("Add Execution Time:");
        Label addImmReservationStationExecutionTimeLabel = new Label("Add Immediate Execution Time:");
        Label subReservationStationExecutionTimeLabel = new Label("Subtraction Execution Time:");
        Label subImmReservationStationExecutionTimeLabel = new Label("Subtraction Immediate ExecutionTime:");
        Label multiplyReservationStationExecutionTimeLabel = new Label("Multiplication Execution Time:");
        Label divideReservationStationExecutionTimeLabel = new Label("Division Execution Time:");
        Label loadBufferExecutionTimeLabel = new Label("Load Execution Time:");
        Label storeBufferExecutionTimeLabel = new Label("Store Execution Time:");

        // VBox for sizes
        VBox sizesBox = new VBox(10,
                addReservationStationsLabel, addReservationStationsField,
                multiplyReservationStationsLabel, multiplyReservationStationsField,
                immediatesLabel, immediatesField,
                loadBuffersLabel, loadBuffersField,
                storeBuffersLabel, storeBuffersField,
                blockSizeLabel, blockSizeField,
                cacheSizeLabel, cacheSizeField

        );

        // VBox for execution times
        VBox executionTimesBox = new VBox(10,
                addReservationStationExecutionTimeLabel, addReservationStationExecutionTimeField,
                addImmReservationStationExecutionTimeLabel, addImmReservationStationExecutionTimeField,
                subReservationStationExecutionTimeLabel, subReservationStationExecutionTimeField,
                subImmReservationStationExecutionTimeLabel, subImmReservationStationExecutionTimeField,
                multiplyReservationStationExecutionTimeLabel, multiplyReservationStationExecutionTimeField,
                divideReservationStationExecutionTimeLabel, divideReservationStationExecutionTimeField,
                loadBufferExecutionTimeLabel, loadBufferExecutionTimeField,
                storeBufferExecutionTimeLabel, storeBufferExecutionTimeField
        );

        HBox editableIntegerBox = putEditableIntegerRegistersBox();
        HBox editableFloatingBox = putEditableFloatingRegistersBox();
        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                Tomasulo.addReservationStationsSize = parseField(addReservationStationsField, Tomasulo.addReservationStationsSize);
                Tomasulo.multiplyReservationStationsSize = parseField(multiplyReservationStationsField, Tomasulo.multiplyReservationStationsSize);
                Tomasulo.immediateReservationStationSize = parseField(immediatesField, Tomasulo.immediateReservationStationSize);
                Tomasulo.loadBuffersSize = parseField(loadBuffersField, Tomasulo.loadBuffersSize);
                Tomasulo.storeBuffersSize = parseField(storeBuffersField, Tomasulo.storeBuffersSize);
                Tomasulo.blockSize = parseField(blockSizeField, Tomasulo.blockSize);
                Tomasulo.cacheSize = parseField(cacheSizeField, Tomasulo.cacheSize);
                Tomasulo.LoadBufferExecutionTime = parseField(loadBufferExecutionTimeField, Tomasulo.LoadBufferExecutionTime);
                Tomasulo.StoreBufferExecutionTime = parseField(storeBufferExecutionTimeField, Tomasulo.StoreBufferExecutionTime);
                Tomasulo.AddReservationStationExecutionTime = parseField(addReservationStationExecutionTimeField, Tomasulo.AddReservationStationExecutionTime);
                Tomasulo.AddImmReservationStationExecutionTime = parseField(addImmReservationStationExecutionTimeField, Tomasulo.AddImmReservationStationExecutionTime);
                Tomasulo.SubReservationStationExecutionTime = parseField(subReservationStationExecutionTimeField, Tomasulo.SubReservationStationExecutionTime);
                Tomasulo.SubImmReservationStationExecutionTime = parseField(subImmReservationStationExecutionTimeField, Tomasulo.SubImmReservationStationExecutionTime);
                Tomasulo.MultiplyReservationStationExecutionTime = parseField(multiplyReservationStationExecutionTimeField, Tomasulo.MultiplyReservationStationExecutionTime);
                Tomasulo.DivideReservationStationExecutionTime = parseField(divideReservationStationExecutionTimeField, Tomasulo.DivideReservationStationExecutionTime);
                RegisterFile.IntegerRegister[] integerRegisters = getIntegerRegistersFromTable();
                RegisterFile.FloatingRegister[] floatingRegisters = getFloatingRegistersFromTable();
                RegisterFile registerFile = new RegisterFile(floatingRegisters, integerRegisters);
                tomasulo.setRegisterFile(registerFile);
                tomasulo.init();

                System.out.println("Backend variables updated successfully.");
            } catch (Exception ex) {
                System.out.println("Error updating backend variables: " + ex.getMessage());
            }
        });

        // Parent VBox to hold both sizes and execution times
        HBox mainBox = new HBox(20,
                sizesBox,
                executionTimesBox,
                editableIntegerBox,
                editableFloatingBox,
                submitButton
        );
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(15));


        return mainBox;
    }
    private RegisterFile.IntegerRegister[] getIntegerRegistersFromTable() {
        List<RegisterFile.IntegerRegister> list = new ArrayList<>();
        for (int i = 0; i < integerRegisterTable.getItems().size(); i++) {
            RegisterFile.IntegerRegister register = integerRegisterTable.getItems().get(i);
            list.add(register);
        }
        return list.toArray(new RegisterFile.IntegerRegister[0]);
    }

    // Method to gather FloatingRegisters from the table
    private RegisterFile.FloatingRegister[] getFloatingRegistersFromTable() {
        List<RegisterFile.FloatingRegister> list = new ArrayList<>();
        for (int i = 0; i < floatingRegisterTable.getItems().size(); i++) {
            RegisterFile.FloatingRegister register = floatingRegisterTable.getItems().get(i);
            list.add(register);
        }
        return list.toArray(new RegisterFile.FloatingRegister[0]);
    }


    // Helper method to parse the field and return the value from the backend if the field is empty
    private int parseField(TextField field, int defaultValue) {
        String text = field.getText();
        if (text.isEmpty()) {
            return defaultValue;  // Use the backend's default value if the field is empty
        }
        try {
            return Integer.parseInt(text);  // Parse the value from the field
        } catch (NumberFormatException e) {
            return defaultValue;  // If invalid input, return the backend's default value
        }
    }



    @Override
    public void start(Stage primaryStage) {
        // Create Tomasulo instance to access station sizes

        // Create the welcome scene layout with input fields and a start button
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to Tomasulo Simulator");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Use the takeInputs method to get all input fields and labels
        HBox inputsBox = takeInputs();
        VBox instructionsBox = putInstructionsBox();

        // Start button to transition to the main scene
        Button startButton = new Button("Start Simulation");
        startButton.setStyle("-fx-font-size: 16px;");
        startButton.setOnAction(e -> {
            new Thread(() -> {
                Platform.runLater(() -> {
                    try {
                        tomasulo.init(); // method to pass the cycle
                    } catch (Exception ex) {
                        System.out.println("Error during cycle execution: " + ex.getMessage());
                    }
                });
            }).start();
        });

        // Add welcome label, inputs and start button to layout
        welcomeLayout.getChildren().addAll(
                welcomeLabel,
                inputsBox,
                startButton
        );

        // Create the welcome scene
        Scene welcomeScene = new Scene(welcomeLayout);

        // Main simulation scene setup (existing layout code)
        VBox logAreaBox = new VBox(20);
        TextArea logArea = new TextArea();
        logArea.setEditable(false);
        logAreaBox.getChildren().add(logArea);

        VBox addStationBox = putAddStationBox();
        VBox multiplyStationBox = putMultiplyStationBox();
        VBox storeStationBox = putStoreStationBox();
        VBox loadStationBox = putLoadStationBox();
        VBox immediateStationBox = putImmediateStationBox();
       
      


        VBox integerBox = putIntegerRegistersBox();
        VBox floatingBox = putFloatingRegistersBox();
        VBox branchStationBox = putBranchStationBox();

        Label clockCycleLabel = new Label();
        clockCycleLabel.textProperty().bind(tomasulo.clockCycleProperty().asString("Clock Cycle: %d"));
        clockCycleLabel.setStyle("-fx-font-size: 16px;");
        VBox clockCycleBox = new VBox(10, clockCycleLabel);
        clockCycleBox.setPrefWidth(150);

        VBox branchAndClockBox = new VBox(20, branchStationBox, clockCycleBox);


        // HBox to place Integer and Floating Registers next to each other
        HBox registersBox = new HBox(20, integerBox, floatingBox,immediateStationBox,branchAndClockBox);

        // Layout to align tables horizontally (without Integer Registers)
        HBox tablesBox = new HBox(40, addStationBox, multiplyStationBox, storeStationBox, loadStationBox);
        HBox.setHgrow(addStationBox, Priority.ALWAYS);
        HBox.setHgrow(multiplyStationBox, Priority.ALWAYS);
        HBox.setHgrow(storeStationBox, Priority.ALWAYS);
        HBox.setHgrow(loadStationBox, Priority.ALWAYS);
        

        // Next Cycle Button
        Button nextCycleButton = new Button("Next Cycle");
        nextCycleButton.setOnAction(e -> {
            tomasulo.setUpdateLogCallback(logArea::appendText);
            new Thread(() -> {
                Platform.runLater(() -> {
                    try {
                        tomasulo.executeCycle(); // method to pass the cycle
                    } catch (Exception ex) {
                        logArea.appendText("Error during cycle execution: " + ex.getMessage() + "\n");
                    }
                });
            }).start();
        });
     // Create instructionsBox and set its height
        instructionsBox.setPrefWidth(400);
        instructionsBox.setPrefHeight(400); // Adjust this height as needed

        // Create logAreaBox and set its height
        logAreaBox.setPrefHeight(400); // Match the height of instructionsBox

        // Add to instructionsLogBox
        HBox instructionsLogBox = new HBox(20, instructionsBox, logAreaBox);
        instructionsLogBox.setPrefWidth(900); // Adjust the total width as needed
        instructionsLogBox.setPadding(new Insets(10));
        HBox.setHgrow(instructionsBox, Priority.ALWAYS);
        HBox.setHgrow(logAreaBox, Priority.ALWAYS);
        // Main simulation layout
        VBox layout = new VBox(20,instructionsLogBox, tablesBox, registersBox, nextCycleButton);
        layout.setPadding(new Insets(15));
        VBox.setVgrow(tablesBox, Priority.ALWAYS);
        VBox.setVgrow(logArea, Priority.ALWAYS);

        Scene mainScene = new Scene(layout);

        // Start button action to switch to the main scene
        startButton.setOnAction(e -> {
            primaryStage.setScene(mainScene);
        });

        // Set the initial scene to the welcome scene
        primaryStage.setScene(welcomeScene);
        primaryStage.setTitle("Tomasulo Simulator");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }





    public static void main(String[] args) throws IOException {
    	Memory.storeDoubleWord(0, 1152921504606846950L);
        launch();
    }

}
