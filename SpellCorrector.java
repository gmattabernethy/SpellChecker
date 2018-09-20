import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SpellCorrector implements spell.ISpellCorrector {
    Trie trie;

    public SpellCorrector(){
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

        strings.addAll(deletion(inputWord));
        strings.addAll(transposition(inputWord));
        strings.addAll(alteration(inputWord));
        strings.addAll(insertion(inputWord));

        List<String> words = wordFind(strings);

        if(words.size() == 0){
            for(String s: strings){
                words.addAll(deletion(s));
                words.addAll(transposition(s));
                words.addAll(alteration(s));
                words.addAll(insertion(s));
            }

            words = wordFind(words);
        }


        if(words.size() == 0) return null;

        String appearsMost = "";
        int appearances = 0;

        for(String s : words){
                Node n = trie.find(s);
                if(n.count > appearances) {
                    appearances = n.count;
                    appearsMost = s;
                }
                else if(n.count == appearances && appearsMost.compareTo(s) > 0) appearsMost = s;
        }

        return appearsMost;
    }

    private List<String> wordFind(List<String> strings){
        ArrayList<String> words = new ArrayList<>();
        for(String str : strings){
            if(trie.find(str) != null) words.add(str);
        }

        return words;
    }

    private List<String> deletion(String inputWord){
        ArrayList<String> strings = new ArrayList<>();

        for(int i = 0; i < inputWord.length(); i++){
            StringBuilder builder = new StringBuilder();
            builder.append(inputWord);
            String deleted = builder.deleteCharAt(i).toString();

            strings.add(deleted);
        }

        return strings;
    }

    private List<String> transposition(String inputWord){
        ArrayList<String> strings = new ArrayList<>();

        for(int i = 0; i < inputWord.length()-1; i++){
            StringBuilder builder = new StringBuilder();
            builder.append(inputWord);
            builder.deleteCharAt(i);
            String transposed = builder.insert(i+1,inputWord,i, i+1).toString();

            strings.add(transposed);
        }

        return strings;
    }

    private List<String> alteration(String inputWord){
        ArrayList<String> strings = new ArrayList<>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < inputWord.length(); i++){
            for(int j = 0; j < 26; j++){
                StringBuilder builder = new StringBuilder();
                builder.append(inputWord);
                builder.deleteCharAt(i);
                String altered = builder.insert(i,alphabet,j, j+1).toString();

                strings.add(altered);
            }
        }

        return strings;
    }

    private List<String> insertion(String inputWord){
        ArrayList<String> strings = new ArrayList<>();
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        for(int i = 0; i < inputWord.length()+1; i++){
            for(int j = 0; j < 26; j++){
                StringBuilder builder = new StringBuilder();
                builder.append(inputWord);
                String inserted = builder.insert(i,alphabet,j, j+1).toString();

                strings.add(inserted);
            }
        }

        return strings;
    }

    public static void main(String[] args){
        SpellCorrector sc = new SpellCorrector();
        sc.useDictionary("test.txt");
        System.out.println(sc.suggestSimilarWord("gina"));
        System.out.println(sc.suggestSimilarWord("your"));
        System.out.println(sc.suggestSimilarWord("ur"));
    }

}
