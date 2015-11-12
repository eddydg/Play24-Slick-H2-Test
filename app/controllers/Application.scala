package controllers

import dao._
import models._
import play.api.Play
import play.api.data.Forms._
import play.api.data._
import play.api.db.slick.DatabaseConfigProvider
import play.api.mvc.{Action, Controller}
import slick.driver.H2Driver.api._
import slick.driver.JdbcProfile
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class Application extends Controller{

  val db = DatabaseConfigProvider.get[JdbcProfile](Play.current).db
  //create an instance of the table

  def index = Action{
    val users: List[User] = Await.result(Users.all, Duration.Inf)
    val result: List[(User, Seq[(Advertiser, Boolean)])] = {
      users.map{ user =>
        val couple = Await.result(Users.getAdverts(user), Duration.Inf)
        val adverts: Seq[(Advertiser, Boolean)] = couple.map{ x =>
          val adv = Await.result(Advertisers.findById(x._1), Duration.Inf).get
          (adv, x._2)
        }
        (user, adverts)
      }
    }
    Ok(views.html.index(result))
  }


  /*def index = Action.async {
    Users.all.flatMap { case user => Users.getAdverts(user.head).flatMap { case couple =>
      for {
        u <- db.run(Users.query.result).map(_.toList)
        a <- db.run(Advertisers.query.result).map(_.toList)
        aa <- db.run(AdvertiserAccesses.query.result).map(_.toList)
        adv <- Advertisers.findById(couple._1)
      } yield Ok(views.html.index(u, a, aa, couple))
    }
    }
  }*/

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