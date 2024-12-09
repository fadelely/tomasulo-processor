module org.openjfx {
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires javafx.controls;  // Add this line to include javafx.controls module
    exports processor.tomasulo;
}