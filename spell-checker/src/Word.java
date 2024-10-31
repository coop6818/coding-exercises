/**
 * Class to represent a word. Not strictly necessary, but adds convenience and
 * is a bit more future proof.
 */
public class Word {

	public String value;
	private int lineNumber;
	private int colNumber;
	private boolean start;
	private boolean capitalized;

	public Word(String word, int lineNumber, int colNumber, boolean start) {
		this.value = word.toLowerCase();
		this.lineNumber = lineNumber;
		this.colNumber = colNumber;
		this.start = start; //Represents whether or not a word is thought to be the start of a sentence
		capitalized = Character.isUpperCase(word.charAt(0));
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getColNumber() {
		return colNumber;
	}

	public boolean isStart() {
		return start;
	}

	public boolean isCapitalized() {
		return capitalized;
	}

	@Override
	public String toString() {
		return "Word{" + "word='" + value + '\'' + ", lineNumber=" + lineNumber + ", charIndex=" + colNumber + ", start="
				+ start + ", capitalized=" + capitalized + '}';
	}
}
