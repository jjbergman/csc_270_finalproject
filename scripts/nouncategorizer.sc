// Grab all nouns from the Holmes text
import scala.io.Source
import edu.holycross.shot.cite
import java.io._

val filepath:String = "/vagrant/csc_270_finalproject/sherlock.txt"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)

val wordVec:Vector[String] = {
  val bigString:String = myBook.mkString(" ")
  val noPunc:String = bigString.replaceAll("""[,.’‘'?:;!"”“_-]""",""  ).replaceAll(" +"," ").replaceAll("'s"," ").replaceAll("æ"," ")
  val tokenizedVector:Vector[String] = noPunc.split(" ").toVector.filter( _.size > 0)
  tokenizedVector
}

val dictpath:String = "/vagrant/csc_270_finalproject/attic/wordnet/nounlist.txt"
val dictEntries:Vector[String] = Source.fromFile(dictpath).getLines.toVector.filter(_.size > 0)

val nouns:Vector[String] = wordVec.filter( w => {
  (dictEntries.contains(w.toUpperCase) == true)
})

for (w<-nouns) {
   println(w)
  }

// Categorize them according to “animal”, “artifact”, etc.
case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)



// mapping words-in-Holmes-citableNodes <—> noun-categories

// A story-by-story histogram of noun-categories might be interesting, as would a histogram of category-occurrences within a story
