import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.util.*;
/**
 * Helper class used to introduce error to words and files
 */
public class ErrorGenerator {

    public static void main(String[] args) {

        
        if (args.length < 1) {
            System.out.println("Required arguments: <text file>");
            return;
        }
        
        String textFile = args[0];
        
        ErrorGenerator.introduceErrorsFromFile(textFile, textFile.substring(0,textFile.length()-4)+"_test.txt");

    }

    //Reads in file, introduces errors to words at random, and saves file again
    public static void introduceErrorsFromFile(String inputFile, String outputFile) {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
        	Random random = new Random();
            String line;
            while ((line = reader.readLine()) != null) {
                StringBuilder modifiedLine = new StringBuilder();
                int i = 0;

                while (i < line.length()) {
                    char c = line.charAt(i);

                    if (Character.isLetter(c)) {
                        int startIndex = i;

                        while (i < line.length() && Character.isLetter(line.charAt(i))) i++;

                        String word = line.substring(startIndex, i);
                        int errorChance = random.nextInt(20);
                        String modifiedWord = word;
                        if (errorChance <= 5) modifiedWord = introduceErrorToWord(word);
                        modifiedLine.append(modifiedWord);

                    } else {
                        modifiedLine.append(c); // Non-letter characters are added directly
                        i++;
                    }
                }
                writer.println(modifiedLine.toString().trim());
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Method to introduce error to a word to simulate misspelling. NumModifications can be changed to introduce more than one error
    public static String introduceErrorToWord(String word) {
    	Random random = new Random();
        String modifiedWord = word;

        	int numModifications = random.nextInt(2);
        	for(int i = 0; i<numModifications; i++) {
	            int modificationType = random.nextInt(4); // 0: insertion, 1: deletion, 2: substitution, 3: transposition
	            switch (modificationType) {
	                case 0: // Insertion
	                    modifiedWord = insertCharacter(modifiedWord);
	                    break;
	                case 1: // Deletion
	                    modifiedWord = deleteCharacter(modifiedWord);
	                    break;
	                case 2: // Substitution
	                    modifiedWord = substituteCharacter(modifiedWord);
	                    break;
	                case 3: // Transposition
	                    modifiedWord = transposeCharacters(modifiedWord);
	                    break;
            	}
        	}
        return modifiedWord;
    }

    private static String insertCharacter(String word) {
        Random random = new Random();
        char[] chars = word.toCharArray();
        int index = random.nextInt(chars.length + 1); // Insert at any position
        char newChar = (char) ('a' + random.nextInt(26)); // Random lowercase letter
        return new StringBuilder(word).insert(index, newChar).toString();
    }

    private static String deleteCharacter(String word) {
        if (word.length() <= 1) return word; // Can't delete if the word is too short
        Random random = new Random();
        int index = random.nextInt(word.length());
        return new StringBuilder(word).deleteCharAt(index).toString();
    }

    private static String substituteCharacter(String word) {
        if (word.length() <= 0) return word; // Can't substitute if the word is empty
        Random random = new Random();
        int index = random.nextInt(word.length());
        char[] chars = word.toCharArray();
        chars[index] = (char) ('a' + random.nextInt(26)); // Random lowercase letter
        return new String(chars);
    }

    private static String transposeCharacters(String word) {
        if (word.length() < 2) return word; // Can't transpose if the word is too short
        Random random = new Random();
        int index = random.nextInt(word.length() - 1); // Ensure there's a character to swap with
        char[] chars = word.toCharArray();
        // Swap adjacent characters
        char temp = chars[index];
        chars[index] = chars[index + 1];
        chars[index + 1] = temp;
        return new String(chars);
    }
}
