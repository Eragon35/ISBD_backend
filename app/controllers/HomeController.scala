package controllers

import play.api.libs.functional.syntax.toFunctionalBuilderOps
import play.api.libs.json.Format.GenericFormat
import play.api.libs.json._
import play.api.mvc._

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
  def index: Action[AnyContent] = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  case class User(login: String, pass: String)
  implicit val userReads: Reads[User] =
  ((JsPath \ "login").read[String] and
    (JsPath \ "pass").read[String])(User.apply _)

  def auth: Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val user = json.as[User]
    // check if password is right
    println(user)
    Ok(Json.toJson(1)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
    // send user/doctor id
  }


  case class Smoker(id: Int, firstName: String, lastName: String, numberOfAccidents: Int)
  implicit val smokerWrites: Writes[Smoker] = (smoker: Smoker) => Json.obj(
    "id" -> smoker.id,
    "firstName" -> smoker.firstName,
    "lastName" -> smoker.lastName,
    "numberOfAccidents" -> smoker.numberOfAccidents
  )

  /**
   * select * from smoker S where (S.doctorId = doctorId) -> smokers
   * names = smokers.map(x => select P.firstName, P.LastName from person P where (P.id = x.personId)
   * list = smokers.map(person => smoker(person.firstname, person, lastname, smoker.numberofaccidents)
   */
  def getPatientsList(doctorId: Int): Action[AnyContent] = Action { _ =>
    val list: Seq[Smoker] = Seq(
      Smoker(35,"Arsentii", "Antipin", 0),
      Smoker(26, "Alexander", "Antipin", 8)
    )
    println(doctorId)
    Ok(Json.toJson(list)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  case class Patient(smoker: Smoker, relatives: Seq[Relative], punishments: Seq[PreviousPunishment])
  implicit val patientWrites: Writes[Patient] = (patient: Patient) => Json.obj(
    "smoker" -> patient.smoker,
    "relatives" -> patient.relatives,
    "punishments" -> patient.punishments
  )

  case class PreviousPunishment(punishment: String, victimFirstName: String, victimLastName: String)
  implicit val previousPunishmentWrites: Writes[PreviousPunishment] = (pp: PreviousPunishment) => Json.obj(
    "punishment" -> pp.punishment,
    "victimFirstName" -> pp.victimFirstName,
    "victimLastName" -> pp.victimLastName
  )

  def getSmoker(smokerId: Int):Action[AnyContent] =  Action { _ =>
    val smoker = Smoker(26, "Alexander", "Antipin", 8) // find in smoker by smokerId
    println(smokerId)
    Ok(Json.toJson(smoker)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }

  def getRelatives(smokerId: Int): Action[AnyContent] = Action { _ =>
    val relatives: Seq[Relative] = Seq(
      Relative(1, "Victor", "Andreev", "husband"),
      Relative(2, "Andrey", "Smirnov", "son")) // find in person by smoker.personId
      println(smokerId)
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

  // victimId = personId
  case class Punishment(punishment: String, smokerId: Int, victimId: Int, cost: Int)
  implicit val punishmentReads: Reads[Punishment] =
    ((JsPath \ "punishment").read[String] and
      (JsPath \ "smokerId").read[Int] and
      (JsPath \ "victim").read[Int] and
      (JsPath \ "cost").read[Int])(Punishment.apply _)

    def punishment: Action[AnyContent] = Action { request =>
      val json = request.body.asJson.get
      val punishment = json.as[Punishment]
      // save punishment to DB punishment
      println(punishment)
      Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
    }


  case class Weighing(smokerId: Int, date: String, weight: Float)
  implicit val weighingReads: Reads[Weighing] =
    ((JsPath \ "smokerId").read[Int] and
      (JsPath \ "date").read[String] and
      (JsPath \ "weight").read[Float])(Weighing.apply _)

  def addWeighing(): Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val weighing = json.as[Weighing]
    // save weighing to DB: to weighing
    println(weighing)
    Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }


  case class Relative(personId: Int, firstName: String, lastName: String, relationship: String)
  implicit val relativeWrites: Writes[Relative] = (relative: Relative) => Json.obj(
    "personId" -> relative.personId,
    "firstName" -> relative.firstName,
    "lastName" -> relative.lastName,
    "relationship" -> relative.relationship
  )

  def relativesWithFinger(smokerId: Int): Action[AnyContent] = Action { _ =>
    val list: Seq[Relative] = Seq(
      Relative(1, "Victor", "Andreev", "husband"),
      Relative(2, "Andrey", "Smirnov", "son")) // get from BD: select * from relative R
    // where (R.smokerId = smokerId) and (isFingerCuttingOff = false)
    println(smokerId)
    Ok(Json.toJson(list)).withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }

  def cutFinger(personId: Int): Action[AnyContent] = Action { _ =>
    // update relative R
    // set isFingerCuttingOff = true
    // where R.personId = personId
    println(personId)
    Ok.withHeaders("Access-Control-Allow-Origin" -> "http://localhost:3000")
  }

}
