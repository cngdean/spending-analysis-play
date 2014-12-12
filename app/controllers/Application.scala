package controllers

import models.{TransactionSummary, ProcessFiles}
import play.api._
import play.api.mvc._
import play.api.libs.json.Json

object Application extends Controller {

  val regexFile = System.getProperty("user.home") + "/qfx/regexconfig.txt"
  val directory = System.getProperty("user.home") + "/qfx/"


  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def analyze = Action {
    Ok(views.html.analyze("OK lets analyze?", directory, regexFile))
  }

  def transactions = Action {
    var txns = ProcessFiles.processDirectory(directory, regexFile)
    Ok(views.html.transactions("transactions", txns))
  }

  def categories = Action {
    import models.CategoryMapper
    val cats = CategoryMapper(regexFile).categories
    Ok(views.html.categories("categories", cats))
  }

  def transactionsJson = Action {
    val fakeTxns = List(Map("date" -> "2014-09-01", "amount" -> "33"),
        Map("date" -> "2014-09-02", "amount" -> "133"),
        Map("date" -> "2014-09-03", "amount" -> "233"),
        Map("date" -> "2014-09-04", "amount" -> "333") )
    val worksJS = Json.toJson(fakeTxns);
        
    var txns = ProcessFiles.processDirectory(directory, regexFile) //.filter(_.month > "2014-01")
    val txnMap = TransactionSummary.spendingByMonthJSON(txns)

    val realJS = Json.toJson(txnMap)

    Ok(realJS);
  }
    
  
  def categoryTxnsJson = Action {
    val fakeTxns = List(Map("category" -> "Clothes", "amount" -> "33"),
        Map("category" -> "gooey", "amount" -> "133") )
        
//    val txns = 
//        List(
//            List("gooey", "33.50"),
//            List("clothes", "133.50"),
//            List("other", "333.50"))
        
    val byCat = TransactionSummary.spendingByCategory(ProcessFiles.processDirectory(directory, regexFile))
    val by2 = for ( (cat, amount) <- byCat) yield List(cat.categoryName, Math.abs(amount.toInt).toString)

    val restJs = Json.toJson(by2);
    Ok(restJs);
//    Ok(Json.toJson(txns2))
    
  }
}