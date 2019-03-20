import scala.io.Source
import edu.holycross.shot.cite._

case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)

val filepath:String = "sherlock.txt"
val myLines:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter( _.size > 0 )

// Grab line numbers

val indexedFileLines:Vector[IndexedLine] = myLines.zipWithIndex.map( ln => {
  new IndexedLine(ln._1, ln._2)
})

// Filter out chapter headings

val chapters:Vector[ChapterHeading] = {
  indexedFileLines.filter(_.text.startsWith("Chapter")).map(c => {
    val index:Int = c.index
    val newTitle:String = c.text.replaceAll("Chapter ","chpt_")
    new ChapterHeading(newTitle, index)
  })
}

val realParagraphs:Vector[IndexedLine] = {
  indexedFileLines.filter(_.text.startsWith("Chapter") == false )
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

  val bookParas:Vector[BookPara] = chapterParas.map (cp => {
    new BookPara( thisChapt.title, cp.text, cp.index)
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

val savableLines:Vector[String] = {
  allChapterLines.map(cl=> {
    s"${cl.chapterName}.${cl.index}#${cl.text}"
  })
}
