package WSPP;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class WsppCountPosition {

    public static class IntList {
        private int[] array;
        private int pos = 0;
        private int len;

        public IntList(int size) {
            this.array = new int[size];
            this.len = size;
        }

        public IntList() {
            this.array = new int[8];
            this.len = 8;
        }

        public void add(int i) {
            if (pos == len - 1) {
                resize();
            }
            array[pos] = i;
            pos++;
        }

        public boolean isEmpty() {
            return pos == 0;
        }

        public int size() {
            return pos;
        }

        public void clear() {
            array = new int[8];
            pos = 0;
        }

        public String toString() {
            if (pos == 0)
                return "";
            int iMax = pos - 1;

            StringBuilder b = new StringBuilder();
            for (int i = 0; ; i++) {
                b.append(array[i]);
                if (i == iMax)
                    return b.toString();
                b.append(" ");
            }
        }

        public String toStringPairs() {
            if (pos == 0)
                return "";
            int iMax = pos - 2;

            StringBuilder b = new StringBuilder();
            for (int i = 0; ; i += 2) {
                b.append(array[i]);
                b.append(":");
                b.append(array[i + 1]);
                if (i == iMax) {
                    return b.toString();
                }
                b.append(" ");
            }
        }

        public int get(int i) {
            return array[i];
        }

        private void resize() {
            len = array.length * 2;
            array = Arrays.copyOf(array, len);
        }
    }

    public static void main(String[] args) {
        TreeMap<String, IntList> map = new TreeMap<>();
        int lines = 1;
        String input = args[0];
        String output = args[1];

        try (MySSScanner scan = new MySSScanner(new File(input))) {
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                if (!line.isEmpty()) {
                    int wordPos = 0;
                    MySSScanner toWords = new MySSScanner(line);
                    while (toWords.hasNextWord()) {
                        String word = toWords.nextWord().toLowerCase();
                        wordPos++;
                        updateMap(map, wordPos, lines, word);
                    }
                    lines++;
                }
            }
        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8))) {
            for (Map.Entry<String, IntList> entry : sortForValues(map)) {
                bw.write(entry.getKey() + " " + entry.getValue().size() / 2 + " " + entry.getValue().toStringPairs() + System.lineSeparator());
            }
        } catch (IOException e) {
            System.err.println("Output error: " + e.getMessage());
        }
    }

    public static void updateMap(TreeMap<String, IntList> map, int wordPos, int lineNum, String word) {
        IntList i = map.getOrDefault(word, null);
        if (i == null) {
            map.put(word, new IntList());
            map.get(word).add(lineNum);
            map.get(word).add(wordPos);
        } else {
            i.add(lineNum);
            i.add(wordPos);
        }
    }

    public static List<Map.Entry<String, IntList>> sortForValues(TreeMap<String, IntList> map) {
        List<Map.Entry<String, IntList>> entries = new ArrayList<>(map.entrySet());
        entries.sort((entry1, entry2) -> {
            IntList value1 = entry1.getValue();
            IntList value2 = entry2.getValue();
            int len1 = value1.size();
            int len2 = value2.size();

            if (len1 == len2) {
                if (value1.get(0) == value2.get(0)) {
                    return Integer.compare(value1.get(1), value2.get(1));
                } else return Integer.compare(value1.get(0), value2.get(0));
            } else {
                return Integer.compare(len1, len2);
            }
        });
        return entries;
    }
}
