package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class PersonTable(tag:Tag) extends Table[(Int, String, String)](tag, "person") {
  def id = column[Int]("id")
  def firstName = column[String]("firstname")
  def lastName = column[String]("lastname")

  override def * = (id, firstName, lastName)
}
