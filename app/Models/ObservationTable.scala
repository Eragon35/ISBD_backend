package Models

import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._


class ObservationTable(tag:Tag) extends Table[(Int, Int, java.sql.Date, java.sql.Date, Int)](tag, "observationschedule") {
  def id = column[Int]("id")
  def smokerId = column[Int]("smokerid")
  def start = column[java.sql.Date]("start")
  def finish = column[java.sql.Date]("finish")
  def hoursPerDay = column[Int]("hoursperday")

  override def * = (id, smokerId, start, finish, hoursPerDay)
}
