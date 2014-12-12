package models

/**
 * Created by carmen on 12/4/14.
 */
object TransactionSummary {
  def spendingByMonth(txns: List[Transaction]): Map[String,BigDecimal] = {
    for ((month,txns) <- txns.groupBy(_.month)) yield (month, txns.foldLeft(BigDecimal(0))((acc, x) => acc + x.amount))
  }

  //Map("date" -> "2014-09-03", "amount" -> "233"),

  def spendingByMonthJSON(txns: List[Transaction]): List[Map[String, String]] = {
    val l = for ((month,txns) <- txns.groupBy(_.month)) yield (Map("date" -> month, "amount" -> txns.foldLeft("0")((acc, x) => (BigDecimal(acc) + x.amount).toString())))
    l.toList
  }


  def splitByMonth(txns: List[Transaction]): Map[String,List[Transaction]] = txns.groupBy(_.month)

  def spendingByCategory(txns: List[Transaction]): Map[Category,BigDecimal] = {
    for ((category,txns) <- txns.groupBy(_.category)) yield (category, txns.foldLeft(BigDecimal(0))((acc, x) => acc + x.amount))
  }


}
