package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Punishment(punishment: String, smokerId: Int, victimId: Int, cost: Int)
case class PreviousPunishment(punishment: String, victimFirstName: String, victimLastName: String)

class PunishmentTable(tag:Tag) extends Table[(Int, Int, String, Int, Int)](tag, "punishment") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def punishment = column[String]("type")
  def victimId = column[Int]("victimid")
  def cost = column[Int]("cost")

  override def * = (id, smokerId, punishment, victimId, cost)
}
