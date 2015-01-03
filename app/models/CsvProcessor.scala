package models

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
}
