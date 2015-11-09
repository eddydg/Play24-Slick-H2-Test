package models

case class User(id: Option[Long], email: String, password: String, is_super: Boolean, status: String)
