package com.example.analyzer_text;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Класс для взаимодействия с объектами класса Dict
 */
public class DictUtils {

    private final static Logger log = LogManager.getLogger(DictUtils.class);

    /**
     * Список словарей, по которым будут проверяться слова в тексте
     */
    private final List<Dict> dicts;

    /**
     * Конструктор для создания DictUtils
     *
     * @param dicts список словарей
     */
    public DictUtils(List<Dict> dicts) {
        this.dicts = dicts;
    }

    /**
     * Метод проверяет наличие слова в имеющихся словарях. Если есть совпадение, то значение по ключу увеличивается
     *
     * @param word слово, по которому происходит поиск в словарях
     */
    public void checkDict(String word) {
        for (Dict dict : dicts) {
            Map<String, Integer> map = dict.getDict();
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (containsIgnoreCase(word, entry.getKey())) {
                    entry.setValue(entry.getValue() + 1);
                    log.debug(word + " found in " + dict.getName() + " dictionary, current value = " + entry.getValue());
                }
            }
        }
    }

    /**
     * Метод возвращает словарь, в котором выявлено набольшее совпадение по ключевым словам
     *
     * @return словарь Dict
     */
    public Dict getTheme() {
        int max = 0;
        Dict res = null;

        for (Dict dict : dicts) {
            int t = getSumForDict(dict.getDict());
            if (t > max) {
                max = t;
                res = dict;
            }
        }

        return res;
    }

    /**
     * Метод для получения статистики по появлению ключевых слов в тексте. Возвращает список объектов TableData для
     * отображения статистики в таблице
     *
     * @param dict словарь, по которому нужно получить статистику
     * @return список объектов TableData для отображения в таблице
     */
    public ObservableList<TableData> getStatistics(Dict dict) {
        ObservableList<TableData> list = FXCollections.observableArrayList();

        dict.getDict().entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue((v1, v2) -> v1 < v2 ? 1 : (v1 == v2) ? 0 : -1))
                .forEach(e -> list.add(new TableData(e.getKey(), e.getValue())));

        return list;
    }

    /**
     * Метод находит сумму значений для всех ключей в словаре, таким образом определяя сколько раз ключевые слова были
     * найдены в тексте
     *
     * @param dict словарь, в котором будет происходить сложение значений
     * @return сумма значений по всем ключам в словаре
     */
    private int getSumForDict(Map<String, Integer> dict) {
        return dict.values().stream().mapToInt(Integer::intValue).sum();
    }

    /**
     * Обнуляет все значения по ключам в словарях из писка dicts
     */
    public void clearDicts() {
        for (Dict dict : dicts) {
            for (Map.Entry<String, Integer> entry : dict.getDict().entrySet()) {
                entry.setValue(0);
            }
        }
    }
}
