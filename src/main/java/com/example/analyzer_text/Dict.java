package com.example.analyzer_text;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс объединяет словарь для хранения ключевых слов, по которым будет определена тема текста,
 * а также имя словаря, которое затем используется как название тематики текста
 */
public class Dict {

    private final Logger log = LogManager.getLogger(Dict.class);

    /**
     * Имя словаря (название тематики)
     */
    private final String name;

    /**
     * Словарь для хранения ключевых слов
     */
    private final Map<String, Integer> dict = new HashMap<>();

    /**
     * Конструктор для создания объектов типа Dict
     *
     * @param name название словаря (теамтики текста)
     * @param path путь до файла с ключевыми словами
     */
    public Dict(String name, String path) {
        log.debug("Creating dictionary " + name);
        this.name = name;
        fillDict(dict, path);
    }

    /**
     * Метод для получения имени словаря
     *
     * @return имя словаря
     */
    public String getName() {
        return name;
    }

    /**
     * Метод для получения словаря
     *
     * @return словарь с ключевыми словами
     */
    public Map<String, Integer> getDict() {
        return dict;
    }

    /**
     * Метод заполняет словарь map ключевыми словами из файла, расположенного по пути path, используя слово как ключ
     *
     * @param map словарь, который будет заполняться ключевыми словами
     * @param path путь до файла с ключевыми словами
     */
    private void fillDict(Map<String, Integer> map, String path) {
        log.debug("Filling dictionary " + name + " from file " + path);
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while (reader.ready()) {
                String[] line = reader.readLine().split(" ");
                for (String word : line) {
                    map.put(word, 0);
                }
            }
        } catch (IOException e) {
            log.error("Exception while reading file " + path);
            e.printStackTrace();
        }
    }
}
