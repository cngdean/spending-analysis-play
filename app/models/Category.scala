package models

case class Category(categoryName: String, regex: String, exclude: Boolean)

case class CategoryMapper(filename: String) {
  val source = scala.io.Source.fromFile(filename)

  def buildCategories(lines: List[String]):List[Category] = {
    if (lines.isEmpty) Nil
    else {
      val items = lines.head.split("\\|\\|")
      if (items.size != 3) {
        throw new IllegalArgumentException("malformed regex file: should have 3 columns, had " + items.size)
      }

      val (cat,regex,exclude) = (items(0), items(1), items(2))
      Category(cat, regex, "Y".equals(exclude)) :: buildCategories(lines.tail)
    }
  }

  val categories: List[Category] = buildCategories(source.getLines().toList) ::: Category("Needs Category", ".*", false) :: Nil

  def findCategory(desc: String):Category = {
    val match2 = categories.dropWhile((x: Category) => !desc.matches(x.regex))
    match2.head
  }

}
