package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class PunishmentTable(tag:Tag) extends Table[(Int, Int, String, Int, Int)](tag, "punishment") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def punishment = column[String]("punishment")
  def victimId = column[Int]("victimid")
  def cost = column[Int]("cost")

  override def * = (id, smokerId, punishment, victimId, cost)
}
