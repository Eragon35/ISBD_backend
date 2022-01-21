package Models

import slick.lifted.Tag

import scala.collection.mutable.ArrayBuffer
//import slick.model._
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Models extends App {
  val lines = new ArrayBuffer[Any]()
//  def println(s: Any) = lines += s


  class Person(tag:Tag) extends Table[(Int, String, String)](tag, "person") {
    def id = column[Int]("id")
    def firstName = column[String]("firstname")
    def lastName = column[String]("lastname")

    override def * = (id, firstName, lastName)
  }
  val personTable = TableQuery[Person]

  class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
    def id = column[Int]("SUP_ID", O.PrimaryKey) // This is the primary key column
    def name = column[String]("SUP_NAME")
    def street = column[String]("STREET")
    def city = column[String]("CITY")
    def state = column[String]("STATE")
    def zip = column[String]("ZIP")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, street, city, state, zip)
  }
  val suppliers = TableQuery[Suppliers]

  val mydb = Database.forConfig("mydb")
  try {

//    val setup = DBIO.seq(
//      // Create the tables, including primary and foreign keys
//      (suppliers.schema).create,
//
//      // Insert some suppliers
//      suppliers += (101, "Acme, Inc.",      "99 Market Street", "Groundsville", "CA", "95199"),
//      suppliers += ( 49, "Superior Coffee", "1 Party Place",    "Mendocino",    "CA", "95460"),
//      suppliers += (150, "The High Ground", "100 Coffee Lane",  "Meadows",      "CA", "93966"),
//      // Equivalent SQL code:
//      // insert into SUPPLIERS(SUP_ID, SUP_NAME, STREET, CITY, STATE, ZIP) values (?,?,?,?,?,?)
//
//      // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
//
//      // Equivalent SQL code:
//      // insert into COFFEES(COF_NAME, SUP_ID, PRICE, SALES, TOTAL) values (?,?,?,?,?)
//    )

//    val setupFuture = mydb.run(setup)
//    val resultFuture = setupFuture.flatMap { _ =>
//      println("SUpplyes:")
//      mydb.run(suppliers.result).map(_.foreach {
//          println
//      })
//    }

    val q1 = personTable.filter(_.id > 5)
    println("persons:")
    mydb.run(q1.result).map(_.foreach{println})

//    Await.result(resultFuture, Duration.Inf)
//    lines.foreach(Predef.println _)
  } finally  mydb.close

}
