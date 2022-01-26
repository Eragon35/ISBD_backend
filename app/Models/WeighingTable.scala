package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Weighing(smokerId: Int, date: String, weight: Int)
case class SmokerWeighing(date: String, weight: Int)

class WeighingTable(tag:Tag) extends Table[(Int, Int, String, Boolean, Int)](tag, "weighing") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def date = column[String]("date")
  def isBigger = column[Boolean]("isbigger")
  def weight = column[Int]("weight")

  override def * = (id, smokerId, date, isBigger, weight)
}
