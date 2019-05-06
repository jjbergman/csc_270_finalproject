
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
val filepath:String = "attic/sherlock.txt"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)


def tokenizeString(s:String): Vector[String] = {
  val noPunc:String = s.replaceAll("""[,.’‘'?:;!"”“_-]""",""  ).replaceAll(" +"," ").replaceAll("'s"," ").replaceAll("æ"," ")
  val tokenizedVector:Vector[String] = noPunc.split(" ").toVector.filter( _.size > 0)
  tokenizedVector
}


val nounlist:String = "attic/wordnet/nounlist.txt"
val nounVec:Vector[String] = Source.fromFile(nounlist).getLines.toVector.filter(_.size > 0)


// Find the location of each noun in the sherlock text and give the citation

val myCexFile:String = "sherlock.cex"

def loadLibrary(fp:String):CiteLibrary = {
	val library = CiteLibrary(Source.fromFile(fp).getLines.mkString("\n"),"#",",")
	library
}

def saveString(s:String, fileName:String):Unit = {
	val pw = new PrintWriter(new File(fileName))
	for (line <- s.lines){
		pw.append(line)
		pw.append("\n")
	}
	pw.close
}

lazy val lib = loadLibrary(myCexFile)
lazy val tr = lib.textRepository.get
lazy val doyleCorpus = tr.corpus

// Categorize them according to “animal”, “artifact”, etc. mapping words-in-Holmes-citableNodes <—> noun-categories
case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class NounCategorization(category:String, noun:String, index:Int)

// Grab line numbers

val indexedNounVec:Vector[IndexedLine] = nounVec.zipWithIndex.map( ln => {
  new IndexedLine(ln._1, ln._2)
})

// Filter out category headings

val categoryHeaders:Vector[ChapterHeading] = {
  indexedNounVec.filter(_.text.startsWith("NOUN.")).map(c => {
    val index:Int = c.index
    val newTitle:String = c.text.replaceAll("NOUN.","")
    new ChapterHeading(newTitle, index)
  })
}

val actualNouns:Vector[IndexedLine] = {
  indexedNounVec.filter(_.text.startsWith("NOUN.") == false )
}

// find where each category begins and ends
val categoryRanges:Vector[Vector[ChapterHeading]] = categoryHeaders.sliding(2,1).toVector


val allButTheLastCategory:Vector[NounCategorization] = categoryRanges.map(cr => {
  val thisCat:ChapterHeading = cr.head
  // the line-number in the original file where this category begins
  val thisCatLineNum:Int = thisCat.index

  val nextCat:ChapterHeading = cr.last
  // the line-number in the original file where the next category begins
  val nextCatLineNum:Int = nextCat.index

  // the nouns in the text that belong to this category
  val categoriedNounLines:Vector[IndexedLine] = {
    actualNouns.filter( il => {
      (( il.index > thisCatLineNum) & (il.index < nextCatLineNum ) )
    })
  }

  val categorizedNouns:Vector[NounCategorization] = categoriedNounLines.zipWithIndex.map (cp => {
    val thisIndex:Int = cp._2 + 1
    new NounCategorization( thisCat.title, cp._1.text, thisIndex)
  })
  // return that value
  categorizedNouns
}).flatten

val theLastCategory:Vector[NounCategorization] = {
  val lastCatHeading:String = categoryRanges.last.last.title
  // where the last category begins
  val lastCatLineNum:Int = categoryRanges.last.last.index

  // filter out all nouns that are before the last category
  val categoriedNounLines:Vector[IndexedLine] = {
    actualNouns.filter( il => {
      (il.index > lastCatLineNum)
    })
  }

  // attach the title of the last category to each
  val categorizedNouns:Vector[NounCategorization] = categoriedNounLines.map( cp => {
    new NounCategorization( lastCatHeading, cp.text, cp.index)
  })

  categorizedNouns
}


// print the categorized nouns
val allCategorizedNouns:Vector[NounCategorization] = {
  allButTheLastCategory ++ theLastCategory
}

def dePunc(s:String):String = s.replaceAll("""[,.’‘'?:;!"”“_-]"""," ").replaceAll(" +"," ")


// look at an example passage
val u:CtsUrn = CtsUrn("urn:cts:fuTexts:doyle.holmes.fu:I.2")
val p:CitableNode = (doyleCorpus ~~ u).nodes.head

val toks:Vector[String] = dePunc(p.text).split(" ").toVector

val filteredTokens:Vector[String] = {
  toks.filter( t => {
    val tt:String = t.toUpperCase
    allCategorizedNouns.filter(_.noun == tt).size > 0
  })
}

val cats:Vector[String] = {
  filteredTokens.map( t => {
    allCategorizedNouns.filter(_.noun == t.toUpperCase).head.category
  })
}


// do this to the entire corpus
val mappedCorpus:Corpus = {
  val nodes:Vector[CitableNode] = doyleCorpus.nodes.map( n => {
     val u:CtsUrn = n.urn
     val toks:Vector[String] = dePunc(n.text).split(" ").toVector
     val filteredTokens:Vector[String] = {
        toks.filter( t => {
            val tt:String = t.toUpperCase
            allCategorizedNouns.filter(_.noun == tt).size > 0
        })
     }
     val cats:Vector[String] = {
        filteredTokens.map( t => {
          allCategorizedNouns.filter(_.noun == t.toUpperCase).head.category
        })
      }
      val newText = cats.mkString(" ") + " "
      val newUrn = u.addExemplar("categorized")
      CitableNode(newUrn,newText)
  })
  Corpus(nodes)
}

val newCex:String = mappedCorpus.cex("#")
