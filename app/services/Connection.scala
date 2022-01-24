package services

import Models.{BillTable, DoctorTable, ObservationTable, PersonTable, Punishment, PunishmentTable, RelativeTable, Smoker, SmokerObservation, SmokerTable, Weighing, WeighingTable}
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.language.postfixOps



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
  def personID(id: Int): Unit = personId = id
  var billId = 0
  def billID(id: Int): Unit = billId = id
  var doctorId = 0
  def doctorID(id: Int): Unit = doctorId = id
  var observationId = 0
  def observationID(id: Int): Unit = observationId = id
  var punishmentId = 0
  def punishmentID(id: Int): Unit = punishmentId = id
  var relativeId = 0
  def relativeID(id: Int): Unit = relativeId = id
  var smokerId = 0
  def smokerID(id: Int): Unit = smokerId = id
  var weighingId = 0
  def weighingID(id: Int): Unit = weighingId = id

  // todo: create function to read all IDs

  val mydb = Database.forConfig("mydb")
  /**
   * Initializing all IDs, 'cause it better than call select every time before insert
   */
  val q = personTable.map(_.id).max.result
  val q1 = billTable.map(_.id).max.result
  val q2 = doctorTable.map(_.id).max.result
  val q3 = observationTable.map(_.id).max.result
  val q4 = punishmentTable.map(_.id).max.result
  val q5 = relativeTable.map(_.id).max.result
  val q6 = smokerTable.map(_.id).max.result
  val q7 = weighingTable.map(_.id).max.result
  mydb.run(q).map(_.foreach{personID})
  mydb.run(q1).map(_.foreach{billID})
  mydb.run(q2).map(_.foreach{doctorID})
  mydb.run(q3).map(_.foreach{observationID})
  mydb.run(q4).map(_.foreach{punishmentID})
  mydb.run(q5).map(_.foreach{relativeID})
  mydb.run(q6).map(_.foreach{smokerID})
  mydb.run(q7).map(_.foreach{weighingID})
  println("Init was made")


  def check (): Unit = {
    println(personId)
    println(billId)
    println(doctorId)
    println(observationId)
    println(punishmentId)
    println(relativeId)
    println(smokerId)
    println(weighingId)
  }

  def getPatinets(doctorId: Int): Seq[Smoker] = {
    var result = Seq[Smoker]()
    val join = for {
      s <- smokerTable if s.doctorId === doctorId
      p <- personTable if s.personId === p.id
    } yield (s.id, p.firstName, p.lastName, s.numberOfAccidents)
    val resultFuture = mydb.run(join.result).map{ row =>
      row.groupBy(_._1).map{ r =>
        val smoker = r._2
        smoker.map(x => result = result :+ Smoker(x._1, x._2, x._3, x._4))
      }
    }
    Await.result(resultFuture, Duration.Inf)
    result
  }

  def insertObservation(observation: SmokerObservation): Unit = {
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
      }
    println(observation + " was inserted")
  }

  def insertPunushment(punishment: Punishment): Unit = {
    try {
      punishmentId += 1
      val setup = DBIO.seq(
        punishmentTable += (
          punishmentId,
          punishment.smokerId,
          punishment.punishment,
          punishment.victimId,
          punishment.cost
        ),
      )
      mydb.run(setup)
    }
    println(punishment + " was inserted")
  }

  def insertWeighing(weighing: Weighing): Unit = {
    try {
      val smoker = smokerTable.filter(_.id === weighing.smokerId)
      var referenceWeight = -1
      mydb.run(smoker.result).map(_.foreach {
        case (_, _, _, _, weight) => referenceWeight = weight
      })
//      println(referenceWeight)

      weighingId += 1
      val setup = DBIO.seq(
        weighingTable += (
                          weighingId,
                          weighing.smokerId,
                          weighing.date,
                          weighing.weight > referenceWeight,
                          weighing.weight),
      )
      mydb.run(setup)
    }
    println(weighing + " was inserted")
  }

  def updateRelativeFinger(personId: Int): Unit = {
    val update = relativeTable.filter(_.personId === personId).map(_.isFingerCuttingOff).update(false)
    val action = DBIO.seq(update)
    mydb.run(action)
  }

  def exit(): Unit = mydb.close

}
