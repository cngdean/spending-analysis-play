package models

import scala.collection.SortedMap

/**
 * Created by carmen on 12/4/14.
 */
object TransactionSummary {
  def spendingByMonth(txns: List[Transaction]): Map[String,BigDecimal] = {
    for ((month,txns) <- txns.groupBy(_.month)) yield (month, txns.foldLeft(BigDecimal(0))((acc, x) => acc + x.amount))
  }

  def splitByMonth(txns: List[Transaction]): List[(String,BigDecimal)] = {
    val m:Map[String, BigDecimal] = for (month <- txns.groupBy(_.month)) yield (month._1, (month._2).foldLeft(BigDecimal(1))( (x, acc) => x + acc.amount))
    m.toList.sorted
  }

  def spendingByCategory(txns: List[Transaction]): Map[Category,BigDecimal] = {
    for ((category,txns) <- txns.groupBy(_.category)) yield (category, txns.foldLeft(BigDecimal(0))((acc, x) => acc + x.amount))
  }


}
