package processor.tomasulo;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



@SuppressWarnings("unchecked")
public class App extends Application{

    public static Tomasulo tomasulo=new Tomasulo();


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

        // Add columns to the table
        table.getColumns().addAll(tagColumn, busyColumn, opcodeColumn, vjColumn, vkColumn, qjColumn, qkColumn);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
	private void setupTableLoad(TableView<Tomasulo.LoadBuffer> table) {

        TableColumn<Tomasulo.LoadBuffer, Number> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());

        TableColumn<Tomasulo.LoadBuffer, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(cellData -> cellData.getValue().busyProperty());

        TableColumn<Tomasulo.LoadBuffer, Number> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void setupTableStore(TableView<Tomasulo.StoreBuffer> table) {

        TableColumn<Tomasulo.StoreBuffer, Number> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(cellData -> cellData.getValue().tagProperty());

        TableColumn<Tomasulo.StoreBuffer, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(cellData->cellData.getValue().busyProperty());

        TableColumn<Tomasulo.StoreBuffer, Number> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(cellData->cellData.getValue().addressProperty());

        TableColumn<Tomasulo.StoreBuffer, Number> vColumn = new TableColumn<>("V");
        vColumn.setCellValueFactory(cellData->cellData.getValue().VProperty());

        TableColumn<Tomasulo.StoreBuffer, Number> qColumn = new TableColumn<>("Q");
        qColumn.setCellValueFactory(cellData->cellData.getValue().QProperty());

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn, vColumn, qColumn);

        // Set table properties (optional)
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



    @Override
    public void start(Stage primaryStage) {
        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        // Create Tomasulo instance to access station sizes
        Tomasulo tomasulo = new Tomasulo();

        // AddReservation table
        TableView<Tomasulo.ReservationStation> addStationTable = new TableView<>();
        setupTable(addStationTable);
        Label addStationLabel = new Label("AddReservation");
        VBox addStationBox = new VBox(10, addStationLabel, addStationTable);
        addStationBox.setPrefWidth(300);
        populateTableAdd(addStationTable, Tomasulo.addReservationStations);

        // MultiplyReservation table
        TableView<Tomasulo.ReservationStation> multiplyStationTable = new TableView<>();
        setupTable(multiplyStationTable);
        Label multiplyStationLabel = new Label("MultiplyReservation");
        VBox multiplyStationBox = new VBox(10, multiplyStationLabel, multiplyStationTable);
        multiplyStationBox.setPrefWidth(300);
        populateTableMultiply(multiplyStationTable, Tomasulo.multiplyReservationStations);

        // StoreReservation table
        TableView<Tomasulo.StoreBuffer> storeStationTable = new TableView<>();
        setupTableStore(storeStationTable);
        Label storeStationLabel = new Label("StoreReservation");
        VBox storeStationBox = new VBox(10, storeStationLabel, storeStationTable);
        storeStationBox.setPrefWidth(250);
        populateTableStore(storeStationTable, Tomasulo.storeBuffers);

        // LoadReservation table
        TableView<Tomasulo.LoadBuffer> loadStationTable = new TableView<>();
        setupTableLoad(loadStationTable);
        Label loadStationLabel = new Label("LoadReservation");
        VBox loadStationBox = new VBox(10, loadStationLabel, loadStationTable);
        loadStationBox.setPrefWidth(150);
        populateTableLoad(loadStationTable, Tomasulo.loadBuffers);

        // Layout to align tables horizontally
        HBox tablesBox = new HBox(20, addStationBox, multiplyStationBox, storeStationBox, loadStationBox);
        HBox.setHgrow(addStationBox, Priority.ALWAYS);
        HBox.setHgrow(multiplyStationBox, Priority.ALWAYS);
        HBox.setHgrow(storeStationBox, Priority.ALWAYS);
        HBox.setHgrow(loadStationBox, Priority.ALWAYS);

        // Start Button
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            tomasulo.setUpdateLogCallback(logArea::appendText);
            new Thread(() -> {
                try {
                    tomasulo.executeCycle();
                } catch (IOException ex) {
                    Platform.runLater(() -> logArea.appendText("Error: " + ex.getMessage() + "\n"));
                }
            }).start();
        });

        // Next Cycle Button
        Button nextCycleButton = new Button("Next Cycle");
        nextCycleButton.setOnAction(e -> {
            tomasulo.setUpdateLogCallback(logArea::appendText);
            new Thread(() -> {
                Platform.runLater(() -> {
                    try {
//                        tomasulo.executeCycle(); // method to pass the cycle
                    } catch (Exception ex) {
                        logArea.appendText("Error during cycle execution: " + ex.getMessage() + "\n");
                    }
                });
            }).start();
        });

        // Main layout
        HBox buttonsBox = new HBox(10, startButton, nextCycleButton);
        VBox layout = new VBox(20, logArea, tablesBox, buttonsBox);
        layout.setPadding(new Insets(15));
        VBox.setVgrow(tablesBox, Priority.ALWAYS);
        VBox.setVgrow(logArea, Priority.ALWAYS);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomasulo Simulator");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {

        tomasulo.init();
        Memory.storeDouble(0, 5);
        Memory.storeDouble(8, 7);
        tomasulo.executeCycle();
        tomasulo.executeCycle();
        tomasulo.executeCycle();
        tomasulo.executeCycle();
        tomasulo.executeCycle();
        tomasulo.executeCycle();
//        launch();
    }

}
