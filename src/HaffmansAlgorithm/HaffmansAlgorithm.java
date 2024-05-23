package HaffmansAlgorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

import static RLE.RLE.compressRLE;


public class HaffmansAlgorithm {
    public static void main(String[] args) throws IOException {
        String text = "dddddeeeeettttt";

        TreeMap<Character, Integer> frequencies = countFrequency(text);

        System.out.println(HaffmansAlgorithm.countFrequency(text));

        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
        for (Character c : frequencies.keySet()) { // keySet() возвращает набор всех ключей отображения // для всех символов из таблицы...
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }

        CodeTreeNode tree = haffman(codeTreeNodes); // строим дерево с помощью вызова функции алгоритма Хаффмана

        // System.out.println(tree.weight);
        // System.out.println(tree.left.weight);

        TreeMap<Character, String> codes = new TreeMap<>(); // хранение строки двоичной
        for (Character c : frequencies.keySet()) {
            codes.put(c, tree.getCodeForCharacter(c, "")); // null не присваивать, потому что это считывается как строковый литерал
        }

        System.out.println("Таблица префиксных кодов: " + codes.toString());

        StringBuilder encoded = new StringBuilder();
        for ( int i = 0; i < text.length(); i++) {
            encoded.append(codes.get(text.charAt(i)));
        }

        System.out.println("Размер исходной строки: " + text.getBytes().length);
        System.out.println("Длина сжатой строки: " + encoded.length());

        //String input = "";
        System.out.println("Строка до сжатия: " + text);

        //String compressed = compressRLE(text);
        System.out.println("Строка после сжатия: " + encoded); // сама строка после

        FileWriter writer = new FileWriter("/Users/macuser/Desktop/test1.txt", false);

        writer.write(String.valueOf(encoded));
        writer.close(); // закрытие потока

        //String decoded = huffmanDecode(encoded.toString(), tree);

        //System.out.println("Расшифровано: " + decoded);
    }

    // с помощью кодового дерева нужно сгенерировать коды для каждого элемента массива

    private static TreeMap<Character, Integer> countFrequency(String text) { // функция, которая считает количество символов в строке
        TreeMap<Character, Integer> freqMap = new TreeMap<>();
        for (int i = 0; i < text.length(); i++) {
            Character c = text.charAt(i); // метод char вытаскивает символ по индексу в скобках
            Integer count = freqMap.get(c); // присваиваем переменной count тот самый символ
            freqMap.put(c, count != null ? count + 1 : 1);
        }
        return freqMap;
    }

    private static CodeTreeNode haffman(ArrayList<CodeTreeNode> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes);
            CodeTreeNode left = codeTreeNodes.remove(codeTreeNodes.size() - 1); // видимо, он удаляет и считывает последний и самый меньший узел из списка узлов
            CodeTreeNode right = codeTreeNodes.remove(codeTreeNodes.size() - 1);

            CodeTreeNode parent = new CodeTreeNode(null, right.weight + left.weight, left, right); // создаем пустой промежуточный узел
            codeTreeNodes.add(parent); // кладем промежуточный символ в массив (он, получается, добавляется в конце, как раз вместо двух меньших удаленных узлов)
        }
        return codeTreeNodes.get(0);
    }

    private static String huffmanDecode(String encoded, CodeTreeNode tree) { // декомпрессор принимает в качестве параметров закодированную строку и дерево кодировок
        StringBuilder decoded = new StringBuilder();

        CodeTreeNode node = tree; // переменная - текущий узел, который рассматриваем, когда спускаемся по дереву // изначально он равен самому корневому узлу
        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right;
            if (node.content != null) {
                decoded.append(node.content);
                node = tree;
            }
        }
        return decoded.toString();
    }

    private static class CodeTreeNode implements Comparable<CodeTreeNode> { // класс для представления кодового дерева

        Character content;
        int weight;
        CodeTreeNode left;
        CodeTreeNode right;

        public CodeTreeNode(Character content, int weight) { // конструктор листа
            this.content = content;
            this.weight = weight;
        }

        public CodeTreeNode(Character content, int weight, CodeTreeNode left, CodeTreeNode right) { // конструктор ветки
            this.content = content;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(CodeTreeNode o) {
            return o.weight - weight;
        }

        // сейчас будет обход по дереву с повротами налево и направо // здесь, чтобы в этом класссе полностью реализовывалось дерево и его функционал

        public String getCodeForCharacter(Character ch, String parentPath) {
            if (content == ch) { // дошли до нужного листа
                return parentPath;
            } else {
                if (left != null) {
                    String path = left.getCodeForCharacter(ch, parentPath + 0);
                    if (path != null) {
                        return path;
                    }
                }
                if (right != null) {
                    String path = right.getCodeForCharacter(ch, parentPath + 1);
                    if (path != null) {
                        return path;
                    }
                }

            } // если ни один иф не сработал, значит мы находимся в листе
            return null;
        }
    }
}
