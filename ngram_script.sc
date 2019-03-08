import scala.io.Source
import edu.holycross.shot.cite._


// EDIT THIS to point to your file!!!
val filepath:String = "/vagrant/csc_270_finalproject/sherlock.txt"
// Get the file as a vector of lines, ignoring empty lines
val myLines:Vector[String] = Source.fromFile(filepath).getLines.toVector.filter( _.size > 0 )

val wordVec:Vector[String] = {
  val bigString:String = myLines.mkString(" ")
  val noPunc:String = bigString.replaceAll("""[,.’‘'?:;!"”“_-]""",""  ).replaceAll(" +"," ")
  val tokenizedVector:Vector[String] = noPunc.split(" ").toVector.filter( _.size > 0)
  tokenizedVector
}

/* How to (a) remove punctuation, and (b) tokenize by word in a short chunk */
val myTokenizedLines:Vector[Vector[String]] = myLines.map( aLine => {
  val noPunc:String = aLine.replaceAll("""[,.?;":!)(]""",""  ).replaceAll(" +"," ")
  val wordVec:Vector[String] = noPunc.split(" ").toVector
  wordVec
})

/* A thing you can do */
val newVec:Vector[String] = wordVec
val mySlided:Vector[Vector[String]] = newVec.sliding(3,1).toVector
val grouped:Map[Vector[String],Vector[Vector[String]]] = mySlided.groupBy(i => i)
val madeIntoAVectorBecauseMapsAreHard:Vector[(Vector[String], Vector[Vector[String]])] = grouped.toVector
// rename this to make it shorter ;-)
val madeVec = madeIntoAVectorBecauseMapsAreHard

// Do that to all items in madeVec

val ng:Vector[(String, Int)] = madeVec.map(mv => {
    val s:String = mv._1.mkString(" ")
    val i:Int = mv._2.size
    (s,i)
}).sortBy(_._2)

// See the results
for (n <- ng) {
  println(s""" "${n._1}" occurs ${n._2}""")
}
