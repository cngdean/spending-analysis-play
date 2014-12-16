import models.{Transaction, ProcessFiles, Category, CategoryMapper}
import org.specs2.mutable._

class TransactionSpec extends Specification {
  " Transactions" should {

    "have a transaction" in {
      val tp = ProcessFiles.processQfx("test/resources/test_qfxFile.qfx", "test/resources/test_regexfile.txt")
      val t = tp.head
      t.amount must === (BigDecimal("-90.00"))
      tp.tail.head.amount must === (BigDecimal("-29.31"))
    }

    "have a qfx transaction in directories" in {
      val tp = ProcessFiles.processDirectory("test/resources/", "test/resources/test_regexfile.txt")
      val t = tp.head
      t.amount must === (BigDecimal("-90.00"))
      tp.tail.head.amount must === (BigDecimal("-29.31"))
    }

    "have a csv transaction in directories" in {
      val tp = ProcessFiles.processCsv("test/resources/test_csvFile.csv", "test/resources/test_regexfile.txt")
      val t = tp.head
      t.amount must === (BigDecimal("-335.98"))
      tp.tail.head.amount must === (BigDecimal("-25.85"))
      // de dupe test
      tp.size must === (3)
    }

    "ignore excluded category transactions" in {
      val tp = ProcessFiles.processDirectory("test/resources/", "test/resources/test_regexfile.txt")
      tp.find(_.note.equals("PETCO PET MARKET")) should not be empty
      tp.find(_.note.equals("ExcludeMe")) should be empty
    }
  }
}

