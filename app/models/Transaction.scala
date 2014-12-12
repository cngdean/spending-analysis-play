package models

/**
 * Created by carmen on 12/1/14.
 */
case class Transaction(id: String, note: String, memo: String, amount: BigDecimal, date: java.util.Date, category: Category) {

  val format = new java.text.SimpleDateFormat("yyyy-MM")

  def month:String = format.format(date)


}