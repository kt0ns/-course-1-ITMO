package md2html;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;


public class Md2Html {
    private static final Character[] singleTags = {'*', '_', '`', '\\', '<', '>', '&'};
    private static final Character[] doubleTags = {'*', '_', '-', '+'};
    private static final String[] screenTags = {"\\", "<", ">", "&"};
    private static final Set<Character> typeOfDoubleTags = new HashSet<>(Arrays.asList(doubleTags));
    private static final Set<Character> typeOfSingleTags = new HashSet<>(Arrays.asList(singleTags));
    private static final Set<String> typeOfScreenTags = new HashSet<>(Arrays.asList(screenTags));
    private static IntList indexes = new IntList();
    private static List<String> tags = new ArrayList<>();
    private static int numOfParagpraph = 0;
    private static final String lineSep = System.lineSeparator();
    private static final Map<String, Boolean> tagStatus = new HashMap<>() {{
        put("*", false);
        put("_", false);
        put("--", false);
        put("++", false);
        put("**", false);
        put("__", false);
        put("`", false);
    }};
    private static final Map<String, String> htmlTags = new HashMap<>() {{
        put("*", "em");
        put("_", "em");
        put("--", "s");
        put("++", "u");
        put("**", "strong");
        put("__", "strong");
        put("`", "code");
        put("\\", "");
        put("<", "&lt;");
        put("&", "&amp;");
        put(">", "&gt;");
    }};


    public static void main(String[] args) {
        final String inputFileName = args[0];
        final String outputFileName = args[1];
        try (Scanner file = new Scanner(new File(inputFileName))) {
            StringBuilder paragraph = new StringBuilder();
            final StringBuilder outputFile = new StringBuilder();
            while (file.hasNextLine()) {
                String line = file.nextLine();
                if (!line.trim().isEmpty()) {
                    paragraph.append(line).append(lineSep);
                } else if (!paragraph.isEmpty()) {
                    outputFile.append(parseToHTML(paragraph.toString()));
                    paragraph = new StringBuilder();
                }
            }
            if (!paragraph.isEmpty()) {
                outputFile.append(parseToHTML(paragraph.toString()));
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
                writer.write(outputFile.toString());
            } catch (IOException e) {
                System.out.println("Output error: " + e.getMessage());
            }

        } catch (IOException e) {
            System.out.println("Input error: " + e.getMessage());
        }
    }


    public static String parseToHTML(String text) {
        getInfoAboutParagraph(text);
        return craftParsedParagraph(text);
    }


    public static void getInfoAboutParagraph(String text) {
        int size = text.length();
        int i = 0;
        while (i < size && text.charAt(i) == '#') {
            i++;
        }
        if (text.charAt(i) == ' ') {
            numOfParagpraph = i;
        }
        for (int j = i; j < size; j++) {
            char firstCharOfTag = text.charAt(j);
            if (j < size - 1) {
                if (typeOfDoubleTags.contains(firstCharOfTag) && firstCharOfTag == text.charAt(j + 1)) {
                    indexes.add(j);
                    tags.add(text.substring(j, j + 2));
                    j++;
                } else if (typeOfSingleTags.contains(firstCharOfTag)) {
                    indexes.add(j);
                    tags.add(text.substring(j, j + 1));
                }
            } else {
                if (typeOfSingleTags.contains(firstCharOfTag)) {
                    indexes.add(j);
                    tags.add(text.substring(j, j + 1));
                }
            }
        }
    }

    public static String craftParsedParagraph(String text) {
        final StringBuilder parsedText = new StringBuilder();
        int lastSlice = (numOfParagpraph == 0) ? (numOfParagpraph) : (numOfParagpraph + 1);
        if (numOfParagpraph == 0) {
            parsedText.append("<p>");
        } else {
            parsedText.append("<h").append(numOfParagpraph).append('>');
        }
        for (int i = 0; i < tags.size(); i++) {
            String tag = tags.get(i);
            String lastTag = i > 0 ? tags.get(i - 1) : "";
            if (!lastTag.equals("\\")) {
                if (!typeOfScreenTags.contains(tag)) {
                    if (!tagStatus.get(tag)) {
                        if (findIndexOfNextTag(tags, i + 1, tag) != Integer.MAX_VALUE) {
                            parsedText.append(text, lastSlice, indexes.get(i));
                            lastSlice = indexes.get(i) + tags.get(i).length();
                            parsedText.append("<").append(htmlTags.get(tag)).append(">");
                            tagStatus.put(tag, true);
                        }
                    } else {
                        parsedText.append(text, lastSlice, indexes.get(i));
                        lastSlice = indexes.get(i) + tags.get(i).length();
                        parsedText.append("</").append(htmlTags.get(tag)).append(">");
                        tagStatus.put(tag, false);
                    }
                } else {
                    parsedText.append(text, lastSlice, indexes.get(i));
                    lastSlice = indexes.get(i) + tags.get(i).length();
                    parsedText.append(htmlTags.get(tag));
                }
            }
        }
        parsedText.append(text, lastSlice, text.length() - lineSep.length());
        if (numOfParagpraph == 0) {
            parsedText.append("</p>");
        } else {
            parsedText.append("</h").append(numOfParagpraph).append('>');
        }
        parsedText.append(lineSep);
        numOfParagpraph = 0;
        indexes = new IntList();
        tags = new ArrayList<>();
        return parsedText.toString();
    }

    public static int findIndexOfNextTag(List<String> list, int begin, String key) {
        for (int i = begin; i < list.size(); i++) {
            if (list.get(i).equals(key) ) {
                if (i > 0 && !list.get(i - 1).equals("\\")) {
                    return i;
                } else if (i == 0) {
                    return i;
                }
            }
        }
        return Integer.MAX_VALUE;
    }
}