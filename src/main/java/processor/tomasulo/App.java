package processor.tomasulo;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class App extends Application{
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

    // Add columns to the table
    table.getColumns().addAll(tagColumn, busyColumn, opcodeColumn, vjColumn, vkColumn);

    // Set table properties (optional)
    table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
}

    @Override
    public void start(Stage primaryStage) {
        TextArea logArea = new TextArea();
        logArea.setEditable(false);

        TableView<Tomasulo.ReservationStation> addStationTable = new TableView<>();
        setupTable(addStationTable); // Setup columns and data binding

        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            Tomasulo tomasulo = new Tomasulo();
            tomasulo.setUpdateLogCallback(logArea::appendText);
            new Thread(() -> {
                try {
                    tomasulo.startExecution();
                } catch (IOException ex) {
                    Platform.runLater(() -> logArea.appendText("Error: " + ex.getMessage() + "\n"));
                }
            }).start();
        });

        VBox layout = new VBox(10, logArea, addStationTable, startButton);
        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Tomasulo Simulator");
        primaryStage.show();
    }


    public static void main(String[] args) throws IOException {
        launch();
    	Tomasulo tomasolu = new Tomasulo();
    	tomasolu.startExecution();
    }

}
