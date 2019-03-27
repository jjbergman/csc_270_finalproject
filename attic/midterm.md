# Midterm Assignment

## N-grams

An N-gram is a sequence of items of length *n* in a text, list, or collection of data. When analyzing a text, an n-gram can be a series of words, letters, or linguistic units that occur next to each other. The length *n* can be any number of terms, though the bigram and trigram seem most common. N-grams are created by looking at every item in a data set in a sliding fashion, such that every possible sequence is examined. Then, depending on what the researcher is looking for, the sequences can be organized into a histogram to see which sequences are the most common.

There are several applications for n-grams and are used in several fields to examine different types of data. One application for examining a text is to see which words form a single entity, e.g. "Los Angeles" or "The Lonely Island". Another possible application is for predicting the next word that will come, i.e. looking at the most common n-grams to predict a sequence of words. An application in the sciences is looking at DNA sequencing. An n-gram can be used to figure out which series of base pairs occur most frequently next to each other. N-grams can also be used in the business world to examine what words are frequently searched together on line to create targeted advertising. Another use could be looking at purchase histories to predict what someone will want or buy next.

##### How to find an N-gram

As stated above, n-grams are found by looking at every item in a data set within the sequence of items around it. These sequences are created by sliding down the data set, creating a set for each sequence. For example, a trigram of natural numbers would be (1,2,3), (2,3,4,), (3,4,5), etc.

##### Methods useful in analyzing a text for N-grams
* .sliding - the main method for creating an n-gram, slides down each word creating a sequence of words.
* .toVector - creates a vector out of a map to make the coding and analysis easier.
* .sortBy - used to look at the frequency of each n-gram in order.
* .filter - sort out the words and punctuation marks that are uninteresting in an n-gram.

## Collaboration
My colleague Ian informed me to include a "sortBy" command at the end of my n-gram script, so that I could look at the trigrams that were created based on their frequency. He also noticed that I had not replaced every type Int with String in my N-gram scripts, so he told me to ensure I did this so as not to get any type mismatches. Using his help, I have a file called __ngram_script.sc__, which creates an trigram of my text showing the most frequent trigrams at the end.
