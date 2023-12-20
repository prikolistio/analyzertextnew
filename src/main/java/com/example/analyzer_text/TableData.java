package com.example.analyzer_text;

/**
 * Класс для отображения данных в таблице JavaFX
 */
public class TableData {

    /**
     * Столбец, отвечающий за ключевое слово
     */
    private String word;

    /**
     * Столбец, отражающий число вхождений соответствующего ключевого слова в текст
     */
    private int count;

    /**
     * Конструктор для создания объектов класса TableData
     *
     * @param word ключевое слово
     * @param count количество вхождений
     */
    public TableData(String word, int count) {
        this.word = word;
        this.count = count;
    }

    /**
     * Получение ключевого слова
     *
     * @return ключевое слово
     */
    public String getWord() {
        return word;
    }

    /**
     * Получение числа вхождений для ключевого слова
     *
     * @return число вхождений
     */
    public int getCount() {
        return count;
    }
}
