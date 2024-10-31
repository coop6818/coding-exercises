// test/SpellCheckerTest.java

public class SpellCheckerTest {
    public static void main(String[] args) {
    	
        System.out.println("Loading 'dictionary.txt'");
        SpellChecker spellChecker = new SpellChecker("resources/dictionary.txt");

        System.out.println("Spell checking 'dictionary.txt'");
        spellChecker.spellCheckFile("resources/dictionary.txt");
        
        System.out.println("Spell checking 'test.txt'");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        spellChecker.spellCheckFile("resources/test.txt");
        
        System.out.println("\n\nSpell checking lotr.txt");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        spellChecker.spellCheckFile("resources/lotr.txt");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
    }
}