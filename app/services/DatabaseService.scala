package services

import javax.inject.*
import slick.jdbc.PostgresProfile.api.*

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DatabaseService @Inject()(implicit ec: ExecutionContext) {
  val db: Database = Database.forConfig("slick.dbs.default.db")

  def run[R](action: DBIO[R]): Future[R] = {
    db.run(action)
  }
}
