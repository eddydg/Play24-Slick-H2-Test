package dao

import models._
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

  def advertisers = AdvertiserAccesses.query.filter(_.userId === id).map(aa => (aa.advertiserId, aa.canWrite))
}

object Users {
  val db = Database.forURL("jdbc:h2:mem:play;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
  lazy val query = TableQuery[Users]

  def all(): Future[List[User]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.length.result)
  def insert(user: User): Future[Unit] = db.run(query += user).map(_ => ())
  def insert(users: Seq[User]): Future[Unit] = users.map(insert).head

  def getAdverts(u: User) = {
    val query = u.advertisers
    db.run(query.result)
  }
}