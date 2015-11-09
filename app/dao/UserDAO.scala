package dao

import models.User
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

class UserDAO extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  //private val Users = TableQuery[UsersTable]

  class Users(tag: Tag) extends Table[User](tag, "USER") {

    def id = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
    def email = column[String]("user_email")
    def password = column[String]("user_password")
    def isSuper = column[Boolean]("is_super")
    def status = column[String]("status")

    def * = (id.?, email, password, isSuper, status) <> ((User.apply _).tupled, User.unapply)
  }

  object users extends TableQuery(new Users(_))

  def all(): Future[List[User]] = db.run(users.result).map(_.toList)
  def count(): Future[Int] = db.run(users.length.result)
  def insert(user: User): Future[Unit] = db.run(users += user).map(_ => ())
  def insert(users: Seq[User]): Future[Unit] = users.map(insert).head

}