import com.softwaremill.sttp._
import io.circe.{Decoder, parser}
import io.circe.generic.semiauto.deriveDecoder

case class DummyResult(data: String)
case class AuthResult(token: String)
case class SendMessageResult(trackId: String)
case class MessageStatusResult(status: String, error: String)
case class DBScriptResult(status: Boolean)

object HttpOps {

  implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
  val url = "http://localhost:4000/porticconnector-cxf/services/rest"

  def ping(): String = {

    val request = sttp.post(uri"$url/dummy1")
    val response = request.send()
    implicit val resultDecoder: Decoder[DummyResult] = deriveDecoder[DummyResult]
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


  def authenticate(username: String, password: String, companyId: String): String = {

    val request = sttp.body(Map("username" -> username, "password" -> password, "companyId" -> companyId))
      .post(uri"$url/authenticate")
    val response = request.send()
    implicit val resultDecoder: Decoder[AuthResult] = deriveDecoder[AuthResult]
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


  def sendMessage(securityToken: String, companyCode: String, documentType: String, sender: String, receiver: String,
                  msgNumber: String, numVersion: String, messageFormat: String, message: String, digest: String,
                  signed: String): String = {

    val request = sttp.body(Map("securityToken" -> securityToken, "companyCode" -> companyCode, "documentType" -> documentType,
      "sender" -> sender, "receiver" -> receiver, "msgNumber" -> msgNumber,
      "numVersion" -> numVersion, "messageFormat" -> messageFormat, "message" -> message,
      "signed" -> signed)).post(uri"$url/sendmessage")
    val response = request.send()
    implicit val resultDecoder: Decoder[SendMessageResult] = deriveDecoder[SendMessageResult]
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

    val request = sttp.body(Map("securityToken" -> securityToken, "companyCode" -> companyCode, "trackId" -> trackId, "msgId" -> msgId))
      .post(uri"$url/messagestatus")
    val response = request.send()
    implicit val resultDecoder: Decoder[MessageStatusResult] = deriveDecoder[MessageStatusResult]
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

    val request = sttp.body(Map("securityToken" -> securityToken, "name" -> name, "companyCode" -> companyCode))
      .post(uri"$url/dbscript")
    val response = request.send()
    implicit val resultDecoder: Decoder[DBScriptResult] = deriveDecoder[DBScriptResult]
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
