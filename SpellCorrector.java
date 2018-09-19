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

        String deleted = deletionDistance(inputWord, 1);
        String transposed = transpositionDistance(inputWord, 1);
        String altered = alterationDistance(inputWord, 1);
        String inserted = insertionDistance(inputWord, 2);

        //if()

        return inserted;
    }

    private String deletionDistance(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();

        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder builder = new StringBuilder();
            builder.append(inputWord);
            String deleted = builder.deleteCharAt(i).toString();

            if (trie.find(deleted) != null) strings.add(deleted);

            if(distance > 1) {
                String str = deletionDistance(deleted, distance-1);
                if(str != null) strings.add(str);
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    private String transpositionDistance(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();

        for(int i = 0; i < inputWord.length()-1; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(inputWord);
            builder.deleteCharAt(i);
            String transposed = builder.insert(i+1,inputWord,i, i+1).toString();

            if (trie.find(transposed) != null) strings.add(transposed);

            if (distance > 1) {
                String str = transpositionDistance(transposed, distance - 1);
                if (str != null) strings.add(str);
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    private String alterationDistance(String inputWord, int distance){
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
                    String str = alterationDistance(altered, distance - 1);
                    if (str != null) strings.add(str);
                }
            }
        }

        Collections.sort(strings);

        if(strings.size() > 0) return strings.get(0);
        else return null;
    }

    private String insertionDistance(String inputWord, int distance){
        ArrayList<String> strings = new ArrayList<>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < inputWord.length()+1; i++){
            for(int j = 0; j < 26; j++){
                StringBuilder builder = new StringBuilder();
                builder.append(inputWord);
                String inserted = builder.insert(i,alphabet,j, j+1).toString();

                if (trie.find(inserted) != null) strings.add(inserted);

                if (distance > 1) {
                    String str = insertionDistance(inserted, distance - 1);
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
        System.out.println(sc.suggestSimilarWord("yo"));
        System.out.println(sc.suggestSimilarWord("yu"));
        System.out.println(sc.suggestSimilarWord("y"));
    }

}
