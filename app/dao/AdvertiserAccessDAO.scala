package dao

import models.AdvertiserAccess
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AdvertiserAccesses(tag: Tag) extends Table[AdvertiserAccess](tag, "ADVERTISERACCESS") {

  def id = column[Long]("advertiser_access_id", O.PrimaryKey, O.AutoInc)
  def userId = column[Long]("user_id")
  def advertiserId = column[Long]("advertiser_id")

  def * = (id.?, userId, advertiserId) <> ((AdvertiserAccess.apply _).tupled, AdvertiserAccess.unapply)

  def userFK = foreignKey("user_fk", userId, Users.query)(_.id)
  def advertiserFK = foreignKey("advertiser_fk", advertiserId, Advertisers.query)(_.id)
}

object AdvertiserAccesses {
  val db = Database.forURL("jdbc:h2:mem:play;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
  lazy val query = TableQuery[AdvertiserAccesses]

  def all(): Future[List[AdvertiserAccess]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.map(_.id).length.result)
  def insert(aa: AdvertiserAccess): Future[Unit] = db.run(query += aa).map(_ => ())
  def insert(aas: Seq[AdvertiserAccess]): Future[Unit] = aas.map(insert).head
}

/*
object AdvertiserAccesses extends TableQuery(new AdvertiserAccesses(_)) {
  val db = Database.forConfig("play")
  lazy val query = TableQuery[AdvertiserAccesses]

  def all(): Future[List[AdvertiserAccess]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.map(_.id).length.result)
  def insert(aa: AdvertiserAccess): Future[Unit] = db.run(query += aa).map(_ => ())
  def insert(aas: Seq[AdvertiserAccess]): Future[Unit] = aas.map(insert).head
}
*/
