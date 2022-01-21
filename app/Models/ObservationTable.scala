package Models

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.JsPath
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import play.api.libs.json._



class ObservationTable(tag:Tag) extends Table[(Int, Int, String, String, Int)](tag, "observationschedule") {
  def id = column[Int]("id", O.PrimaryKey)
  def smokerId = column[Int]("smokerid")
  def start = column[String]("start")
  def finish = column[String]("finish")
  def hoursPerDay = column[Int]("hoursperday")

  override def * = (id, smokerId, start, finish, hoursPerDay)
}

case class SmokerObservation(smokerId: Int, start: String, finish: String, hoursPerDay: Int)
