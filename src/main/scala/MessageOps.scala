import java.io.File
import java.nio.file.{Files, Path, Paths}

import scala.io.Source

/**
 * Created by tetio on 23/03/2017.
 */
case class Message(documentType: String, sender: String, receiver: String, msgNumber: String, numVersion: String,
                          messageFormat: String, signed: String, message: String)
//,digest: String, securityToken: String,
//                          companyCode: String, trackId: String, status: String, error: String, path: String)

object MessageOps {

  def messagesInFolder(path: String): Iterable[File] = {
    getClass.getResource(path) match {
      case null => List()
      case _ =>
        new File(getClass.getResource(path).getPath).listFiles(_.getName.endsWith(".msg"))
          .map(_.getAbsoluteFile)
    }
  }

  def loadMessages(path: String): Iterable[Message] = {
    messagesInFolder(path).map(f => {
      val data = f.getName().split("\\.")
      val bufferedSource = Source.fromFile(f)
      val contents  = bufferedSource.getLines().mkString
      bufferedSource.close()
      Message(data(0), data(1), data(2), data(3), data(4), data(5), data(6), contents)
    })
  }
}
