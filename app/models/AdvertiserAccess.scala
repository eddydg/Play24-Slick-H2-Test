package models

case class AdvertiserAccess(advertiserAccessId: Option[Long], userId: Long, advertiserId: Long, canWrite: Boolean)