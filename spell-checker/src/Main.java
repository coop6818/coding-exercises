import java.io.IOException;

/**
 * Main classs. Error checks arguments, and then creates a spellchecker and runs it
 */
public class Main {
    public static void main(String[] args) {
        
        if (args.length < 2) {
            System.out.println("Required arguments: <dictionary file> <text file>");
            return;
        }
        
        String dictionaryFile = args[0];
        String textFile = args[1];

        SpellChecker check = new SpellChecker(dictionaryFile);
        check.spellCheckFile(textFile);
    }
}