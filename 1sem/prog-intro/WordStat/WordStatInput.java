package WordStat;

import java.io.*;
import java.lang.StringBuilder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

public class WordStatInput {
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(args[0]), StandardCharsets.UTF_8))) {
            char[] bufferSize = new char[1024];
            StringBuilder word = new StringBuilder();
            int countOfCharsReaded = br.read(bufferSize);
            while (countOfCharsReaded != -1) {
                for (int j = 0; j < countOfCharsReaded; j++) {
                    char i = bufferSize[j];
                    if (Character.isLetter(i) || i == '\'' || Character.DASH_PUNCTUATION == Character.getType(i)) {
                        word.append(i);
                    } else if (!word.isEmpty()) {
                        map.merge(word.toString().toLowerCase(), 1, Integer::sum);
                        word.setLength(0);
                    }
                }
                countOfCharsReaded = br.read(bufferSize);
            }
        } catch (IOException e) {
            System.err.println("Input error: " + e.getMessage());
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                bw.write(entry.getKey() + " " + entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Output error: " + e.getMessage());
        }
    }
}
