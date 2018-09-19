import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
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
        String transposed = transpositionDistance(inputWord);
        String altered = alterationDistance(inputWord);
        String inserted = insertionDistance(inputWord);

        if()

        return null;
    }

    private String deletionDistance(String inputWord, int distance){
      String str = null;
      StringBuilder builder = new StringBuilder();
      builder.append(inputWord);

      for(int i = 0; i < inputWord.length(); i++){
        String deleted = builder.deleteCharAt(i);
        if trie.find(deleted)
      }

      return str;
    }

    private String transpositionDistance(String inputWord){


    }

    private String alterationDistance(String inputWord){


    }

    private String insertionDistance(String inputWord){


    }
/*
    public static void main(String[] args){
        SpellCorrector sc = new SpellCorrector();
        System.out.println(sc.suggestSimilarWord("hi"));
    }
    */
}
