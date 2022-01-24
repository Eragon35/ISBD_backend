package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._

case class SmokerObservation(smokerId: Int, start: String, finish: String, hoursPerDay: Int)
case class Observation(start: String, finish: String, hoursPerDay: Int)

class ObservationTable(tag:Tag) extends Table[(Int, Int, String, String, Int)](tag, "observationschedule") {
  def id = column[Int]("id", O.PrimaryKey)
  def smokerId = column[Int]("smokerid")
  def start = column[String]("start")
  def finish = column[String]("finish")
  def hoursPerDay = column[Int]("hoursperday")

  override def * = (id, smokerId, start, finish, hoursPerDay)
}

