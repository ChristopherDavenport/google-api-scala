package edu.eckerd.google.api.services.directory.models

import language.implicitConversions

case class User(name: Name,
                primaryEmail: Email,
                password: Option[String] = None,
                id: Option[String] = None,
                orgUnitPath: String = "/",
                agreedToTerms: Option[Boolean] = Some(false),
                changePasswordAtNextLogin: Boolean = false,
                includeInGlobalAddressList: Boolean = true,
                ipWhiteListed: Boolean = false,
                isAdmin: Boolean = false,
                isMailboxSetup: Boolean = false,
                suspended: Boolean = false
               )

object User {
  implicit def toMember(user: User): Member ={
    Member(
      Some(user.primaryEmail)
    )
  }
}