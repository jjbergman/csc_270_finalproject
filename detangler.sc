import scala.io.Source
import edu.holycross.shot.cite._
import java.io._

case class IndexedLine(text:String, index:Int)
case class ChapterHeading(title:String, index:Int)
case class BookPara(chapterName:String, text:String, index:Int)

val filePath:String = "sherlock.txt"
def saveString(s:String, filePath:String = filePath, fileName:String = "saved_sherock.txt"):Unit = {
  val pw = new PrintWriter(new File(filePath + fileName))
  for (line <- s.lines){
    pw.append(line)
    pw.append("\n")
  }
  pw.close
}

val filepath:String = "sherlock.txt"
val myLines:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter( _.size > 0 )

// Grab line numbers

val indexedFileLines:Vector[IndexedLine] = myLines.zipWithIndex.map( ln => {
  new IndexedLine(ln._1, ln._2)
})

// Filter out chapter headings

val chapters:Vector[ChapterHeading] = {
  indexedFileLines.filter(_.text.startsWith("Adventure")).map(c => {
    val index:Int = c.index
    val newTitle:String = c.text.replaceAll("Adventure ","chpt_")
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

val betterABTLC:Vector[BookPara] = allButTheLastChapter.zipWithIndex.map( a => {
  // each "a" is a (BookPara, Int)
  val thisIndex:Int = a._2 + 1
  val oldPara:BookPara = a._1
  val oldChap:String = oldPara.chapterName
  val oldText:String = oldPara.text
  BookPara(oldChap, oldText, thisIndex)
})

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
  val myUrn:String = "urn:cts:fuTexts:doyle.holmes.fu:"
  allChapterLines.map( cl => {
    s"${myUrn}${cl.chapterName}.${cl.index}#${cl.text}"
  })
}

val stringToSave:String = savableLines.mkString("\n")

saveString(stringToSave)



