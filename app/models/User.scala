package models

import dao.Users
import slick.driver.H2Driver.api._

case class User(id: Option[Long], email: String, password: String, is_super: Boolean, status: String)
{
  def advertisers = for {
    user <- Users.query.filter(_.id === this.id)
    advert <- user.advertisers
  } yield advert
}
