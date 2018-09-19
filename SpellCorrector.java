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
        return null;
    }

    public static void main(String[] args){
        SpellCorrector sc = new SpellCorrector();
        sc.useDictionary("words.txt");
        System.out.println(sc.trie.getWordCount());
    }
}
