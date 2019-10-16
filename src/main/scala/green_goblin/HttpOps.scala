package green_goblin

import com.softwaremill.sttp._
import io.circe.generic.semiauto.deriveDecoder
import io.circe.syntax._
import io.circe.{Decoder, parser}

case class DummyResult(data: String)

case class AuthResult(token: String)

case class SendMessageResult(trackId: String)

case class MessageStatusResult(status: String, error: String)

case class DBScriptResult(status: Boolean)

object HttpOps {
  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
  //val url = "http://localhost:4000/porticconnector-cxf/services/rest"
  val url = "http://10.120.1.182:12100/porticconnector-cxf/services/rest"

  def ping(): String = {
    val request = sttp.contentType("application/json").post(uri"$url/dummy1")
    implicit val resultDecoder: Decoder[DummyResult] = deriveDecoder[DummyResult]
    val response = request.send()
    response.code match {
      case 200 =>
        response.body match {
          case Right(x) =>
            val decodeResult = parser.decode[DummyResult](x)
            decodeResult match {
              case Right(result) => result.data
              case Left(error) => error.getMessage
            }
          case Left(error) => error
        }
      case _ => "ERROR"
    }
  }


  def authenticate(username: String, password: String, companyCode: String): String = {
    val payload: String = Map("username" -> username, "password" -> password, "companyCode" -> companyCode).asJson.toString()
    val request = sttp.contentType("application/json")
      .body(payload)
      .post(uri"$url/authenticate")
    implicit val resultDecoder: Decoder[AuthResult] = deriveDecoder[AuthResult]
    val response = request.send()
    response.code match {
      case 200 =>
        response.body match {
          case Right(x) =>
            val decodeResult = parser.decode[AuthResult](x)
            decodeResult match {
              case Right(result) => result.token
              case Left(error) => error.getMessage
            }
          case Left(error) => error
        }
      case _ => "ERROR"
    }
  }


  //  def sendMessage(securityToken: String, companyCode: String, documentType: String, sender: String, receiver: String,
  //                  msgNumber: String, numVersion: String, messageFormat: String, message: String, digest: String,
  //                  signed: String): String = {
  def sendMessage(securityToken: String, companyCode: String, m: Message): String = {
    val payload = Map("securityToken" -> securityToken, "companyCode" -> companyCode, "documentType" -> m.documentType,
      "sender" -> m.sender, "receiver" -> m.receiver, "msgNumber" -> m.docNumber,
      "numVersion" -> m.docVersion, "messageFormat" -> m.messageFormat, "message" -> m.message,
      "signed" -> m.signed).asJson.toString()
    val request = sttp.contentType("application/json")
      .body(payload).post(uri"$url/sendmessage")
    implicit val resultDecoder: Decoder[SendMessageResult] = deriveDecoder[SendMessageResult]
    val response = request.send()
    response.code match {
      case 200 =>
        response.body match {
          case Right(x) =>
            val decodeResult = parser.decode[SendMessageResult](x)
            decodeResult match {
              case Right(result) => result.trackId
              case Left(error) => error.getMessage
            }
          case Left(error) => error
        }
      case _ => "ERROR"
    }
  }

  def messageStatus(securityToken: String, companyCode: String, trackId: String, msgId: String): Map[String, String] = {
    val payload = Map("securityToken" -> securityToken, "companyCode" -> companyCode, "trackId" -> trackId, "msgId" -> msgId).asJson.toString()
    val request = sttp.contentType("application/json")
      .body(payload)
      .post(uri"$url/messagestatus")
    implicit val resultDecoder: Decoder[MessageStatusResult] = deriveDecoder[MessageStatusResult]
    val response = request.send()
    response.code match {
      case 200 =>
        response.body match {
          case Right(x) => {
            val decodeResult = parser.decode[MessageStatusResult](x)
            decodeResult match {
              case Right(result) => Map("status" -> result.status, "error" -> result.error)
              case Left(error) => Map("situation" -> error.getMessage)
            }
          }
          case Left(error) => Map("situation" -> error)
        }
      case _ => Map("situation" -> "error")
    }
  }

  def dbScript(securityToken: String, companyCode: String, name: String): Boolean = {
    val payload = Map("securityToken" -> securityToken, "name" -> name, "companyCode" -> companyCode).asJson.toString()
    val request = sttp.contentType("application/json")
      .body(payload)
      .post(uri"$url/dbscript")
    implicit val resultDecoder: Decoder[DBScriptResult] = deriveDecoder[DBScriptResult]
    val response = request.send()
    response.code match {
      case 200 =>
        response.body match {
          case Right(x) =>
            val decodeResult = parser.decode[DBScriptResult](x)
            decodeResult match {
              case Right(result) => result.status
              case Left(_) => false
            }
          case Left(_) => false
        }
      case _ => false
    }
  }
}
