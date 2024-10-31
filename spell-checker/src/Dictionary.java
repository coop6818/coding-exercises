import java.io.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Dictionary class for comparing words. Uses a set to quickly check if a word
 * exists, and a BK tree to suggest words for misspellings
 */
public class Dictionary {
	private HashSet<String> validWords;
	private BKTree wordTree;

	// Constructor, loads dictionary for given file
	public Dictionary(String filePath) throws IOException {
		validWords = new HashSet<>();
		wordTree = new BKTree();
		loadDictionary(filePath);
	}

	// Check if word exists
	public boolean isWord(String word) {
		return validWords.contains(word.toLowerCase());
	}

	// Suggests numSuggestions words that are most similar to word. Uses a set to
	// avoid duplicates
	public List<String> suggestWords(String word, int numSuggestions) {
		Set<String> suggestions = new LinkedHashSet<>();

		// Check ascending distance metrics measurements until we get 5 suggestions, or
		// we have an edit distance of larger than 5
		for (int i = 1; i <= 5; i++) {
			Set<String> tempList = wordTree.search(word, i, numSuggestions, suggestions);
			for (String tempWord : tempList) {
				suggestions.add(tempWord);
				if (suggestions.size() == numSuggestions)
					return new ArrayList<>(suggestions);
			}
		}
		return new ArrayList<>(suggestions); // Convert Set back to List for return
	}

	// loads dictionary by filling hashset for quick lookup, and tree for
	// suggestions
	private void loadDictionary(String filePath) throws IOException {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Futureproofing against capitalized words that might be added to dict
				String word = line.toLowerCase(); 
				wordTree.insert(word);
				validWords.add(word);
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
