import scala.io.Source
import edu.holycross.shot.cite

// Fun with Collections

//val myList = List("a","b","c")
//val myArray = Array("a","b","c")
//val myVector = Vector("a","b","c")

// Rename the file to sherlock Holmes
val filepath:String = "texts/sherlock.txt"
val myBook:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter(_.size > 0)



println(s"\n\nmyBook has ${myBook.size} lines.")

//making a roman numeral map

//val romanNum:Map[Int,String] = Map( 1->"I", 2->"II", 3->"III", 4->"IV", 5->"V", 6->"VI", 7->"VII", 8->"VIII", 9->"IX", 10->"X", 11->"XI", 12->"XII", 13->"XIII", 14->"XIV", 15->"XV", 16->"XVI", 17->"XVII", 18->"XVIII", 19->"XIX", 20->"XX")

//val testInt1=10
//println(s"""${testInt1}= X: ${romanNum(10) == "X"}""")

//making a character histogram

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

println(s"\n\n----------\nThere are ${wordHisto.size} unique words.\n---------\n")

/* Spell-check */
val dictpath:String = "/vagrant/csc_270_finalproject/words.txt"
val dictEntries:Vector[String] = Source.fromFile(dictpath).getLines.toVector.filter(_.size > 0)

val badWords:Vector[String] = uniqueWords.filter( w => {
  (dictEntries.contains(w.toLowerCase) == false) &
    (dictEntries.contains(w) == false)
})

for (w<-badWords) {
   println(w)
  }
/*

for( wh <- wordHisto) println(s"${wh._1} = ${wh._2}")

println(s"\n\n---------\nThere are ${wordVec.size} total words.\n-------\n")

// filtering for unique words
wordHisto.filter(_._2 == 1)

*/

// .size.toDouble / wordVec.size.toDouble * 100
// to get a map of all words (change from String,Int to String) do wordHisto.map(_._1)
// Summary of Zipf's Law: Zipf's Law is a mathematical distribution model that describes many different phenomena in science and social sciences. The model states that an item's frequency and rank are inverse, such that the second most common word occurs half as frequently as the most common, the third one-third as frequently, etc.

/*
val maxNum:Int = wordHisto.head._2
val divisor:Int = maxNum / 75

for (wh <- wordHisto.take(20)) {
  val stars:Int = wh._2 / divisor
  if (stars > 0) {
      println()
      print(wh._1.padTo(20, " ").mkString(""))
      for (x <- 1 to stars) print("*")
    }
}
*/

//another way to do a histogram
//val sherlockLines = io.Source.fromFile("texts/sherlock.txt").getLines.toVector
//val sherlockWords = sherlockLines.flatMap(_.split("\\W+"))
//val sherlockHist = sherlockWords.groupBy(_.length).mapValues(_.length)
//val sherlockLengths = (1 to sherlockHist.keys.max).map(sherlockHist.getOrElse(_, 0))
//for ((count, length) <- sherlockLengths.zipWithIndex)
//  println(f"${length+1}%2s: ${"#" * (count/100)}")
