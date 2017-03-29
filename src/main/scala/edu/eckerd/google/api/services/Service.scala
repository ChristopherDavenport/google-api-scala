package edu.eckerd.google.api.services

import com.google.api.client.googleapis.auth.oauth2.{GoogleClientSecrets, GoogleCredential}
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory

trait Service {

  val serviceAccountEmail: String
  val impersonatedEmail: String
  val credentialFilePath: String
  val applicationName: String
  val scopes: List[String]

  val httpTransport = new NetHttpTransport
  val jsonFactory = new JacksonFactory
  val credential = getCredential(serviceAccountEmail, impersonatedEmail, credentialFilePath, applicationName, scopes)



  private def getCredential(serviceAccountEmail: String,
                    impersonatedEmail: String,
                    credentialFilePath: String,
                    applicationName: String,
                    scopes: List[String]): GoogleCredential = {
    import scala.collection.JavaConverters._

    val initCredential = new GoogleCredential.Builder()
      .setTransport(httpTransport)
      .setJsonFactory(jsonFactory)
      .setServiceAccountScopes(scopes.asJavaCollection)
      .setServiceAccountId(serviceAccountEmail)
      .setServiceAccountUser(impersonatedEmail)

    val credential: GoogleCredential = credentialFilePath match {

      case path if path.endsWith(".p12") =>
        val file = new java.io.File(credentialFilePath)
        val credential = initCredential
          .setServiceAccountPrivateKeyFromP12File(file)
          .build()
        credential

      case path if path.endsWith(".json") =>
        val inputStream = this.getClass.getResourceAsStream(credentialFilePath)
        val inputStreamReader = new java.io.InputStreamReader(inputStream)
        val clientSecrets = GoogleClientSecrets.load(jsonFactory, inputStreamReader)
        val credential = initCredential
          .setClientSecrets(clientSecrets)
          .build()
        credential

    }

    if (!credential.refreshToken()) {
      throw new RuntimeException("Failed OAuth to refresh the token")
    }

    credential
  }

}
