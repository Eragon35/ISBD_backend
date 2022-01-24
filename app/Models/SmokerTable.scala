package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class Smoker(id: Int, firstName: String, lastName: String, numberOfAccidents: Int)

class SmokerTable(tag:Tag) extends Table[(Int, Int, Int, Int, Int)](tag, "smoker") {
  def id = column[Int]("id")
  def personId = column[Int]("personid")
  def doctorId = column[Int]("doctorid")
  def numberOfAccidents = column[Int]("numberofaccidents")
  def referenceWeight = column[Int]("referenceweight")

  override def * = (id, personId, doctorId, numberOfAccidents, referenceWeight)
}
