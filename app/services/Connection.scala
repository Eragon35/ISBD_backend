package services

import slick.driver.PostgresDriver.api._

object Connection {
  val database = Database.forConfig("db")
  try {
    // ...
  } finally database.close

}
