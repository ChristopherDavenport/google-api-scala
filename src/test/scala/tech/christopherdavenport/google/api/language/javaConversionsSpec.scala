package tech.christopherdavenport.google.api.language

import org.scalatest.{FlatSpec, Matchers}
import com.google.api.services.admin.{directory => jDirectory}
import com.google.api.services.{calendar => jCalendar, drive => jDrive}
import com.typesafe.config.ConfigFactory
import tech.christopherdavenport.google.api.services.Scopes._
import tech.christopherdavenport.google.api.services.admin.{directory => sDirectory}
import tech.christopherdavenport.google.api.services.{calendar => sCalendar, drive => sDrive}



/**
  * Created by davenpcm on 5/11/16.
  */
class javaConversionsSpec extends FlatSpec with Matchers {

  val config = ConfigFactory.load().getConfig("googleTest")
  val applicationName = config.getString("applicationName")
  val credentialFilePath = config.getString("credentialFilePath")
  val serviceAccountEmail = config.getString("serviceAccountEmail")
  val adminImpersonatedEmail = config.getString("administratorEmail")

  val ListScopes = CALENDAR :: ADMIN_DIRECTORY ::: DRIVE

  "scalaDirectoryAsJavaDirectoryConversion" should "convert a scala Directory to a Java Directory" in {
    val adminDir = sDirectory.Directory(serviceAccountEmail,
      adminImpersonatedEmail,
      credentialFilePath,
      applicationName,
      ListScopes
    )
    val jDir = JavaConversions.scalaDirectoryAsJavaDirectoryConversion(adminDir)
    jDir shouldBe a [jDirectory.Directory]
  }

  it should "maintain the application name through conversion" in {
    val adminDir = sDirectory.Directory(serviceAccountEmail,
      adminImpersonatedEmail,
      credentialFilePath,
      applicationName,
      ListScopes
    )
    val jDir = JavaConversions.scalaDirectoryAsJavaDirectoryConversion(adminDir)
    jDir.getApplicationName === applicationName
  }

  "scalaGroupAsJavaGroupConversion" should "convert a scala group to java group" in {
    val group = sDirectory.models.Group("TestGroup", "test@test.com")
    val jGroup = JavaConversions.scalaGroupAsJavaGroupConversion(group)
    jGroup shouldBe a [jDirectory.model.Group]
  }

  it should "maintain the group name through conversion" in {
    val name = "TestGroup"
    val group = sDirectory.models.Group(name, "test@test.com")
    val jGroup = JavaConversions.scalaGroupAsJavaGroupConversion(group)
    jGroup.getName === name
  }

  it should "maintain the email through conversion" in {
    val email = "test@test.com"
    val group = sDirectory.models.Group("TestGroup", email)
    val jGroup = JavaConversions.scalaGroupAsJavaGroupConversion(group)
    jGroup.getEmail === email
  }

  it should "convert a id of None to null" in {
    val group = sDirectory.models.Group("TestGroup", "test@test.com")
    val jGroup = JavaConversions.scalaGroupAsJavaGroupConversion(group)
    jGroup.getId === null
  }

  it should "convert a description of None to null" in {
    val group = sDirectory.models.Group("TestGroup", "test@test.com")
    val jGroup = JavaConversions.scalaGroupAsJavaGroupConversion(group)
    jGroup.getDescription === null
  }

  it should "convert a directMembersCount of None to null" in {
    val group = sDirectory.models.Group("TestGroup", "test@test.com")
    val jGroup = JavaConversions.scalaGroupAsJavaGroupConversion(group)
    jGroup.getDirectMembersCount === null
  }

}
