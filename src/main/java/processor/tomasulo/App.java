package processor.tomasulo;

import java.io.IOException;
import java.util.function.Function;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import java.util.Arrays;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;


public class App extends Application{
    private final ObservableList<Tomasulo.ReservationStation> addStationData = FXCollections.observableArrayList();
    private final ObservableList<Tomasulo.ReservationStation> multiplyStationData = FXCollections.observableArrayList();
    private final ObservableList<Tomasulo.LoadBuffer> loadBufferData = FXCollections.observableArrayList();
    private final ObservableList<Tomasulo.StoreBuffer> storeBufferData = FXCollections.observableArrayList();


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

        TableColumn<Tomasulo.LoadBuffer, String> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<Tomasulo.LoadBuffer, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.LoadBuffer, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private void setupTableStore(TableView<Tomasulo.StoreBuffer> table) {

        TableColumn<Tomasulo.StoreBuffer, String> tagColumn = new TableColumn<>("Tag");
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));

        TableColumn<Tomasulo.StoreBuffer, Boolean> busyColumn = new TableColumn<>("Busy");
        busyColumn.setCellValueFactory(new PropertyValueFactory<>("busy"));

        TableColumn<Tomasulo.StoreBuffer, String> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        TableColumn<Tomasulo.StoreBuffer, String> vColumn = new TableColumn<>("V");
        vColumn.setCellValueFactory(new PropertyValueFactory<>("v"));

        TableColumn<Tomasulo.StoreBuffer, String> qColumn = new TableColumn<>("Q");
        qColumn.setCellValueFactory(new PropertyValueFactory<>("q"));

        // Add columns to the table
        table.getColumns().addAll(tagColumn,busyColumn, addressColumn, vColumn, qColumn);

        // Set table properties (optional)
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }
    private <T> void customizeTagColumn(TableView<T> table, String prefix, Function<T, Integer> tagExtractor) {
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

    private void populateTableAdd(TableView<Tomasulo.ReservationStation> table,Tomasulo.ReservationStation[] reservationStations, int size) {
        ObservableList<Tomasulo.ReservationStation> data = FXCollections.observableArrayList(reservationStations);
        table.setItems(data);
        customizeTagColumn(table, "A", Tomasulo.ReservationStation::getTag);


    }
    private void populateTableMultiply(TableView<Tomasulo.ReservationStation> table,Tomasulo.ReservationStation[] reservationStations, int size) {
        ObservableList<Tomasulo.ReservationStation> data = FXCollections.observableArrayList(reservationStations);
        table.setItems(data);
        customizeTagColumn(table, "M", Tomasulo.ReservationStation::getTag);

    }
    private void populateTableLoad(TableView<Tomasulo.LoadBuffer> table, Tomasulo.LoadBuffer[] buffers, int size) {
        ObservableList<Tomasulo.LoadBuffer> data = FXCollections.observableArrayList(buffers);
        table.setItems(data);
        customizeTagColumn(table, "L", Tomasulo.LoadBuffer::getTag);

    }
    private void populateTableStore(TableView<Tomasulo.StoreBuffer> table, Tomasulo.StoreBuffer[] buffers, int size) {
        ObservableList<Tomasulo.StoreBuffer> data = FXCollections.observableArrayList(buffers);
        table.setItems(data);
        customizeTagColumn(table, "S", Tomasulo.StoreBuffer::getTag);

    }

    private void syncFrontendWithBackend() {
        Platform.runLater(() -> {
            addStationData.setAll(Arrays.asList(Tomasulo.addReservationStations));
            multiplyStationData.setAll(Arrays.asList(Tomasulo.multiplyReservationStations));
            loadBufferData.setAll(Arrays.asList(Tomasulo.loadBuffers));
            storeBufferData.setAll(Arrays.asList(Tomasulo.storeBuffers));
        });
    }
    private void startDataSync() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.5), e -> syncFrontendWithBackend()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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
        VBox addStationBox = new VBox(10, addStationLabel, addStationTable); // Increased spacing
        addStationBox.setPrefWidth(300); // Set preferred width for better visibility
        populateTableAdd(addStationTable, Tomasulo.addReservationStations, tomasulo.addReservationStationsSize);

        // MultiplyReservation table
        TableView<Tomasulo.ReservationStation> multiplyStationTable = new TableView<>();
        setupTable(multiplyStationTable);
        Label multiplyStationLabel = new Label("MultiplyReservation");
        VBox multiplyStationBox = new VBox(10, multiplyStationLabel, multiplyStationTable);
        multiplyStationBox.setPrefWidth(300);
        populateTableMultiply(multiplyStationTable, Tomasulo.multiplyReservationStations, tomasulo.multiplyReservationStationsSize);


        // StoreReservation table
        TableView<Tomasulo.StoreBuffer> storeStationTable = new TableView<>();
        setupTableStore(storeStationTable);
        Label storeStationLabel = new Label("StoreReservation");
        VBox storeStationBox = new VBox(10, storeStationLabel, storeStationTable);
        storeStationBox.setPrefWidth(250);
        populateTableStore(storeStationTable, Tomasulo.storeBuffers, tomasulo.storeBuffersSize);


        // LoadReservation table
        TableView<Tomasulo.LoadBuffer> loadStationTable = new TableView<>();
        setupTableLoad(loadStationTable);
        Label loadStationLabel = new Label("LoadReservation");
        VBox loadStationBox = new VBox(10, loadStationLabel, loadStationTable);
        loadStationBox.setPrefWidth(150);
        populateTableLoad(loadStationTable, Tomasulo.loadBuffers, tomasulo.loadBuffersSize);



        // Layout to align tables horizontally
        HBox tablesBox = new HBox(20, addStationBox, multiplyStationBox, storeStationBox,loadStationBox); // Increased spacing
        HBox.setHgrow(addStationBox, Priority.ALWAYS);
        HBox.setHgrow(multiplyStationBox, Priority.ALWAYS);
        HBox.setHgrow(storeStationBox, Priority.ALWAYS);
        HBox.setHgrow(loadStationBox, Priority.ALWAYS);

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            tomasulo.setUpdateLogCallback(logArea::appendText);
            new Thread(() -> {
                try {
                    tomasulo.startExecution();
                } catch (IOException ex) {
                    Platform.runLater(() -> logArea.appendText("Error: " + ex.getMessage() + "\n"));
                }
            }).start();
        });

        // Main layout
        VBox layout = new VBox(20, logArea, tablesBox, startButton); // Adjusted spacing
        layout.setPadding(new Insets(15));
        VBox.setVgrow(tablesBox, Priority.ALWAYS);
        VBox.setVgrow(logArea, Priority.ALWAYS);

        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomasulo Simulator");
        primaryStage.setFullScreen(true); // Set to fullscreen
        primaryStage.show();
    }





    public static void main(String[] args) throws IOException {
        Tomasulo tomasolu = new Tomasulo();
        tomasolu.startExecution();
        launch();


    }

}
