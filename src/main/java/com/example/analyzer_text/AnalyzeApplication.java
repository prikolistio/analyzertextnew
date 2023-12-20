package com.example.analyzer_text;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.List;

/**
 * Класс, наследующийся от Application. Отвечает за запуск javafx приложения
 */
public class AnalyzeApplication extends Application {

    private static Logger log = LogManager.getLogger(AnalyzeApplication.class);

    @Override
    public void start(Stage stage) {
        Dict medicalDict = new Dict("Медицинский", "./testDict/medicalDict.txt");
        Dict cryptoDict = new Dict("Криптография", "./testDict/cryptoDict.txt");
        Dict programmingDict = new Dict("Программирование", "./testDict/programmingDict.txt");
        Dict networkDict = new Dict("Сети", "./testDict/networkDict.txt");
        Dict financeDict = new Dict("Финансы", "./testDict/financeDict.txt");
        Dict historyDict = new Dict("Исторический", "./testDict/historyDict.txt");

        DictUtils dictUtils = new DictUtils(List.of(medicalDict, cryptoDict, programmingDict, networkDict, financeDict, historyDict));

        stage.setTitle("Анализатор текста");

        FileChooser fileChooser = new FileChooser();

        Label buttonLabel = new Label("");

        Button button = new Button("Выбрать файл для анализа");

        Label tableLabel = new Label("Статистика");

        TableView<TableData> table = new TableView<>();
        TableColumn<TableData, String> wordColumn = new TableColumn<>("Слово");
        wordColumn.setCellValueFactory(new PropertyValueFactory<>("word"));
        table.getColumns().add(wordColumn);

        TableColumn<TableData, Integer> countColumn = new TableColumn<>("Совпадения");
        countColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
        table.getColumns().add(countColumn);

        wordColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.7));
        countColumn.prefWidthProperty().bind(table.widthProperty().multiply(0.3));

        wordColumn.setResizable(false);
        countColumn.setResizable(false);

        EventHandler<ActionEvent> event =
                e -> {
                    dictUtils.clearDicts();
                    File file = fileChooser.showOpenDialog(stage);

                    if (file != null) {
                        String path = file.getAbsolutePath();
                        if (path.endsWith("txt")) {
                            ReadFilesUtils.readTxtFile(path, dictUtils);
                        } else if (path.endsWith("doc")) {
                            ReadFilesUtils.readDocFile(path, dictUtils);
                        } else if (path.endsWith("docx")) {
                            ReadFilesUtils.readDocxFile(path, dictUtils);
                        } else {
                            log.debug("Wrong extension was chosen in fileChooser");
                            buttonLabel.setText("Расширение файла не поддерживается");
                            return;
                        }
                    } else {
                        buttonLabel.setText("Файл не выбран");
                        return;
                    }

                    Dict dict = dictUtils.getTheme();

                    if (dict == null) {
                        log.debug("Could not determine the subject of the text");
                        buttonLabel.setText("Не удалось определить тематику текста");
                        return;
                    }
                    buttonLabel.setText(dict.getName());
                    table.setItems(dictUtils.getStatistics(dict));
                };

        button.setOnAction(event);

        // create a VBox
        VBox vbox = new VBox(15, buttonLabel, button, tableLabel, table);

        // set Alignment
        vbox.setAlignment(Pos.CENTER);

        // create a scene
        Scene scene = new Scene(vbox, 500, 300);

        // set the scene
        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        log.debug("Program is running");
        launch();
        log.debug("Exiting program");
    }
}