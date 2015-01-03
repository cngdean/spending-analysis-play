package models

import java.text.SimpleDateFormat

/**
 * Created by carmen on 1/3/15.
 */
object CsvProcessor {

  class CsvField

  case class txnId(idx: Int) extends CsvField

  case class txnMemo(idx: Int) extends CsvField

  case class txnAmount(idx: Int) extends CsvField

  case class txnDate(idx: Int) extends CsvField

  case class txnCategory(idx: Int) extends CsvField

  case class txnSource(idx: Int) extends CsvField

  case class Processor(fields: List[CsvField])

  def process(process: Processor, filename: String, regexfile: String): List[Transaction] = {
    try {
      val dateFormat = new SimpleDateFormat("MM/dd/yyyy")
      val catMapper = CategoryMapper(regexfile)

      val txns = for {
        line <- scala.io.Source.fromFile(filename).getLines.toList
        values: Array[String] = line.split(",")
        id = line.toLowerCase.replaceAll(" ", "")
        note = values(4)
        memo = ""
        amount = BigDecimal(values(1))
        date = dateFormat.parse(values(0))
        category = catMapper.findCategory(values(4))
      } yield Transaction(id, note, memo, amount, date, category, filename)

      ProcessFiles.dedupe(txns)
    }
    catch {
      case _ => println("Error handling file: " + filename)
        return Nil
    }
  }
}
