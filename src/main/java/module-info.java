module com.example.analyzer_text {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.logging.log4j;
    requires org.apache.commons.lang3;
    requires org.apache.poi.scratchpad;
    requires org.apache.poi.ooxml;

    opens com.example.analyzer_text to javafx.fxml;
    exports com.example.analyzer_text;
}