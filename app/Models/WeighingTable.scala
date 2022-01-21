package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class WeighingTable(tag:Tag) extends Table[(Int, Int, java.sql.Date, Boolean, Int)](tag, "punishment") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def date = column[java.sql.Date]("date")
  def isBigger = column[Boolean]("isbigger")
  def weight = column[Int]("weight")

  override def * = (id, smokerId, date, isBigger, weight)
}
