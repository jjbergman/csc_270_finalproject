import scala.io.Source
import edu.holycross.shot.cite._

val filepath:String = "/vagrant/csc_270_finalproject/sherlock.txt"
lazy val myLines:Vector[String] = Source.fromFile(filepath).getLines.toVector

val urn1:CtsUrn = CtsUrn("urn:cts:namespace:group.work.vers:1.1")

// I want to do "urn:cts:csc270:doyle.holmes.gutenberg:adventure#.paragraph#"
