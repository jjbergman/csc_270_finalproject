# Analyzing *The Adventures of Sherlock Holmes*

####Objectives
This **analysis.sc** script does several things as a means of categorizing nouns. It is useful for displaying the different objects, people, and ideas represented in each investigation. This process can be used to simplify a text to find a basic syntactical and semantic structure of a story. The script also turns the text into a library, and includes some commands for interacting with the text as a Corpus, including a string search and a token search.


1. First, it creates a list of every noun in the text of Sherlock Holmes, using a noun list from the website [WordNet](https://wordnet.princeton.edu/).
2. Then, through a series of maps and filters, the noun list is detangled to match every noun on the list with its category heading as a vector.
3. The nouns are then filtered through the categorized list to print out just the category in which that noun belongs.
4. A single passage is used as an exemplar, the first full paragraph of the text.
5. This is then applied to the rest of the corpus.
6. Finally, a CEX file of the categorized text is generated.

####Results
The script creates an output that shows each type of noun in its category. The filteredTokens command creates a Vector[String] that shows just the nouns. So, for passage urn:cts:fuTexts:doyle.holmes.fu:I.2, it is (Sherlock, Holmes, eyes, emotion, lover, spoke, things, men, reasoner, might, emotion). Then, that text is categorized in the cats command, which is also a Vector[String] that turns these nouns into (PERSON, PERSON, COGNITION, FEELING, PERSON, ARTIFACT, POSSESSION, GROUP, PERSON, ATTRIBUTE, FEELING). The script boils down the text of the story into something the machine can analyze and break down more easily.

These results can be agreed upon somewhat confidently because they create a verifiable output upon close reading. However, there are some limitations to the output, as will be discussed below.

####Limitations
This analysis is only done for the nouns of Sherlock Holmes, as this was of primary interest to the research project. A full analysis of the semantic weight of each word in the text could prove fruitful and would not be difficult to do based off the materials in this repository- simply use the entire list from WordNet rather than just the nouns and change the headings of each of the categories to include "NOUNS." at the beginning.

Another problem with the script is that certain words, such as "spoke", appear on the noun list, despite being the verb form of the word. I have chosen to include this in the results still, but to fully use this categorizer, one would need a lemmatized version of the text that first categorizes each word by part of speech.

Another limitation is that not every noun in the text is included in this list, as it would be quite difficult to create a list of every single possible noun, including compound words and older words, along with alternate spellings.


####Ideas for Further Research
Beyond analyzing the entirety of the text instead of simply the nouns, there are several other applications for this analysis script.

- A story-by-story histogram of noun categories could prove interesting, as the onus of this analysis was to discover how Sherlock Holmes solves his mysteries. It would be useful to examine what types of nouns are used as his clues and which ones he relies upon to resolve each case. Perhaps he solves each case differently, or a pattern emerges.

- Another histogram of category-occurrences within a story might be interesting, as

- Finally, this analysis could be used to compare *The Adventures of Sherlock Holmes* with other stories or genres of novels. This analysis can create a basic structure of a story, including for each sentence but also for the whole story. This analysis could be used to look at how different authors write or even to create a computer generated story based on common syntax and semantic verbiage.
