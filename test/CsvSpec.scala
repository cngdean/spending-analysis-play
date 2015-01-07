import models.{CsvProcessor, ProcessFiles}
import org.specs2.mutable.Specification

/**
 * Created by carmen on 1/7/15.
 */
class CsvSpec extends Specification {
  " CSV Processor" should {

    "have a transaction" in {
      def txnlines = List(
        """
          "07/27/2014","-60.90","*","","KING SOOPERS #3100 BRONX"
        """.stripMargin,
        """
          "07/27/2014","-65.24","*","","TARGET 03342616 DOVER UK"
        """.stripMargin
      )

      def txns = CsvProcessor.nextTxn("testfile", txnlines, "regexfile")
      println(txns)
      1 must === (1)
    }

  }
}


