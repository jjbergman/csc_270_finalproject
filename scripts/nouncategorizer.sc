
import scala.io.Source
import edu.holycross.shot.cite
import java.io._
import scala.collection.mutable.LinkedHashMap
import edu.holycross.shot.scm._
import edu.holycross.shot.cite._
import edu.holycross.shot.citeobj._
import edu.holycross.shot.ohco2._
import edu.holycross.shot.seqcomp._
import edu.furman.classics.citealign._
import java.util.Calendar

// Grab all nouns from the Holmes text
val filepath:String = "/vagrant/csc_270_finalproject/sherlock.txt"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)

val wordVec:Vector[String] = {
  val bigString:String = myBook.mkString(" ")
  val noPunc:String = bigString.replaceAll("""[,.’‘'?:;!"”“_-]""",""  ).replaceAll(" +"," ").replaceAll("'s"," ").replaceAll("æ"," ")
  val tokenizedVector:Vector[String] = noPunc.split(" ").toVector.filter( _.size > 0)
  tokenizedVector
}

val nounlist:String = "/vagrant/csc_270_finalproject/attic/wordnet/nounlist.txt"
val dictEntries:Vector[String] = Source.fromFile(nounlist).getLines.toVector.filter(_.size > 0)

val nouns:Vector[String] = wordVec.filter( w => {
  (dictEntries.contains(w.toUpperCase) == true)
})

for (w<-nouns) {
   println(w)
  }

// Find the location of each noun in the sherlock text and give the citation

val myCexFile:String = "sherlock.cex"

def loadLibrary(fp:String):CiteLibrary = {
	val library = CiteLibrary(Source.fromFile(fp).getLines.mkString("\n"),"#",",")
	library
}

def loadFile(fp:String):Vector[String] = {
	Source.fromFile(fp).getLines.toVector
}

def saveString(s:String, filePath:String = "", fileName:String = ""):Unit = {
	val pw = new PrintWriter(new File(filePath + fileName))
	for (line <- s.lines){
		pw.append(line)
		pw.append("\n")
	}
	pw.close
}

lazy val lib = loadLibrary(myCexFile)
lazy val tr = lib.textRepository.get
lazy val doyleCorpus = tr.corpus

val citableWords:Vector[(String, Vector[CtsUrn])] = {
  nouns.map(w => {
    val filteredNodes:Vector[CitableNode]= doyleCorpus.nodes.filter(_.text.contains(w))
    val justUrns:Vector[CtsUrn] = filteredNodes.map(_.urn)
    (w, justUrns)
  })
}

// saveString(nouns) ?

// Categorize them according to “animal”, “artifact”, etc. mapping words-in-Holmes-citableNodes <—> noun-categories
case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)

val theNounList:Vector[String] = Source.fromFile(nounlist).getLines.toVector.filter( _.size > 0 )

// Grab line numbers

val indexedFileLines:Vector[IndexedLine] = theNounList.zipWithIndex.map( ln => {
  new IndexedLine(ln._1, ln._2)
})

// Filter out chapter headings

val chapters:Vector[ChapterHeading] = {
  indexedFileLines.filter(_.text.startsWith("NOUN")).map(c => {
    val index:Int = c.index
    val newTitle:String = c.text.replaceAll("NOUN ","")
    new ChapterHeading(newTitle, index)
  })
}

val realParagraphs:Vector[IndexedLine] = {
  indexedFileLines.filter(_.text.startsWith("NOUN") == false )
}


// find where each chapter begins and ends!
val chapterRanges:Vector[Vector[ChapterHeading]] = chapters.sliding(2,1).toVector


val allButTheLastChapter:Vector[BookPara] = chapterRanges.map(cr => {
  val thisChapt:ChapterHeading = cr.head
  // the line-number in the original file where this chapter begins
  val thisChaptLineNum:Int = thisChapt.index

  val nextChapt:ChapterHeading = cr.last
  // the line-number in the original file where the next chapter begins
  val nextChaptLineNum:Int = nextChapt.index

  // the paragraphs of my text that belong to this chapter
  val chapterParas:Vector[IndexedLine] = {
    realParagraphs.filter( il => {
      (( il.index > thisChaptLineNum) & (il.index < nextChaptLineNum ) )
    })
  }

  val bookParas:Vector[BookPara] = chapterParas.zipWithIndex.map (cp => {
    val thisIndex:Int = cp._2 + 1
    new BookPara( thisChapt.title, cp._1.text, thisIndex)
  })
  // return that value
  bookParas
}).flatten

val theLastChapter:Vector[BookPara] = {
  val lastChaptHeading:String = chapterRanges.last.last.title
  // where the last chapter begins
  val lastChaptLineNum:Int = chapterRanges.last.last.index

  // filter out all paragraphs that are before the last chapter
  val chapterParas:Vector[IndexedLine] = {
    realParagraphs.filter( il => {
      (il.index > lastChaptLineNum)
    })
  }

  // attach the title of the last chapter to each
  val bookParas:Vector[BookPara] = chapterParas.map( cp => {
    new BookPara( lastChaptHeading, cp.text, cp.index)
  })

  bookParas
}

val allChapterLines:Vector[BookPara] = {
  allButTheLastChapter ++ theLastChapter
}

// map citableWords into this
val catPreMap:Vector[(String, Vector[(String, String, Int])] = {
allChapterLines.groupBy(_._1).toVector
}

val catMap:Vector[(String,Vector[String])] = {
catPreMap.map( cpm => {
cpm.map(_._2)
})
}



// A story-by-story histogram of noun-categories might be interesting, as would a histogram of category-occurrences within a story
