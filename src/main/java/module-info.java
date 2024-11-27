module org.openjfx {
    requires javafx.graphics;
    requires javafx.controls;  // Add this line to include javafx.controls module
    exports processor.tomasulo;
}