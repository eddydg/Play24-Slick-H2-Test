package dao

import models.Advertiser
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class AdvertiserDAO extends HasDatabaseConfig[JdbcProfile] {
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  class Advertisers(tag: Tag) extends Table[Advertiser](tag, "ADVERTISER") {

    def id = column[Long]("advertiser_id", O.PrimaryKey, O.AutoInc)
    def name = column[String]("advertiser_name")

    def * = (id.?, name) <> ((Advertiser.apply _).tupled, Advertiser.unapply)
  }

  object advertisers extends TableQuery(new Advertisers(_))

  def all(): Future[List[Advertiser]] = db.run(advertisers.result).map(_.toList)
  def count(): Future[Int] = db.run(advertisers.length.result)
  def insert(user: Advertiser): Future[Unit] = db.run(advertisers += user).map(_ => ())
  def insert(advertisers: Seq[Advertiser]): Future[Unit] = advertisers.map(insert).head
}