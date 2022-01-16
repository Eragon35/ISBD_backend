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
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }


  case class Smoker(firstName: String, lastName: String, numberOfAccidents: Int)
  implicit val smokerWrites: Writes[Smoker] = (smoker: Smoker) => Json.obj(
    "firstName" -> smoker.firstName,
    "lastName" -> smoker.lastName,
    "numberOfAccidents" -> smoker.numberOfAccidents
  )

  /**
   * select * from smoker S where (S.doctorId = doctorId) -> smokers
   * names = smokers.map(x => select P.firstName, P.LastName from person P where (P.id = x.personId)
   * list = smokers.map(person => smoker(person.firstname, person, lastname, smoker.numberofaccidents)
   */
  def getPatientsList(doctorId: Int): Action[AnyContent] = Action { doctorId =>
    val list: Seq[Smoker] = Seq(
      Smoker("Arsentii", "Antipin", 0),
      Smoker("Alexander", "Antipin", 8)
    )
    Ok(Json.toJson(list))
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

  def getPatient(patientId: Int):Action[AnyContent] =  {
    Action { patientId =>
      val smoker = Smoker("Alexander", "Antipin", 8) // find in smoker by patientId
      val relatives: Seq[Relative] = Seq(
        Relative(1, "Victor", "Andreev", "husband"),
        Relative(2, "Andrey", "Smirnov", "son")) // find in person by smoker.personId
      val punishments: Seq[PreviousPunishment] = Seq(
        PreviousPunishment("broke hand", "Niyaz", "Bayramov"),
        PreviousPunishment("iznasilovanie", "Alexey", "Panin")
      )
      val patient: Patient = Patient(smoker, relatives, punishments) // patient(person.firstname, person, lastname, smoker.numberofaccidents)
      println(patientId)
      Ok(Json.toJson(patient))
    }
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
      Ok
    }


  case class Weighing(smokerId: Int, date: String, weight: Float)
  implicit val weighingReads: Reads[Weighing] =
    ((JsPath \ "smokerId").read[Int] and
      (JsPath \ "date").read[String] and
      (JsPath \ "weight").read[Float])(Weighing.apply _)

  def addWeighing: Action[AnyContent] = Action { request =>
    val json = request.body.asJson.get
    val weighing = json.as[Weighing]
    // save weighing to DB: to weighing
    println(weighing)
    Ok
  }


  case class Relative(personId: Int, firstName: String, lastName: String, relationship: String)
  implicit val relativeWrites: Writes[Relative] = (relative: Relative) => Json.obj(
    "personId" -> relative.personId,
    "firstName" -> relative.firstName,
    "lastName" -> relative.lastName,
    "relationship" -> relative.relationship
  )

  def relativesWithFinger(smokerId: Int): Action[AnyContent] = Action { smokerId =>
    val list: Seq[Relative] = Seq(
      Relative(1, "Victor", "Andreev", "husband"),
      Relative(2, "Andrey", "Smirnov", "son")) // get from BD: select * from relative R
    // where (R.smokerId = smokerId) and (isFingerCuttingOff = false)
    println(smokerId)
    Ok(Json.toJson(list))
  }

  def cutFinger(personId: Int): Action[AnyContent] = Action { personId =>
    // update relative R
    // set isFingerCuttingOff = true
    // where R.personId = personId
    println(personId)
    Ok
  }

  // TODO: think about need of this end-point
  def getRelatives(smokerId: Int): Action[AnyContent] = Action { smokerId =>
    val list: Seq[Relative] = Seq(
      Relative(1, "Victor", "Andreev", "husband"),
      Relative(2, "Andrey", "Smirnov", "son")) // get from BD: select * from relative R
    // where (R.smokerId = smokerId)
    Ok(Json.toJson(list))
  }



}
