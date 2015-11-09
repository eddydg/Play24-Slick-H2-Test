package controllers

import javax.inject.Inject

import models.User
import dao.{AdvertiserDAO, UserDAO}
import play.api.Play
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick.DatabaseConfigProvider

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc.Action
import play.api.mvc.Controller
import slick.driver.JdbcProfile


class Application @Inject() (userDao: UserDAO, advertiserDAO: AdvertiserDAO) extends Controller{

  //create an instance of the table
  def index = Action.async {
    userDao.all().map(u => Ok(views.html.index(u)))
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
    userDao.insert(user).map(_ => Redirect(routes.Application.index))
  }
}