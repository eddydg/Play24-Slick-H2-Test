package dao

import models.{Advertiser, User}
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Users(tag: Tag) extends Table[User](tag, "USER") {

  def id = column[Long]("user_id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("user_email")
  def password = column[String]("user_password")
  def isSuper = column[Boolean]("is_super")
  def status = column[String]("status")

  def * = (id.?, email, password, isSuper, status) <> ((User.apply _).tupled, User.unapply)

  def advertisers = AdvertiserAccesses.query.filter(_.userId === id).flatMap(_.advertiserFK)
}

object Users {
  val db = Database.forURL("jdbc:h2:mem:play;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
  lazy val query = TableQuery[Users]

  def all(): Future[List[User]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.length.result)
  def insert(user: User): Future[Unit] = db.run(query += user).map(_ => ())
  def insert(users: Seq[User]): Future[Unit] = users.map(insert).head

  def getAdverts(u: User): Future[Seq[Advertiser]] = {
    val foo = u.advertisers
    db.run(foo.result)
  }
}
/*object Users extends TableQuery(new Users(_)) {
  val db = Database.forConfig("play")
  lazy val query = TableQuery[Users]

  def all(): Future[List[User]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.length.result)
  def insert(user: User): Future[Unit] = db.run(query += user).map(_ => ())
  def insert(users: Seq[User]): Future[Unit] = users.map(insert).head
}*/







/*
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

    //def advertisers = AdvertiserAccesses.query.filter(_.userId === id).flatMa(_.advertiserFK)
  }

  //object users extends TableQuery(new Users(_))
  //val users = TableQuery[Users]

  object Users {
    lazy val all = TableQuery[Users]
  }

  def all(): Future[List[User]] = db.run(Users.all.result).map(_.toList)
  def count(): Future[Int] = db.run(Users.all.length.result)
  def insert(user: User): Future[Unit] = db.run(Users.all += user).map(_ => ())
  def insert(users: Seq[User]): Future[Unit] = users.map(insert).head

  val advAccessDao = new AdvertiserAccessDAO
  def accesses = for {
    (user, access) <- Users.all join advAccessDao.AdvertiserAccesses.all on (_.id === _.userId)
  } yield (access.advertiserId)

}*/
