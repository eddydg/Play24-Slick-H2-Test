package dao

import models.Advertiser
import play.api.Play
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.H2Driver.api._
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


class Advertisers(tag: Tag) extends Table[Advertiser](tag, "ADVERTISER") {

  def id = column[Long]("advertiser_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("advertiser_name")

  def * = (id.?, name) <> ((Advertiser.apply _).tupled, Advertiser.unapply)
}

object Advertisers {
  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db
  lazy val query = TableQuery[Advertisers]

  def all(): Future[List[Advertiser]] = db.run(query.result).map(_.toList)
  def count(): Future[Int] = db.run(query.length.result)
  def findById(id: Long): Future[Option[Advertiser]] = db.run(query.filter(_.id === id).result.headOption)

  def insert(user: Advertiser): Future[Unit] = db.run(query += user).map(_ => ())
  def insert(advertisers: Seq[Advertiser]): Future[Unit] = advertisers.map(insert).head
}


