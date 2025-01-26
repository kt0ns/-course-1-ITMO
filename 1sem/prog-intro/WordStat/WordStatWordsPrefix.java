package WordStat;

import java.io.*;
import java.lang.StringBuilder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;

public class WordStatWordsPrefix {
    public static void main(String[] args) {
        TreeMap<String, Integer> map = new TreeMap<>(Comparator.reverseOrder());
        String fileName = args[0];
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
            char[] bufferSize = new char[1024];
            StringBuilder word = new StringBuilder();
            int countOfCharsReaded;
            while ( (countOfCharsReaded = br.read(bufferSize)) != -1 ) {
                for (int j = 0; j < countOfCharsReaded; j ++ ) {
                    char i = bufferSize[j];
                    if (Character.isLetter(i) || i == '\'' || Character.DASH_PUNCTUATION == Character.getType(i)) {
                        word.append(i);
                    } else if (!word.isEmpty() && word.length() > 2) {
                        map.merge(word.toString().substring(0, 3).toLowerCase(), 1, Integer::sum);
                        word.setLength(0);
                    } else if (!word.isEmpty()) {
                        map.merge(word.toString().toLowerCase(), 1, Integer::sum);
                        word.setLength(0);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Unluchko input: " + e.getMessage());
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(args[1]), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                bw.write(entry.getKey() + " " + entry.getValue() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Unluchko output: " + e.getMessage());
        }
    }
}
