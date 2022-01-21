package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class BillTable(tag:Tag) extends Table[(Int, Int, Int, Int)](tag, "bill") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def doctorId = column[Int]("doctorid")
  def sum = column[Int]("sum")

  override def * = (id, smokerId, doctorId, sum)
}
