import dao._
import models.{Advertiser, AdvertiserAccess, User}
import play.api._
import slick.driver.H2Driver.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    val db = Database.forURL("jdbc:h2:mem:play;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")

    def storedUsers = Users
    def storedAdvertisers = Advertisers
    def storedAdvertiserAccess = AdvertiserAccesses

    val countUsers = Await.result(Users.count(), Duration.Inf)
    val countAdvertisers = Await.result(Advertisers.count(), Duration.Inf)
    val countAdvertiserAccess = Await.result(AdvertiserAccesses.count(), Duration.Inf)

    if (countUsers == 0) {
      val rows = Seq(
        User(None, "alice@example.com", "secret", true, "active"),
        User(None, "bob@example.com",   "secret", false, "active"),
        User(None, "chris@example.com", "secret", false, "active")
      )

      Await.result(Users.insert(rows), Duration.Inf)
    }

    if (countAdvertisers == 0) {
      val rows = Seq(
        Advertiser(None, "advertiser1"),
        Advertiser(None, "advertiser2"),
        Advertiser(None, "advertiser3"),
        Advertiser(None, "advertiser4"),
        Advertiser(None, "advertiser5")
      )

      Await.result(Advertisers.insert(rows), Duration.Inf)
    }

    if (countAdvertiserAccess == 0) {
      val rows = Seq(
        AdvertiserAccess(None, 1, 1),
        AdvertiserAccess(None, 1, 2),
        AdvertiserAccess(None, 2, 1),
        AdvertiserAccess(None, 2, 2),
        AdvertiserAccess(None, 2, 3)
      )

      Await.result(AdvertiserAccesses.insert(rows), Duration.Inf)
    }

  }

}