package dao

import javax.inject.Inject

import models.AdvertiserAccess
import play.api.Play
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import slick.driver.JdbcProfile

import scala.concurrent.Future

class AdvertiserAccessDAO @Inject() (userDao: UserDAO, advertiserDao: AdvertiserDAO) extends HasDatabaseConfig[JdbcProfile]{
  protected val dbConfig = DatabaseConfigProvider.get[JdbcProfile](Play.current)

  import driver.api._

  private val AdvertiserAccesses = TableQuery[AdvertiserAccessTable]

  private class AdvertiserAccessTable(tag: Tag) extends Table[AdvertiserAccess](tag, "ADVERTISERACCESS") {
    def id = column[Long]("advertiser_access_id", O.PrimaryKey, O.AutoInc)
    def userId = column[Long]("user_id")
    def advertiserId = column[Long]("advertiser_id")

    def * = (id.?, userId, advertiserId) <> ((AdvertiserAccess.apply _).tupled, AdvertiserAccess.unapply)

    def userFK = foreignKey("user_fk", userId, userDao.users)(_.id)
    def advertiserFK = foreignKey("advertiser_fk", advertiserId, advertiserDao.advertisers)(_.id)
  }

  def all(): Future[List[AdvertiserAccess]] = db.run(AdvertiserAccesses.result).map(_.toList)

  def insert(aa: AdvertiserAccess): Future[Unit] = db.run(AdvertiserAccesses += aa).map(_ => ())
  def insert(aas: Seq[AdvertiserAccess]): Future[Unit] = aas.map(insert).head

  def count(): Future[Int] = db.run(AdvertiserAccesses.map(_.id).length.result)

}
