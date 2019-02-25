import scala.io.Source
import edu.holycross.shot.cite

val filepath:String = "sherlock.txt"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)

val wordVec:Vector[String] = {
  val bigString:String = myBook.mkString(" ")
  val noPunc:String = bigString.replaceAll("""[,.’‘'?:;!"”“_-]""",""  ).replaceAll(" +"," ")
  val tokenizedVector:Vector[String] = noPunc.split(" ").toVector.filter( _.size > 0)
  tokenizedVector
}

val wordMap:Map[String,Vector[String]] = wordVec.groupBy(w => w)
val quantMap:Map[String,Int] = wordMap.map(m=>(m._1, m._2.size))
val mapVec:Vector[(String,Int)] = quantMap.toVector
val wordHisto = mapVec.sortBy(_._2).reverse
val uniqueWords:Vector[String] = wordHisto.map(_._1)


val dictpath:String = "/vagrant/csc_270_finalproject/words.txt"
val dictEntries:Vector[String] = Source.fromFile(dictpath).getLines.toVector.filter(_.size > 0)

val badWords:Vector[String] = uniqueWords.filter( w => {
  (dictEntries.contains(w.toLowerCase) == false) &
    (dictEntries.contains(w) == false)
})

for (w<-badWords) {
   println(w)
  }
