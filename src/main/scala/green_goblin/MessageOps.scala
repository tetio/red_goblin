package green_goblin

import java.io.File

import scala.io.Source

/**
 * Created by tetio on 23/03/2017.
 */
case class Message(documentType: String, sender: String, receiver: String, docNumber: String, docVersion: String,
                   messageFormat: String, signed: String, message: String)

case class ProcessingMessage(message: Message, trackId: String, status: String, error: String)

object MessageOps {

  def messagesInFolder(path: String): Iterable[File] = {
    getClass.getResource(path) match {
      case null => List()
      case _ =>
        new File(getClass.getResource(path).getPath).listFiles(_.getName.endsWith(".msg"))
          .map(_.getAbsoluteFile)
    }
  }

  def loadMessages(path: String): List[Message] = {
    messagesInFolder(path).map(f => {
      val data = f.getName().split("\\.")
      val bufferedSource = Source.fromFile(f)
      val contents = bufferedSource.getLines().mkString
      bufferedSource.close()
      Message(data(0), data(1), data(2), data(3), data(4), data(5), data(6), contents)
    }).toList
  }
}
