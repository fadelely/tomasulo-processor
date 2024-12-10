package processor.tomasulo;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.security.auth.callback.LanguageCallback;


@SuppressWarnings("unchecked")
public class App extends Application{

    public static Tomasulo tomasulo=new Tomasulo();

    public static int cellSize = 20;

    public static int maxRows = 10;


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

        TableColumn<Tomasulo.ReservationStation, Double> qjColumn = new TableColumn<>("Qj");
        qjColumn.setCellValueFactory(new PropertyValueFactory<>("qj"));

        TableColumn<Tomasulo.ReservationStation, Double> qkColumn = new TableColumn<>("Qk");
        qkColumn.setCellValueFactory(new PropertyValueFactory<>("qk"));

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

        TableColumn<Tomasulo.LoadBuffer, Double> executionColumn = new TableColumn<>("Cycles");
        executionColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn,executionColumn);

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

        TableColumn<Tomasulo.StoreBuffer, Number> vColumn = new TableColumn<>("V");
        vColumn.setCellValueFactory(new PropertyValueFactory<>("V"));

        TableColumn<Tomasulo.StoreBuffer, Number> qColumn = new TableColumn<>("Q");
        qColumn.setCellValueFactory(new PropertyValueFactory<>("Q"));

        TableColumn<Tomasulo.StoreBuffer, Double> executionColumn = new TableColumn<>("Cycles");
        executionColumn.setCellValueFactory(new PropertyValueFactory<>("executionTime"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn, vColumn, qColumn,executionColumn);

        // Set fixed cell size
        table.setFixedCellSize(cellSize); // Adjust as needed
        double tableHeaderHeight = 28; // Approximate header height
        table.setPrefHeight((maxRows * table.getFixedCellSize()) + tableHeaderHeight);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void setupIntegerRegistersTable(TableView<RegisterFile.IntegerRegister> table) {

        TableColumn<RegisterFile.IntegerRegister, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));

        TableColumn<RegisterFile.IntegerRegister, Number> qiColumn = new TableColumn<>("Qi");
        qiColumn.setCellValueFactory(new PropertyValueFactory<>("qi"));

        TableColumn<RegisterFile.IntegerRegister, Number> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        table.getColumns().addAll(nameColumn,qiColumn, valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void setupFloatingRegistersTable(TableView<RegisterFile.FloatingRegister> table) {
        TableColumn<RegisterFile.FloatingRegister, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("registerName"));

        TableColumn<RegisterFile.FloatingRegister, Number> qiColumn = new TableColumn<>("Qi");
        qiColumn.setCellValueFactory(new PropertyValueFactory<>("qi"));

        TableColumn<RegisterFile.FloatingRegister, Number> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

        table.getColumns().addAll(nameColumn,qiColumn, valueColumn);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

    private void populateIntegerRegistersTable(TableView<RegisterFile.IntegerRegister> table, RegisterFile registerFile) {
        ObservableList<RegisterFile.IntegerRegister> data = FXCollections.observableArrayList();
        data.addAll(registerFile.integerRegisters);
        table.setItems(data);
    }

    private void populateFloatingRegistersTable(TableView<RegisterFile.FloatingRegister> table, RegisterFile registerFile) {
        ObservableList<RegisterFile.FloatingRegister> data = FXCollections.observableArrayList();
        data.addAll(registerFile.floatingRegisters);
        table.setItems(data);
    }

    private VBox putAddStationBox(){
        // AddReservation table
        TableView<Tomasulo.ReservationStation> addStationTable = new TableView<>();
        setupTable(addStationTable);
        Label addStationLabel = new Label("AddReservation");
        VBox addStationBox = new VBox(10, addStationLabel, addStationTable);
        addStationBox.setPrefWidth(300);
        populateTableAdd(addStationTable, Tomasulo.addReservationStations);
        return addStationBox;
    }
    private VBox putMultiplyStationBox(){
        // MultiplyReservation table
        TableView<Tomasulo.ReservationStation> multiplyStationTable = new TableView<>();
        setupTable(multiplyStationTable);
        Label multiplyStationLabel = new Label("MultiplyReservation");
        VBox multiplyStationBox = new VBox(10, multiplyStationLabel, multiplyStationTable);
        multiplyStationBox.setPrefWidth(300);
        populateTableMultiply(multiplyStationTable, Tomasulo.multiplyReservationStations);
        return multiplyStationBox;
    }

    private VBox putStoreStationBox(){
        // StoreReservation table
        TableView<Tomasulo.StoreBuffer> storeStationTable = new TableView<>();
        setupTableStore(storeStationTable);
        Label storeStationLabel = new Label("StoreReservation");
        VBox storeStationBox = new VBox(10, storeStationLabel, storeStationTable);
        storeStationBox.setPrefWidth(250);
        populateTableStore(storeStationTable, Tomasulo.storeBuffers);
        return storeStationBox;
    }

    private VBox putLoadStationBox(){
        // LoadReservation table
        TableView<Tomasulo.LoadBuffer> loadStationTable = new TableView<>();
        setupTableLoad(loadStationTable);
        Label loadStationLabel = new Label("LoadReservation");
        VBox loadStationBox = new VBox(10, loadStationLabel, loadStationTable);
        loadStationBox.setPrefWidth(150);
        populateTableLoad(loadStationTable, Tomasulo.loadBuffers);
        return loadStationBox;
    }

    private VBox putIntegerRegistersBox(){
        // Integer Registers table
        TableView<RegisterFile.IntegerRegister> integerTable = new TableView<>();
        setupIntegerRegistersTable(integerTable);
        populateIntegerRegistersTable(integerTable, tomasulo.registerFile);
        Label integerLabel = new Label("Integer Registers");
        VBox integerBox = new VBox(10, integerLabel, integerTable);
        integerBox.setPrefWidth(300); // Adjust width if needed
        return integerBox;
    }

    private VBox putFloatingRegistersBox(){
        // Floating Registers table
        TableView<RegisterFile.FloatingRegister> floatingTable = new TableView<>();
        setupFloatingRegistersTable(floatingTable);
        populateFloatingRegistersTable(floatingTable, tomasulo.registerFile);
        Label floatingLabel = new Label("Floating Registers");
        VBox floatingBox = new VBox(10, floatingLabel, floatingTable);
        floatingBox.setPrefWidth(300); // Adjust width if needed
        return floatingBox;
    }


    private HBox takeInputs() {
        // Create the input fields for each variable, initially empty
        TextField addReservationStationsField = new TextField("");
        TextField multiplyReservationStationsField = new TextField("");
        TextField loadBuffersField = new TextField("");
        TextField storeBuffersField = new TextField("");
        TextField loadBufferExecutionTimeField = new TextField("");
        TextField storeBufferExecutionTimeField = new TextField("");
        TextField addReservationStationExecutionTimeField = new TextField("");
        TextField addImmReservationStationExecutionTimeField = new TextField("");
        TextField subReservationStationExecutionTimeField = new TextField("");
        TextField subImmReservationStationExecutionTimeField = new TextField("");
        TextField multiplyReservationStationExecutionTimeField = new TextField("");
        TextField divideReservationStationExecutionTimeField = new TextField("");

        // Labels for sizes
        Label addReservationStationsLabel = new Label("Add Reservation Stations Size:");
        Label multiplyReservationStationsLabel = new Label("Multiply Reservation Stations Size:");
        Label loadBuffersLabel = new Label("Load Buffers Size:");
        Label storeBuffersLabel = new Label("Store Buffers Size:");

        // Labels for execution times
        Label loadBufferExecutionTimeLabel = new Label("Load Buffer Execution Time:");
        Label storeBufferExecutionTimeLabel = new Label("Store Buffer Execution Time:");
        Label addReservationStationExecutionTimeLabel = new Label("Add Reservation Station Execution Time:");
        Label addImmReservationStationExecutionTimeLabel = new Label("Add Reservation Station Immediate Time:");
        Label subReservationStationExecutionTimeLabel = new Label("Sub Reservation Station Execution Time:");
        Label subImmReservationStationExecutionTimeLabel = new Label("Sub Reservation Station Immediate Time:");
        Label multiplyReservationStationExecutionTimeLabel = new Label("Multiply Reservation Station Execution Time:");
        Label divideReservationStationExecutionTimeLabel = new Label("Divide Reservation Station Execution Time:");

        // VBox for sizes
        VBox sizesBox = new VBox(10,
                addReservationStationsLabel, addReservationStationsField,
                multiplyReservationStationsLabel, multiplyReservationStationsField,
                loadBuffersLabel, loadBuffersField,
                storeBuffersLabel, storeBuffersField
        );

        // VBox for execution times
        VBox executionTimesBox = new VBox(10,
                loadBufferExecutionTimeLabel, loadBufferExecutionTimeField,
                storeBufferExecutionTimeLabel, storeBufferExecutionTimeField,
                addReservationStationExecutionTimeLabel, addReservationStationExecutionTimeField,
                addImmReservationStationExecutionTimeLabel, addImmReservationStationExecutionTimeField,
                subReservationStationExecutionTimeLabel, subReservationStationExecutionTimeField,
                subImmReservationStationExecutionTimeLabel, subImmReservationStationExecutionTimeField,
                multiplyReservationStationExecutionTimeLabel, multiplyReservationStationExecutionTimeField,
                divideReservationStationExecutionTimeLabel, divideReservationStationExecutionTimeField
        );

        // Submit button
        Button submitButton = new Button("Submit");
        submitButton.setOnAction(e -> {
            try {
                tomasulo.addReservationStationsSize = parseField(addReservationStationsField, tomasulo.addReservationStationsSize);
                tomasulo.multiplyReservationStationsSize = parseField(multiplyReservationStationsField, tomasulo.multiplyReservationStationsSize);
                tomasulo.loadBuffersSize = parseField(loadBuffersField, tomasulo.loadBuffersSize);
                tomasulo.storeBuffersSize = parseField(storeBuffersField, tomasulo.storeBuffersSize);
                tomasulo.LoadBufferExecutionTime = parseField(loadBufferExecutionTimeField, tomasulo.LoadBufferExecutionTime);
                tomasulo.StoreBufferExecutionTime = parseField(storeBufferExecutionTimeField, tomasulo.StoreBufferExecutionTime);
                tomasulo.AddReservationStationExecutionTime = parseField(addReservationStationExecutionTimeField, tomasulo.AddReservationStationExecutionTime);
                tomasulo.AddImmReservationStationExecutionTime = parseField(addImmReservationStationExecutionTimeField, tomasulo.AddImmReservationStationExecutionTime);
                tomasulo.SubReservationStationExecutionTime = parseField(subReservationStationExecutionTimeField, tomasulo.SubReservationStationExecutionTime);
                tomasulo.SubImmReservationStationExecutionTime = parseField(subImmReservationStationExecutionTimeField, tomasulo.SubImmReservationStationExecutionTime);
                tomasulo.MultiplyReservationStationExecutionTime = parseField(multiplyReservationStationExecutionTimeField, tomasulo.MultiplyReservationStationExecutionTime);
                tomasulo.DivideReservationStationExecutionTime = parseField(divideReservationStationExecutionTimeField, tomasulo.DivideReservationStationExecutionTime);

                tomasulo.init();
                System.out.println("Backend variables updated successfully.");
            } catch (Exception ex) {
                System.out.println("Invalid input, please enter valid integers.");
            }
        });

        // Parent VBox to hold both sizes and execution times
        HBox mainBox = new HBox(20,
                sizesBox,
                executionTimesBox,
                submitButton
        );
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(15));
        mainBox.setMaxWidth(600);

        return mainBox;
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
        Tomasulo tomasulo = new Tomasulo();

        // Create the welcome scene layout with input fields and a start button
        VBox welcomeLayout = new VBox(20);
        welcomeLayout.setAlignment(Pos.CENTER);

        Label welcomeLabel = new Label("Welcome to Tomasulo Simulator");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // Use the takeInputs method to get all input fields and labels
        HBox inputsBox = takeInputs();

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

        VBox integerBox = putIntegerRegistersBox();
        VBox floatingBox = putFloatingRegistersBox();

        Label clockCycleLabel = new Label();
        clockCycleLabel.textProperty().bind(tomasulo.clockCycleProperty().asString("Clock Cycle: %d"));
        clockCycleLabel.setStyle("-fx-font-size: 16px;");
        VBox clockCycleBox = new VBox(10, clockCycleLabel);
        clockCycleBox.setPrefWidth(150);


        // HBox to place Integer and Floating Registers next to each other
        HBox registersBox = new HBox(20, integerBox, floatingBox, clockCycleBox);

        // Layout to align tables horizontally (without Integer Registers)
        HBox tablesBox = new HBox(20, addStationBox, multiplyStationBox, storeStationBox, loadStationBox);
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

        // Main simulation layout
        VBox layout = new VBox(20, logArea, tablesBox, registersBox, nextCycleButton);
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

//        tomasulo.init();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
//        tomasulo.executeCycle();
        Memory.storeSingle(0, 5);
        Memory.storeSingle(4, 6);
        launch();

    }

}
