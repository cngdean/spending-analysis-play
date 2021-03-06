package models

import java.text.SimpleDateFormat

import net.sf.ofx4j.io.OFXHandler
import net.sf.ofx4j.io.nanoxml.NanoXMLOFXReader
import java.util.{Calendar, Date}

/**
 * Created by carmen on 12/1/14.
 */
object ProcessFiles {

  def dedupe(txns: List[Transaction]):List[Transaction] = txns.sortBy( _.id ).groupBy{_.id}.map{_._2.head}.toList

  def processDirectory(directory: String, regexfile: String):List[Transaction] = {
      import java.io.File
      def recursiveListFiles(f: File): Array[File] = {
        val these = f.listFiles
        these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles)
      }

      val qfxFiles = recursiveListFiles(new File(directory)).filter(_.getName.endsWith(".qfx"))
      val qList = qfxFiles.map(_.getAbsolutePath).map(processQfx(_, regexfile)).flatten.toList

      val csvFiles = recursiveListFiles(new File(directory)).filter(_.getName.endsWith(".csv"))
      val cList = csvFiles.map(_.getAbsolutePath).map(processCsv(_, regexfile)).flatten.toList
      dedupe( qList ::: cList )
    }

  def processCsv(filename: String, regexfile: String):List[Transaction] = {
    CsvProcessor.nextTxn(filename, scala.io.Source.fromFile(filename).getLines.toList, regexfile)
  }

  def processQfx(filename: String, regexfile: String):List[Transaction] = {
    val reader = new NanoXMLOFXReader

    val txns = scala.collection.mutable.ListBuffer.empty[Transaction]

    var (id, note, memo, amount, date) = ("", "", "", BigDecimal(0), new Date())

    val dateFormat = new SimpleDateFormat("yyyyMMddHHmmss")

    val catMapper = CategoryMapper(regexfile)

    val handle = new OFXHandler {
      override def onElement(s: String, v: String): Unit = {
        s match {
          case ("TRNAMT") => amount = BigDecimal(v)
          case ("NAME") => note = v
          case ("FITID") => id = v
          case ("MEMO") => memo = v
          case ("DTPOSTED") => date = dateFormat.parse(v)
          case _ =>
        }
      }

      override def endAggregate(s: String): Unit = {
        if ("STMTTRN" == s) {
          val category = catMapper.findCategory(note)
          if (!category.exclude)
            txns += Transaction(id, note, memo, amount, date, category, filename)
        }
      }

      override def onHeader(s: String, s1: String): Unit = {}

      override def startAggregate(s: String): Unit = {
        if ("STMTTRN" == s) {
          note = ""
          id = ""
          amount = BigDecimal(0)
          memo = ""
        }
      }
    }

    reader.setContentHandler(handle)
    reader.parse(new java.io.FileInputStream(filename))
    // dedupe by txn id
    dedupe(txns.toList)
  }

}
