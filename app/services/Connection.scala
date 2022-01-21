package services

import Models.{BillTable, DoctorTable, ObservationTable, PersonTable, PunishmentTable, RelativeTable, SmokerObservation, SmokerTable, WeighingTable}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global



object Connection {
  val personTable = TableQuery[PersonTable]
  val billTable = TableQuery[BillTable]
  val doctorTable = TableQuery[DoctorTable]
  val observationTable = TableQuery[ObservationTable]
  val punishmentTable = TableQuery[PunishmentTable]
  val relativeTable = TableQuery[RelativeTable]
  val smokerTable = TableQuery[SmokerTable]
  val weighingTable = TableQuery[WeighingTable]

  var personId = 0
  var billId = 0
  var doctorId = 0
  var observationId = 15
  var punishmnetId = 0
  var relativeId = 0
  var smokerId = 0
  var weightingId = 0

  // todo: create function to read all IDs

  val mydb = Database.forConfig("mydb")

  def insertObservation(observation: SmokerObservation): Unit = {
    val mydb = Database.forConfig("mydb")
      try {
        observationId += 1
        val setup = DBIO.seq(
          observationTable += (
                                observationId,
                                observation.smokerId,
                                observation.start,
                                observation.finish,
                                observation.hoursPerDay),
        )
        mydb.run(setup)
//        val q1 = observationTable.filter(_.id > 13)
//        println("observation:")
//        mydb.run(q1.result).map(_.foreach{println})
      }
    println(observationId + " " + observation + " was inserted")
  }

  def exit(): Unit = mydb.close

}
