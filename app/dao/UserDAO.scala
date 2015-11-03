package dao

import javax.inject.Inject

import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class UserDAO @Inject()(protected val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile]{
  import driver.api._

  private val Users = TableQuery[UsersTable]

  private class UsersTable(tag: Tag) extends Table[User](tag, "USER") {
    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("email")

    def * = (id.?, email) <> ((User.apply _).tupled, User.unapply)
  }

  def all(): Future[List[User]] = db.run(Users.result).map(_.toList)

  def insert(user: User): Future[Unit] = db.run(Users += user).map(_ => ())

}
