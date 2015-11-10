package dao

import models.Advertiser
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Advertisers(tag: Tag) extends Table[Advertiser](tag, "ADVERTISER") {

  def id = column[Long]("advertiser_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("advertiser_name")

  def * = (id.?, name) <> ((Advertiser.apply _).tupled, Advertiser.unapply)
}

object Advertisers {
  val db = Database.forURL("jdbc:h2:mem:play;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
  lazy val query = TableQuery[Advertisers]

  def all(): Future[List[Advertiser]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.length.result)
  def insert(user: Advertiser): Future[Unit] = db.run(query += user).map(_ => ())
  def insert(advertisers: Seq[Advertiser]): Future[Unit] = advertisers.map(insert).head
}


