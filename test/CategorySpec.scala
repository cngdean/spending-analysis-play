import models.{Category, CategoryMapper}
import org.specs2.mutable._

class CategorySpec extends Specification {
    "A Category " should  {

      "have a name" in {
        val cat = Category("a name", "a regex", false)
        3 should === (3)
        "hello" must startWith ("hello")
        cat.categoryName must startWith ("a name")
        cat.regex must startWith ("a regex")
      }

      "map to categories" in {
        val catMapper = CategoryMapper("test/resources/test_regexfile.txt")
        catMapper.categories should contain (Category("Kitty", "(petsmart|petco).*", false))
        catMapper.findCategory("starbucks i25 & tamarac").categoryName must startWith ("Coffee")
      }

    }

  "map to no catgegory" in {
    val catMapper = CategoryMapper("test/resources/test_regexfile.txt")
    catMapper.findCategory("unkown").categoryName must startWith ("Needs Category")
  }


}

