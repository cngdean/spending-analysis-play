import models.{Transaction, ProcessFiles, Category, CategoryMapper}
import org.specs2.mutable._

class TransactionSpec extends Specification {
  " Transactions" should {

    "have a transaction" in {
      val tp = ProcessFiles.process("test/resources/test_qfxFile.qfx", "test/resources/test_regexfile.txt")
      val t = tp.head
      t.amount must === (BigDecimal("-90.00"))
      tp.tail.head.amount must === (BigDecimal("-29.31"))
    }

    "have a transaction in directories" in {
      val tp = ProcessFiles.processDirectory("test/resources/", "test/resources/test_regexfile.txt")
      val t = tp.head
      t.amount must === (BigDecimal("-90.00"))
      tp.tail.head.amount must === (BigDecimal("-29.31"))
    }

    "ignore excluded category transactions" in {
      val tp = ProcessFiles.processDirectory("test/resources/", "test/resources/test_regexfile.txt")
      tp.find(_.note.equals("PETCO PET MARKET")) should not be empty
      tp.find(_.note.equals("ExcludeMe")) should be empty
    }
  }
}

