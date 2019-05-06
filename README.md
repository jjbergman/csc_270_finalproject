# An Analysis of *The Adventures of Sherlock Holmes* by Sir Arthur Conan Doyle

### Overview
This project was undertaken at Furman University for the CSC-270 class, "Computational Approaches to the Humanities", in the spring of 2019. The repository contains all of the project files that were used. The public domain text used for this project comes from the [Project Gutenberg](http://www.gutenberg.org/ebooks/48320?msg=welcome_stranger) e-book website.

### Contents
Within this repository, there are several files that can be used to analyze The Adventures of Sherlock Holmes.

- sherlock.cex, a cex version of the text that cites each paragraph using a CtsUrn, along with a categorized.cex and both.cex, which are the result of the analysis script
- cite.html, a web page that can be used to examine and analyze cex files using n-grams[^note2]
- a scripts folder that contains an analysis.sc file, which uses the scala language. It displays the nouns in Sherlock Holmes according to their semantic category
- a docx folder that contains a Microsoft word version of the text
- a md folder that contains a markdown version of the text
- an html version of the text with accompanying style choices for online display. The stylistic choices were meant to evoke a typewritten version of the text
- build.sbt, which builds the version of scala used for this project

Additionally, there are several scripts in the /attic folder that perform analysis on the text, such as spellcheck.sc, readinglevel.sc[^note1], and ngram_script.sc[^note2], which performs an ngram analysis of Sherlock Holmes (which can also be done using the cite.html application). There are also several files that could be of some use, such as mdwriter.sc which can convert from a cex to md file.

##### Acknowledgements
Thank you to Dr. Blackwell for his help in this project, as well as my classmates, especially Ian Bardwell, for their support and ideas.

[^note1]: For more background look at https://en.wikipedia.org/wiki/Readability. The script does its analysis based on the Flesh-Kincaid Readability score.
[^note2]: For additional information on ngrams- https://en.wikipedia.org/wiki/N-gram
