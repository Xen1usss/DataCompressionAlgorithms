package LZ77;

import java.io.FileWriter;
import java.io.IOException;

public class LZ77 {

    public static void main(String[] args) throws IOException {
        String input = "dddddeeeeettttt";
        String compressed = compress(input); // вызов метода compress для сжатия строки
        System.out.println("Compressed string: " + compressed); // вывод сжатой строки
        System.out.println("Длина исходной строки: " + input.length());
        System.out.println("Длина сжатой строки: " + compressed.length());

        FileWriter writer = new FileWriter("/Users/macuser/Desktop/test1.txt", false);

        writer.write(compressed);
        writer.close(); // закрытие потока

    }

    public static String compress(String input) { // объявление метода compress, который принимает на вход строку input и возвращает сжатую строку
        StringBuilder compressed = new StringBuilder(); // создание объекта для хранения сжатой строки

        int windowSize = 10; // размер окна для поиска совпадений
        int lookaheadSize = 5; // размера плавающего окна
        int currentPosition = 0; // текущая позиция входной строки

        while (currentPosition < input.length()) {
            int bestMatchLength = 0; // инициализация переменной для хранения длины лучшего совпадения
            int bestMatchDistance = 0; // инициализация переменной для хранения расстояния до лучшего совпадения

            for (int i = 1; i <= Math.min(windowSize, currentPosition); i++) { // поиск лучшего совпадения
                int matchLength = 0; // инициализация переменной для хранения длины текущего совпадения
                while (currentPosition + matchLength < input.length() && matchLength < lookaheadSize  // проверка совпадения символов в окне и "выглядывающем" окне
                        && input.charAt(currentPosition - i + matchLength) == input.charAt(currentPosition + matchLength)) {
                    matchLength++; // увеличение длины текущего совпадения
                }

                if (matchLength > bestMatchLength) { // проверка, является ли текущее совпадение лучшим
                    bestMatchLength = matchLength; // обновление длины лучшего совпадения
                    bestMatchDistance = i; // обновление расстояния до лучшего совпадения
                }
            }

            if (bestMatchLength > 0) { // если найдено совпадение
                compressed.append("<" + bestMatchDistance + "," + bestMatchLength); // добавление информации о совпадении в сжатую строку / + ">"
                currentPosition += bestMatchLength; // обновление текущей позиции входной строки
            } else { // если совпадение не найдено
                compressed.append(input.charAt(currentPosition)); // добавление символа в сжатую строку
                currentPosition++; // увеличение текущей позиции входной строки
            }
        }

        return compressed.toString(); // возврат сжатой строки
    }


}
