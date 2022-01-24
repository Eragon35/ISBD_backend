package controllers

import Models.{Observation, PreviousPunishment, Punishment, Relative, Smoker, SmokerObservation, Weighing}
import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json._
import play.api.mvc._
import services.Connection
import services.Connection.{getCost, getPatinets, getRelativeWithFinger, getRelativesList, insertObservation, insertPunushment, insertWeighing, updateRelativeFinger}

import javax.inject._

/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
   * Create an Action to render an HTML page with a welcome message.
   * The configuration in the `routes` file means that this method
   * will be called when the application receives a `GET` request with
   * a path of `/`.
   */
  def index(): Action[AnyContent] = Action {
//    init()
    Ok(views.html.index("Your new application is ready."))
  }

  case class User(login: String, pass: String)
  implicit val userReads: Reads[User] =
  ((JsPath \ "login").read[String] and
    (JsPath \ "pass").read[String])(User.apply _)
  implicit val smokerWrites: Writes[Smoker] = (smoker: Smoker) => Json.obj(
    "id" -> smoker.id,
    "firstName" -> smoker.firstName,
    "lastName" -> smoker.lastName,
    "numberOfAccidents" -> smoker.numberOfAccidents
  )
  implicit val previousPunishmentWrites: Writes[PreviousPunishment] = (pp: PreviousPunishment) => Json.obj(
    "punishment" -> pp.punishment,
    "victimFirstName" -> pp.victimFirstName,
    "victimLastName" -> pp.victimLastName
  )
  implicit val observationWrites: Writes[Observation] = (o: Observation) => Json.obj(
    "start" -> o.start,
    "finish" -> o.finish,
    "hoursPerDay" -> o.hoursPerDay
  )
  implicit val punishmentReads: Reads[Punishment] =
    ((JsPath \ "punishment").read[String] and
      (JsPath \ "smokerId").read[Int] and
      (JsPath \ "victim").read[Int] and
      (JsPath \ "cost").read[Int])(Punishment.apply _)
  implicit val weighingReads: Reads[Weighing] = {
    ((JsPath \ "smokerId").read[Int] and
      (JsPath \ "date").read[String] and
      (JsPath \ "weight").read[Int])(Weighing.apply _)
  }
  implicit val relativeWrites: Writes[Relative] = (relative: Relative) => Json.obj(
    "personId" -> relative.personId,
    "firstName" -> relative.firstName,
    "lastName" -> relative.lastName,
    "relationship" -> relative.relationship
  )
  implicit val smokerObservationReads: Reads[SmokerObservation] =
    ((JsPath \ "smokerId").read[Int] and
      (JsPath \ "start").read[String] and
      (JsPath \ "finish").read[String] and
      (JsPath \ "hoursPerDay").read[Int])(SmokerObservation.apply _)

  //  case class Patient(smoker: Smoker, relatives: Seq[Relative], punishments: Seq[PreviousPunishment])
  //  implicit val patientWrites: Writes[Patient] = (patient: Patient) => Json.obj(
  //    "smoker" -> patient.smoker,
  //    "relatives" -> patient.relatives,
  //    "punishments" -> patient.punishments
  //  )

  def auth(): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val user = json.as[User]
    // check if password is right
    println(user)
    Ok(Json.toJson(1)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
    // send user/doctor id
  }

  /**
   * select * from smoker S where (S.doctorId = doctorId) -> smokers
   * names = smokers.map(x => select P.firstName, P.LastName from person P where (P.id = x.personId)
   * list = smokers.map(person => smoker(person.firstname, person, lastname, smoker.numberofaccidents)
   */
  def getPatientsList(doctorId: Int): Action[AnyContent] = Action { _ =>
    val list: Seq[Smoker] = getPatinets(doctorId)
    println("finding " + list.size + " patients for doctor with Id = " + doctorId)
    Ok(Json.toJson(list)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def getSmoker(smokerId: Int):Action[AnyContent] =  Action { _ =>
    val smoker = Smoker(26, "Alexander", "Petushkov", 8) // find in smoker by smokerId
    println(smokerId)
    Ok(Json.toJson(smoker)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def getRelatives(smokerId: Int): Action[AnyContent] = Action { _ =>
    val relatives: Seq[Relative] = getRelativesList(smokerId)
    Ok(Json.toJson(relatives)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def getPunishments(smokerId: Int): Action[AnyContent] = Action { _ =>
    val punishments: Seq[PreviousPunishment] = Seq(
      PreviousPunishment("broke hand", "Niyaz", "Bayramov"),
      PreviousPunishment("iznasilovanie", "Alexey", "Panin")
    )
    println(smokerId)
    Ok(Json.toJson(punishments)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def getObservationSchedule(smokerId: Int): Action[AnyContent] = Action { _ =>
    val observation: Seq[Observation] = Seq(
      Observation("12-12-2021", "01-01-2022", 12),
      Observation("20-08-2000", "20-08-2020", 24)
    )
    println(smokerId) // select * from observationschedule O where O.smokerId = smokerId
    Ok(Json.toJson(observation)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def addPunishment(): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val punishment = json.as[Punishment]
    insertPunushment(punishment)
    Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def addWeighing(): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val weighing = json.as[Weighing]
    insertWeighing(weighing)
    println(weighing)
    Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def relativesWithFinger(smokerId: Int): Action[AnyContent] = Action { _ =>
    val list: Seq[Relative] = getRelativeWithFinger(smokerId)
    // call function findRelativeToCutFingerOut(smoker integer)
//    println(smokerId)
    Ok(Json.toJson(list)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def cutFinger(personId: Int): Action[AnyContent] = Action { _ =>
    updateRelativeFinger(personId)
//    println(personId)
    Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def addObservation(): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val observation = json.as[SmokerObservation]
    insertObservation(observation)
    Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }

  def getTreatmentCost(smokerId: Int) = Action {_ =>
    val cost: Int = getCost(smokerId)
    Ok(Json.toJson(cost)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  def exit(): Action[AnyContent] = Action { _ =>
    Connection.check()
    Ok
  }

}
