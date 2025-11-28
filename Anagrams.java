import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Anagrams {

    final Integer[] primes = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
            43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101
    };

    Map<Character, Integer> letterTable;

    Map<Long, ArrayList<String>> anagramTable;

    public Anagrams() {
        letterTable = new HashMap<>();
        buildLetterTable();
        anagramTable = new HashMap<>();
    }

    private void buildLetterTable() {
        for (int i = 0; i < 26; i++) {
            letterTable.put((char) ('a' + i), primes[i]);
        }
    }

    private Long myHashCode(String s) {
        if (s.isEmpty()) {
            throw new IllegalArgumentException("String cannot be empty");
        }

        long hash = 1L;

        for (int i = 0; i < s.length(); i++) {
            char c = Character.toLowerCase(s.charAt(i));
            hash *= letterTable.get(c);
        }

        return hash;
    }

    private void addWord(String s) {
        if (s == null || s.trim().isEmpty())
            return;

        Long key = myHashCode(s);

        ArrayList<String> list = anagramTable.get(key);
        if (list == null) {
            list = new ArrayList<>();
            anagramTable.put(key, list);
        }
        list.add(s);
    }

    private void processFile(String filename) throws IOException {
        BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(filename)));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                addWord(line.trim());
            }
        } finally {
            br.close();
        }
    }

    private ArrayList<Map.Entry<Long, ArrayList<String>>> getMaxEntries() {

        ArrayList<Map.Entry<Long, ArrayList<String>>> result = new ArrayList<>();
        int maxSize = 0;

        for (Map.Entry<Long, ArrayList<String>> entry : anagramTable.entrySet()) {
            int size = entry.getValue().size();

            if (size > maxSize) {
                result.clear();
                maxSize = size;
                result.add(entry);
            } else if (size == maxSize && size > 1) {
                result.add(entry);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        Anagrams a = new Anagrams();

        long start = System.nanoTime();

        try {
            a.processFile("words_alpha.txt");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        ArrayList<Map.Entry<Long, ArrayList<String>>> max = a.getMaxEntries();
        long end = System.nanoTime();

        double seconds = (end - start) / 1_000_000_000.0;
        System.out.println("Elapsed Time : " + seconds);

        for (Map.Entry<Long, ArrayList<String>> entry : max) {
            System.out.println("Key of max anagrams : " + entry.getKey());
            System.out.println("List of max anagrams : " + entry.getValue());
            System.out.println("Length of list of max anagrams : " + entry.getValue().size());
        }
    }
}
