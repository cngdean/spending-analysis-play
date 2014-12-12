package models

object Analyze {
  def main(args: Array[String]): Unit = {
    println("Reading in QFX files ")

    def sum(txns: List[Transaction]) = {
      txns.map( (x: Transaction) => x.amount).sum
    }

    val dir = System.getProperty("user.home") + "/qfx/"
    var txns = ProcessFiles.processDirectory(dir, System.getProperty("user.home") + "/qfx/" + "regexconfig.txt")
    println(TransactionSummary.spendingByCategory(txns))

    println("Spending:" +  sum(txns.filter(_.amount < 0)))

    val after = new java.util.Date("09/01/2014")
    //    txns = txns.filter( _.date.after(after) )

    println(TransactionSummary.spendingByMonth(txns))

    println(TransactionSummary.spendingByMonth(txns))
    println("Spending:" +  sum(txns.filter(_.amount < 0)))

    println("Spending by category\n" )
    val byCat = TransactionSummary.spendingByCategory(txns)

    val by2 = for ( (cat, amount) <- byCat) yield Map(cat.categoryName -> amount.toString())
    println(by2)

//    println( for (m <- TransactionSummary.splitByMonth(txns)) yield (m._1, TransactionSummary.spendingByCategory(m._2) ))

  //  println(txns.head)

  }
}
