import dao.{UserDAO, AdvertiserDAO, AdvertiserAccessDAO}
import models.{AdvertiserAccess, Advertiser, User}
import play.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    def storedUsers = new UserDAO
    def storedAdvertisers = new AdvertiserDAO
    def storedAdvertiserAccess = new AdvertiserAccessDAO(storedUsers, storedAdvertisers)

    val countUsers = Await.result(storedUsers.count(), Duration.Inf)
    val countAdvertisers = Await.result(storedAdvertisers.count(), Duration.Inf)
    val countAdvertiserAccess = Await.result(storedAdvertiserAccess.count(), Duration.Inf)

    if (countUsers == 0) {
      val rows = Seq(
        User(None, "alice@example.com", "secret", true, "active"),
        User(None, "bob@example.com",   "secret", false, "active"),
        User(None, "chris@example.com", "secret", false, "active")
      )

      Await.result(storedUsers.insert(rows), Duration.Inf)
    }

    if (countAdvertisers == 0) {
      val rows = Seq(
        Advertiser(None, "advertiser1"),
        Advertiser(None, "advertiser2"),
        Advertiser(None, "advertiser3"),
        Advertiser(None, "advertiser4"),
        Advertiser(None, "advertiser5")
      )

      Await.result(storedAdvertisers.insert(rows), Duration.Inf)
    }

    if (countAdvertiserAccess == 0) {
      val rows = Seq(
        AdvertiserAccess(None, 1, 1),
        AdvertiserAccess(None, 1, 2),
        AdvertiserAccess(None, 2, 1),
        AdvertiserAccess(None, 2, 2),
        AdvertiserAccess(None, 2, 3)
      )

      Await.result(storedAdvertiserAccess.insert(rows), Duration.Inf)
    }

  }

}