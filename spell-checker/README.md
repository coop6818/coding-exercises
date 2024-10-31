# Solution

## Installation and Deployment


### Requirements

The program is written in Java, and requires the latest JDK. Download and installation instructions can be found here:

   - https://www.oracle.com/java/technologies/downloads/


### Compilation

For simplicity I opted to simply use `javac` to compile the program:

1. **Open a Terminal:**
   - Navigate to the project root directory.

2. **Compile Source Files:**
   - Run the following command to compile the Java source files in the `src` directory:
     ```bash
     javac -d bin src/*.java
     ```

3. **Compile Test Files:**
   - Similarly, compile the test files in the `test` directory:
     ```bash
     javac -d bin -cp bin test/*.java
     ```

### Step 3: Run the Application

1. **Run the Main Application:**
   - Execute the compiled `Main` class with the two required arguments:
     ```bash
     java -cp bin Main <path to dictionary file> <path to text file>
     ```

### Step 4: Run the Tests

1. **Run the Spell Checker Tests:**
   - Execute the `SpellCheckerTest` class:
     ```bash
     java -cp bin SpellCheckerTest
     ```
     
2.  **Generate File With Errors:**
   - Execute the `ErrorGenerator` class:
     ```bash
     java -cp bin ErrorGenerator <path to text file>
     ```
## Resources

- `resources/dictionary.txt`: Contains the dictionary used for spell checking.
- `resources/dictionary_test.txt`: Contains the dictionary used for spell checking with random errors inserted.
- `resources/test.txt`: Sample text for testing purposes.
- `resources/lotr.txt`: Sample text for testing purposes.

## Background

Since SpellCheck is a common field, a lot of the work involved in this project was researching what approaches were available, and balancing complexity and accuracy. Parsing the input file into words and identifying which of those words are spelled incorrectly according to the provided dictionary were both fairly trivial problems. This meant that the main focus of my research was how to suggest words, how to handle proper nouns, and how to create test data. I wanted to avoid out-of-the-box third-party solutions to showcase the approach I would take in their absence, but looking at what the commonly used approaches did gave me a good idea of how to tackle the problem. A few of the algorithms/papers I looked at were:

https://norvig.com/spell-correct.html

https://aclanthology.org/2020.emnlp-demos.21.pdf

https://seekstorm.com/blog/1000x-spelling-correction/

http://aspell.net/man-html/Phonetic-Code.html

As expected there are a large number of effective ways to approach the problem. More classical approaches seemed to use a metric for string comparison, and then some kind of data structure to make searching through the dictionary more efficient. The most commonly used metric was edit distance, defined as the number of discrete operations a string was from another. Typically Levenshtein or Damerau-Levenshtein distance was used, as they were the most comprehensive. Some approaches applied a number of random edits to the input and searched using the results, while others applied these edits to every word in the dictionary, causing a large initial overhead, and then O(1) search. Aspell used a more novel approach of word phonetics, while others used NLP algorithms. NLP was effective in suggesting words all on its own, or providing context/more information to other algorithms.  Another common optimization was using word frequency when considering suggestions. Most algorithms not involving NLP had no specific failsafe for proper nouns; typically it seems like the 'corpus' or training input for the dictionary were relied on. 

## Approach

With all of this context in mind, I decided to forgo an AI/ML approach due to lack of training data, and avoided solutions that added complexity in exchange for faster runtimes. The dictionary provided is only about 200k lines, so incredibly high efficiency wasn't worth the complexity trade-off given the relatively small size. I opted to take an edit distance based approach. Damerau-Levenshtein distance was not significantly more involved to implement than Levenshtein distance, and BK-trees seemed to be by far the most popular simple data structure, so I settled on using a combination of the two.

Outside of adding them to the dictionary, there weren't any simple ways to handle proper nouns, so I decided to just use a few basic context clues to detect them. I assumed all proper nouns were capitalized, and not in the dictionary. This would include misspelled words at the start of sentences, and I decided this was probably slightly more likely given some basic training data I used, so I opted to only consider words proper nouns if they didn't come directly after a piece of sentence ending punctuation.

Finally I had to create some testing data. I tried a few different approaches. The first was a test file called 'test.txt' which contained a number of misspellings and edge cases I wanted to handle. The second was 'lotr.txt', an excerpt from the first Lord of the Rings book, which gave me a good idea of the error rate given a real piece of text with proper nouns and punctuation. The last was the 'ErrorGenerator' test class, which added random errors into a piece of text. I used this on the dictionary and excerpt to see if the original word was generally one of the words suggested. 

## Limitations and Improvements

While the accuracy of this program is decent, there are a number of fairly obvious improvements that could be made based on the usage context. 
* A frequency metric would help to better suggest for words with a lot of similar options in the dictionary.
* Improving the parser to better understand context and non-letter characters would be helpful if the input involves a lot of complex/nonstandard punctuation.
* Implementing a more efficient third-party algorithm would be worthwhile if the dictionary size will increase.
* NLP could be used to better identify proper nouns or improve algorithmic suggestion accuracy.
* Finding better and more robust test data would help isolate issues with the current implementation.


# Included Instructions

## Make a spell checker!

Write a program that checks spelling. The input to the program is a dictionary file containing a list of valid words and a file containing the text to be checked.

The program should run on the command line like so:

```text
<path to your program> dictionary.txt file-to-check.txt
# output here
```

Your program should support the following features (time permitting):

- The program outputs a list of incorrectly spelled words.
- For each misspelled word, the program outputs a list of suggested words.
- The program includes the line and column number of the misspelled word.
- The program prints the misspelled word along with some surrounding context.
- The program handles proper nouns (person or place names, for example) correctly.


## Additional information

- The formatting of the output is up to you, but make it easy to understand.
- The dictionary file (`dictionary.txt` in the example above) is always a plain text file with one word per line.
- You can use the `dictionary.txt` file included in this directory as your dictionary.
- The input file (`file-to-check.txt` in the example above) is a plain text file that may contain full sentences and paragraphs.
- You should come up with your own content to run through the spell checker.
- Use any programming language, but extra credit for using Java or Kotlin.
- Feel free to fork the repo and put your code in there or create a new blank repo and put your code in there instead.
- Send us a link to your code and include instructions for how to build and run it.
- Someone from Voze will review the code with you, so be prepared to discuss your code.
