package RLE;

import java.io.*;
public class RLE {

        public static String compressRLE(String input) {
            StringBuilder compressed = new StringBuilder();

            char lastChar = input.charAt(0);
            int count = 1;

            for (int i = 1; i < input.length(); i++) {
                char currentChar = input.charAt(i);

                if (currentChar == lastChar) {
                    count++;
                } else {
                    compressed.append(lastChar);
                    compressed.append(count);

                    lastChar = currentChar;
                    count = 1;
                }
            }

            compressed.append(lastChar);
            compressed.append(count);

            return compressed.toString();
        }

        public static void main(String[] args) throws IOException {

            String input = "dddddeeeeettttt";
            System.out.println("Строка до сжатия: " + input);

            String compressed = compressRLE(input);
            System.out.println("Строка после сжатия: " + compressed);

            FileWriter writer = new FileWriter("/Users/macuser/Desktop/test1.txt", false);

            writer.write(compressed);
            writer.close(); // закрытие потока

        }
}
