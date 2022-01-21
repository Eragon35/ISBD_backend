package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class RelativeTable(tag:Tag) extends Table[(Int, Int, Int, Boolean, String)](tag, "relative") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def personId = column[Int]("personid")
  def isFingerCuttingOff = column[Boolean]("isfingercuttingoff")
  def relationship = column[String]("relationship")

  override def * = (id, smokerId, personId, isFingerCuttingOff, relationship)
}
