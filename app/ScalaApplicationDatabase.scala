import javax.inject.Inject
import play.api.db.Database

import scala.concurrent.Future

class ScalaApplicationDatabase @Inject() (db: Database, databaseExecutionContext: DatabaseExecutionContext) {
  def updateSomething(): Unit = {
    Future {
      db.withConnection { conn =>
        // do whatever you need with the db connection
        println("DB connection started")
      }
    }(databaseExecutionContext)
  }
}