import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * BKTree class for word suggestion. Uses distance metric set in
 * caluclateDistanceMetric to sort words. Provides search for all words
 */
public class BKTree {
	private BKNode root;

	// Basic node class consisting of its value (word) and its children, stored in a Map for quick access/search
	private class BKNode {
		String word;
		Map<Integer, BKNode> children;

		BKNode(String word) {
			this.word = word;
			this.children = new HashMap<>();
		}
	}

	// Start tree completely empty
	public BKTree() {
		this.root = null;
	}

	// Tree is populated via repeated insertions
	public void insert(String word) {
		if (root == null)
			root = new BKNode(word);
		else
			insertRecursive(root, word);
	}

	// Inserts word, at the correct level. Each node has up to 1 child for each distance from its value
	private void insertRecursive(BKNode node, String word) {
		int distance = calculateDistanceMetric(node.word, word);

		// If the child node doesn't exist create it, otherwise recursively insert it
		if (node.children.containsKey(distance)) insertRecursive(node.children.get(distance), word);
		else node.children.put(distance, new BKNode(word));
	}

	// helper method allowing for swapping of distance metrics as needed
	private int calculateDistanceMetric(String word1, String word2) {
		return damerauLevenshteinDistance(word1, word2);
	}

	// Calculates Levenshtein Distance https://en.wikipedia.org/wiki/Levenshtein_distance via dynamic programming
	private int levenshteinDistance(String s1, String s2) {
		int lenS1 = s1.length();
		int lenS2 = s2.length();
		int[][] dp = new int[lenS1 + 1][lenS2 + 1];

		for (int i = 0; i <= lenS1; i++) {
			for (int j = 0; j <= lenS2; j++) {
				if (i == 0) dp[i][j] = j; // If s1 is empty
				else if (j == 0) dp[i][j] = i; // If s2 is empty
				else {
					dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, // Deletion
							dp[i][j - 1] + 1), // Insertion
							dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)); // Substitution
				}
			}
		}
		return dp[lenS1][lenS2];
	}

	// Calculates Damerau Levenshtein Distance https://en.wikipedia.org/wiki/Damerau%E2%80%93Levenshtein_distance
	// via dynamic programming
	private int damerauLevenshteinDistance(String s1, String s2) {
		int lenS1 = s1.length();
		int lenS2 = s2.length();
		int[][] dp = new int[lenS1 + 1][lenS2 + 1];

		for (int i = 0; i <= lenS1; i++) {
			for (int j = 0; j <= lenS2; j++) {
				if (i == 0) dp[i][j] = j; // If s1 is empty
				else if (j == 0) dp[i][j] = i; // If s2 is empty
				else {
					dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, // Deletion
							dp[i][j - 1] + 1), // Insertion
							dp[i - 1][j - 1] + (s1.charAt(i - 1) == s2.charAt(j - 1) ? 0 : 1)); // Substitution

					if (i > 1 && j > 1 && s1.charAt(i - 1) == s2.charAt(j - 2)
							&& s1.charAt(i - 2) == s2.charAt(j - 1)) {
						dp[i][j] = Math.min(dp[i][j], dp[i - 2][j - 2] + 1); // Transposition
					}
				}
			}
		}
		return dp[lenS1][lenS2];
	}


	/**
	 Search function. Takes in a word, and finds up to maxSuggestions words that are maxDistance from it, 
	 and places them in currentSuggestions. Since we are searching in ascending distance order we should 
	always get the best suggestions based on distance, but order within that distance is based on insertion order
	 */
	public Set<String> search(String word, int maxDistance, int maxSuggestions, Set<String> currentSuggestions) {
		// HashSet so that we can avoid duplicates
		Set<String> suggestions = new LinkedHashSet<>(currentSuggestions);

		if (root != null)
			searchRecursive(root, word, maxDistance, suggestions, maxSuggestions);
		return suggestions;
	}

	// Recursive search method since we are searching a tree. Checks current node then recurs on relevant children
	private void searchRecursive(BKNode node, String word, int maxDistance, Set<String> suggestions,
			int maxSuggestions) {
		int distance = calculateDistanceMetric(node.word, word);

		if (distance <= maxDistance)
			suggestions.add(node.word);

		// Stop searching if we've reached the desired number of suggestions
		if (suggestions.size() >= maxSuggestions)
			return;

		// We want to check all children with the range of Distance and Max Distance, and recur on each
		for (int d = distance - maxDistance; d <= distance + maxDistance; d++) {
			if (node.children.containsKey(d)) {
				searchRecursive(node.children.get(d), word, maxDistance, suggestions, maxSuggestions);
				if (suggestions.size() >= maxSuggestions)
					return;
			}
		}
	}
}
