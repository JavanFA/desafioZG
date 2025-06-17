module com.desafiozg {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.graphics;
    requires javafx.media;
    
    opens com.desafiozg to javafx.fxml;
    exports com.desafiozg;
}