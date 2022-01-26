package services

import Models.{BillTable, DoctorTable, Observation, ObservationTable, PersonTable, PreviousPunishment, Punishment, PunishmentTable, Relative, RelativeTable, Smoker, SmokerObservation, SmokerTable, SmokerWeighing, Weighing, WeighingTable}
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



  def selectPatinets(doctorId: Int): Seq[Smoker] = {
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

  def selectRelativeWithFinger(smokerId: Int): Seq[Relative] = {
    var result = Seq[Relative]()
    val setup = sql"select * from findrelativetocutfingerout($smokerId)".as[(Int, String, String, String)]
    val resultFuture = mydb.run(setup).map{ row =>
      row.groupBy(_._1).map{ r =>
        val relatives = r._2
        relatives.map(x => result = result :+ Relative(x._1, x._2, x._3, x._4))
      }
    }
    Await.result(resultFuture, Duration.Inf)
    println("Size is " + result.size)
    result
  }

  def selectRelatives(smokerId: Int): Seq[Relative] = {
    var result = Seq[Relative]()
    val setup = sql"select * from findrelative($smokerId)".as[(Int, String, String, String)]
    val resultFuture = mydb.run(setup).map{ row =>
      row.groupBy(_._1).map{ r =>
        val relatives = r._2
        relatives.map(x => result = result :+ Relative(x._1, x._2, x._3, x._4))
      }
    }
    Await.result(resultFuture, Duration.Inf)
    println("Size is " + result.size)
    result
  }

  def getCost(smokerId: Int): Int = {
    var result = -1
    val setup = sql"select * from findrelative($smokerId)".as[Int]
    val resultFuture = mydb.run(setup).map{ row =>
      result = row(0)
    }
    Await.result(resultFuture, Duration.Inf)
    println("Cost is " + result)
    result
  }

  def selectObservations(smokerId: Int): Seq[Observation] = {
    var result = Seq[Observation]()
    val query = observationTable.filter(_.smokerId === smokerId)
    val resultFuture = mydb.run(query.result).map{ row =>
      row.groupBy(_._1).map{ r =>
        val observation = r._2
        observation.map(x => result = result :+ Observation(x._3, x._4, x._5))
      }
    }
    Await.result(resultFuture, Duration.Inf)
    result
  }

  def selectPunishment(smokerId: Int): Seq[PreviousPunishment] = {
    var result = Seq[PreviousPunishment]()
    val join = for {
      pun <- punishmentTable if pun.smokerId === smokerId
      p <- personTable if p.id === pun.victimId
    } yield (pun.id, pun.punishment, p.firstName, p.lastName)
    val resultFuture = mydb.run(join.sortBy(_._1).result).map{ row =>
      row.groupBy(_._1).map{ r =>
        val prevPunishment = r._2
        prevPunishment.map(x => result = result :+ PreviousPunishment(x._2, x._3, x._4))
      }
    }
    Await.result(resultFuture, Duration.Inf)
    result
  }

  def selectSmoker(smokerId: Int): Smoker = {
    var result = Smoker(0, "", "", 0)
    val join = for {
      s <- smokerTable if s.id === smokerId
      p <- personTable if p.id === s.personId
    } yield (p.firstName, p.lastName, s.numberOfAccidents)
    val resultFuture = mydb.run(join.result).map{ row =>
      row.groupBy(_._1).map{ r =>
        val smoker = r._2.head
        result = Smoker(smokerId, smoker._1, smoker._2, smoker._3)
      }
    }
    Await.result(resultFuture, Duration.Inf)
    result
  }

  def selectWeighing(smokerId: Int): Seq[SmokerWeighing] = {
    var result = Seq[SmokerWeighing]()
    val query = weighingTable.filter(_.smokerId === smokerId)
    val resultFuture = mydb.run(query.result).map{ row =>
      row.groupBy(_._1).map{ r =>
        val observation = r._2
        observation.map(x => result = result :+ SmokerWeighing(x._3, x._5))
      }
    }
    Await.result(resultFuture, Duration.Inf)
    result
  }
}
