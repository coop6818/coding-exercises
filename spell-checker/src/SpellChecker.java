import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.*;

/**
 * Overall class for spellchecking. Takes in a dictionary file lication, creates
 * a dictionary using this file, then provides a method to spellcheck a
 * different file
 */
public class SpellChecker {
	private Dictionary dictionary;

	// Constructor, creates dictionary and catches errors
	public SpellChecker(String dictionaryFile) {
		try {
			dictionary = new Dictionary(dictionaryFile);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Main method used to spell check a file. Loops through test file, parses out
	 * words, then checks if those words exist. If they don't we get suggestions,
	 * and a little bit of context from the line, then print all that info to
	 * console.
	 */
	public void spellCheckFile(String testFile) {

		try (BufferedReader reader = new BufferedReader(new FileReader(testFile))) {
			String line;
			int lineNumber = 1; //start lines at 1 for readability
			boolean isStartOfSentence = true;

			//Parse file line by line
			while ((line = reader.readLine()) != null) {
				int charIndex = 0;
				
				
				int i = 0;
				//for each line loop until we hit a letter, ignoring everything else
				while(i < line.length()) {
					char c = line.charAt(i);

					//Once we hit a letter we assume its a word
					if (Character.isLetter(c)) {
						int startIndex = i;

						//loop until we hit another non character  letter, and assume this is a word
						while (i < line.length() && (Character.isLetter(line.charAt(i)))) i++;

						String word = line.substring(startIndex, i);
						//We create a temporary word object for convenience
						Word tempWord = new Word(word, lineNumber, startIndex, isStartOfSentence);

						//If the word isn't in the dictionary, suggest words for it
						if (!checkWord(tempWord)) suggestWords(tempWord, line);
						
						//Assume after we hit a word we are not at the beginning of a sentence
						isStartOfSentence = false;
						
						//Minor optimization to ignore possesive nouns
						if (i < line.length()-1 && line.substring(i,i+2).equals("'s")) i+=2;
						
					} 
					else i++;
					
					//The only non letter characters we care about are sentence enders, for proper noun detection
					if (c == '.' || c == '!' || c == '?') {
						isStartOfSentence = true;
					}
				}
				lineNumber++;
			}
		} catch (IOException e) {
			System.err.println("Error reading file: " + e.getMessage());
			e.printStackTrace();
		}
	}

	// Method to check if the word is in a dictionary. We are considering words that
	// are capitalized and don't start sentences as proper nouns
	public boolean checkWord(Word word) {
		if (!dictionary.isWord(word.value)) {
//			System.out.println(word);
			if (!word.isCapitalized() || word.isStart())
				return false;
			return true;
		}
		return true;
	}

	// Method that queries dictionary for suggestions, then prints formatted output to System
	public List<String> suggestWords(Word word, String line) {
		List<String> suggestions = dictionary.suggestWords(word.value, 5);
		System.out.println("Line " + word.getLineNumber() + ", Column " + word.getColNumber() + ": '" + word.value
				+ "' is not a word");
		System.out.println("\tContext: \""+extractContext(line, word.getColNumber(),word.getColNumber()+word.value.length())+"\"");
		System.out.print("\tSuggestions: ");
        for(int i=0; i<suggestions.size()-1; i++) System.out.print(suggestions.get(i)+", ");
        System.out.print(suggestions.get(suggestions.size()-1)+" \n\n");
        	

		return suggestions;
	}
	
	private static String extractContext(String line, int startIndex, int endIndex) {
	    StringBuilder context = new StringBuilder();
	    int wordCount = 0;
	    int i = Math.max(startIndex-1,0);
	    int j = Math.min(endIndex+1, line.length()-1);
	    while (i > 0 && wordCount<3) {
	        if (Character.isLetter(line.charAt(i))){
	        	while(i>0 && Character.isLetter(line.charAt(i))) i--;
	        	wordCount+=1;
	        }
	        else i--;
	        }
	    if (i!=0) i+=1;
	    
	    wordCount = 0;
	    while (j < line.length() && wordCount<3) {
	        if (Character.isLetter(line.charAt(j))){
	        	while(j<line.length() && Character.isLetter(line.charAt(j))) j++;
	        	wordCount+=1;
	        }
	        else j++;
	        }
//	    if (j!=line.length()) j+=1;
	    context.append(line.substring(i, startIndex)+"'"+line.substring(startIndex,endIndex)+"'"+line.substring(endIndex,j));


	    return context.toString();
	}

}