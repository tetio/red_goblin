package green_goblin

import io.circe.syntax._
import org.scalatest.FunSpec

class SpecHelper extends FunSpec {

  var allMessagesOk: List[ProcessingMessage] = List()
  var allMessagesKo: List[ProcessingMessage] = List()

  def expectTest(messages: (List[ProcessingMessage], List[ProcessingMessage]), numExpectedOK: Int, expectedWithIssues: Int = 0): Unit = {
    val messagesOk = messages._2.filter(RedGoblin.isOk)
    val messagesKo =  messages._2.filter(RedGoblin.hasErrors)
    val numMessagesWithIssues =  messagesKo.size - expectedWithIssues

    assert(messagesOk.size == numExpectedOK, "Not all expected messages are Ok")
    assert(numMessagesWithIssues == 0, "Too many messages have issues")
    assert(messages._1.size  == 0, "Some messages are still processing")

    allMessagesOk = messagesOk ++ allMessagesOk
    allMessagesKo = messages._1 ++ allMessagesKo
    allMessagesKo =  messagesKo ++ allMessagesKo

    addInfo()
  }

  def finalCheck(expectedWithIssues: Int = 0): Unit = {
    if (expectedWithIssues > 0) {
      assert(expectedWithIssues < allMessagesKo.size, "Too many messages with issues.")
    } else {
      assert(allMessagesKo.nonEmpty, "Too many messages with issues.")
    }
    if (allMessagesOk.nonEmpty) {
      alert("*** Messages ok ***")
      allMessagesOk.foreach(m => printMessage(m))
    }
    if (allMessagesKo.nonEmpty) {
      alert("*********** Messages ko **********")
      allMessagesKo.foreach(m => printMessage(m))
    }
  }

  def addInfo(): Unit = {
    if (allMessagesOk.nonEmpty) {

      info("*** Messages ok ***")
      allMessagesOk.foreach(m => printMessage(m))
    }
    if (allMessagesKo.nonEmpty) {
      info("*********** Messages ko **********")
      allMessagesKo.foreach(m => printMessage(m))
    }

  }
  def printMessage(m: ProcessingMessage): Unit = {
    val isOk = !RedGoblin.hasErrors(m) && !RedGoblin.isProcessing(m)
    val s =
      s"""
         |processedOk: $isOk
         |trackId: ${m.trackId}
         |docType: ${m.message.documentType}
         |status: ${m.status}
         |error: ${m.error}
         |""".stripMargin
    info(s)
  }

}
