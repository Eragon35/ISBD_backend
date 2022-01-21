package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

class DoctorTable(tag:Tag) extends Table[(Int, Int, Int)](tag, "doctor") {
  def id = column[Int]("id")
  def personId = column[Int]("personid")
  def cost = column[Int]("cost")

  override def * = (id, personId, cost)
}
