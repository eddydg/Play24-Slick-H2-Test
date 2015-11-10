package controllers

import dao._
import models._
import play.api.data.Forms._
import play.api.data._
import play.api.mvc.{Action, Controller}
import slick.driver.H2Driver.api._
import scala.concurrent.ExecutionContext.Implicits.global

class Application extends Controller{

  val db = Database.forURL("jdbc:h2:mem:play;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
  //create an instance of the table
  def index = Action.async {
    Users.all.flatMap { case user => Users.getAdverts(user.head).flatMap { case myadv =>
      for {
        u <- db.run(Users.query.result).map(_.toList)
        a <- db.run(Advertisers.query.result).map(_.toList)
        aa <- db.run(AdvertiserAccesses.query.result).map(_.toList)
      } yield Ok(views.html.index(u, a, aa, myadv))
    }
    }
  }

  val userForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "email" -> text,
      "password" -> text,
      "isSuper" -> boolean,
      "status" -> text
    )(User.apply)(User.unapply)
  )

  def insert = Action.async { implicit rs =>
    val user = userForm.bindFromRequest.get
    Users.insert(user).map(_ => Redirect(routes.Application.index))
  }
}