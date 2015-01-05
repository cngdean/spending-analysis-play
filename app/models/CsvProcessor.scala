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

  val dateFormat = new SimpleDateFormat("MM/dd/yyyy")

  def nextTxn(filename: String, lines: List[String], regexfile: String):List[Transaction] = {
    if (lines.isEmpty) Nil
    else {
      def line = lines.head.replaceAll(""""""", "")
      def values: Array[String] = line.split(",")
      try {
        def id = line.toLowerCase.replaceAll(" ", "")
        def note = values(3)
        def memo = ""
        def amount = BigDecimal(values(1))
        def date = dateFormat.parse(values(0))
        def category = CategoryMapper(regexfile).findCategory(note)
        Transaction(id, note, memo, amount, date, category, filename) :: nextTxn(filename, lines.tail, regexfile)
      }
      catch {
        case e:Exception => println("Error handling file: " + filename + "line:\n\t" + line + " " + e + " " + (values mkString " "))
          nextTxn(filename, lines.tail, regexfile)
      }
    }
  }

  def process(process: Processor, filename: String, regexfile: String): List[Transaction] = {
    try {

      val txns = for {
        line <- scala.io.Source.fromFile(filename).getLines.toList
        values: Array[String] = line.split(",")
        id = line.toLowerCase.replaceAll(" ", "")
        note = values(4)
        memo = ""
        amount = BigDecimal(values(1))
        date = dateFormat.parse(values(0))
        category = CategoryMapper(regexfile).findCategory(values(4))
      } yield Transaction(id, note, memo, amount, date, category, filename)

      ProcessFiles.dedupe(txns)
    }
    catch {
      case _ => println("Error handling file: " + filename)
        return Nil
    }
  }
}
