
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

// Categorize them according to “animal”, “artifact”, etc.
case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)



// mapping words-in-Holmes-citableNodes <—> noun-categories

// A story-by-story histogram of noun-categories might be interesting, as would a histogram of category-occurrences within a story
