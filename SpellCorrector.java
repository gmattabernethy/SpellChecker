import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SpellCorrector implements spell.ISpellCorrector {
    Trie trie;

    SpellCorrector(){
        trie = new Trie();
    }

    @Override
    public void useDictionary(String dictionaryFileName) {
        try {
            File file = new File(dictionaryFileName);
            Scanner in = new Scanner(file);
            String str;

            while(in.hasNext()) {
                str = in.next();
                boolean word = true;

                for (int i = 0; i < str.length(); i++){
                    if(!Character.isLetter(str.charAt(i))) word = false;

                }

                if(word) trie.add(str);
            }

            in.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public String suggestSimilarWord(String inputWord) {
        if(trie.find(inputWord) != null) return inputWord.toLowerCase();

        ArrayList<String> strings = new ArrayList<>();

        String deleted = deletion(inputWord, 1);
        String transposed = transposition(inputWord, 1);
        String altered = alteration(inputWord, 1);
        String inserted = insertion(inputWord, 1);

        if(deleted != null) strings.add(deleted);
        if(transposed != null) strings.add(transposed);
        if(altered != null) strings.add(altered);
        if(inserted != null) strings.add(inserted);

        if(strings.size() == 0){
            deleted = deletion(inputWord, 2);
            transposed = transposition(inputWord, 2);
            altered = alteration(inputWord, 2);
            inserted = insertion(inputWord, 2);
        }

        if(deleted != null) strings.add(deleted);
        if(transposed != null) strings.add(transposed);
        if(altered != null) strings.add(altered);
        if(inserted != null) strings.add(inserted);

        if(strings.size() == 0) return null;

        String appearsMost = "";
        int appearances = 0;

        for(String s : strings){
                Node n = trie.find(s);
                if(n.count > appearances) {
                    appearances = n.count;
                    appearsMost = s;
                }
                else if(n.count == appearances && appearsMost.compareTo(s) > 0) appearsMost = s;
        }

        return appearsMost;
    }

    private String deletion(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();

        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder builder = new StringBuilder();
            builder.append(inputWord);
            String deleted = builder.deleteCharAt(i).toString();

            if (trie.find(deleted) != null) strings.add(deleted);

            if(distance > 1) {
                String str = deletion(deleted, distance-1);
                if(str != null) strings.add(str);
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    private String transposition(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();

        for(int i = 0; i < inputWord.length()-1; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(inputWord);
            builder.deleteCharAt(i);
            String transposed = builder.insert(i+1,inputWord,i, i+1).toString();

            if (trie.find(transposed) != null) strings.add(transposed);

            if (distance > 1) {
                String str = transposition(transposed, distance - 1);
                if (str != null) strings.add(str);
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    private String alteration(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < inputWord.length(); i++){
            for(int j = 0; j < 26; j++){
                StringBuilder builder = new StringBuilder();
                builder.append(inputWord);
                builder.deleteCharAt(i);
                String altered = builder.insert(i,alphabet,j, j+1).toString();

                if (trie.find(altered) != null) strings.add(altered);

                if (distance > 1) {
                    String str = alteration(altered, distance - 1);
                    if (str != null) strings.add(str);
                }
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    private String insertion(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < inputWord.length()+1; i++){
            for(int j = 0; j < 26; j++){
                StringBuilder builder = new StringBuilder();
                builder.append(inputWord);
                String inserted = builder.insert(i,alphabet,j, j+1).toString();

                if (trie.find(inserted) != null) strings.add(inserted);

                if (distance > 1) {
                    String str = insertion(inserted, distance - 1);
                    if (str != null) strings.add(str);
                }
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    public static void main(String[] args){
        SpellCorrector sc = new SpellCorrector();
        sc.useDictionary("test.txt");
        System.out.println(sc.suggestSimilarWord("ginna"));
        System.out.println(sc.suggestSimilarWord("your"));
        System.out.println(sc.suggestSimilarWord("u"));
    }

}
