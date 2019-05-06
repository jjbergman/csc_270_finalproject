# Analyzing *The Adventures of Sherlock Holmes*

####Objectives
This analysis.sc script does several things as a means of categorizing nouns. It is useful for displaying the different objects, people, and ideas represented in each investigation. This process can be used to simplify a text to find a basic syntactical and semantic structure of a story.

- First, it creates a list of every noun in the text of Sherlock Holmes, using a noun list from the website [WordNet](https://wordnet.princeton.edu/).
- Then, through a series of maps and filters, the noun list is detangled to match every noun on the list with its category heading as a vector.
- The nouns are then filtered through the categorized list to print out just the category in which that noun belongs.
- A single passage is used as an exemplar, the first full paragraph of the text.
- This is then applied to the rest of the corpus.
- Finally, a CEX file of the categorized text is generated. 

####Results
The results of the .

I have confidence in these results because

####Limitations
This analysis is only done for the nouns of Sherlock Holmes, as this was of primary interest to the research project. A full analysis of the semantic weight of each word in the text could prove fruitful and would not be difficult to do based off the materials in this repository- simply use the entire list from WordNet rather than just the nouns and change the headings of each of the categories to include "NOUNS." at the beginning.

Another problem with the script is that certain words, such as "spoke", appear on the noun list, despite being the verb form of the word. I have chosen to include this in the results still, but to fully use this categorizer, one would need a lemmatized version of the text that first categorizes each word by part of speech.


####Ideas for Further Research
Beyond analyzing the entirety of the text instead of simply the nouns, there are several other applications for this analysis script.

A story-by-story histogram of noun-categories could prove interesting.

as would a histogram of category-occurrences within a story
