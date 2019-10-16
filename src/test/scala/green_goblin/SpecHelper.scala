package green_goblin

import io.circe.syntax._
import org.scalatest.FunSpec

class SpecHelper extends FunSpec {

  var messagesOk: List[ProcessingMessage] = List()
  var messagesKo: List[ProcessingMessage] = List()

  def expectTest(messages: (List[ProcessingMessage], List[ProcessingMessage]), numExpectedOK: Int, expectedWithIssues: Int = 0): Unit = {
    val maxMessagesWithIssues = messages._2.size - expectedWithIssues
    assert(maxMessagesWithIssues == numExpectedOK, "Some messages have processing issues")
    messagesOk = messages._2.filter(RedGoblin.isOk) ++ messagesOk
    messagesKo = messages._1 ++ messagesKo
    messagesKo =  messages._2.filter(RedGoblin.hasErrors) ++ messagesKo
    addInfo()
  }

  def finalCheck(expectedWithIssues: Int = 0): Unit = {
    if (expectedWithIssues > 0) {
      assert(expectedWithIssues < messagesKo.size, "Too many messages with issues.")
    } else {
      assert(messagesKo.nonEmpty, "Too many messages with issues.")
    }
    if (messagesOk.nonEmpty) {
      println("*** Messages ok ***")
      messagesOk.foreach(m => printOkMessage(m))
    }
    if (messagesKo.nonEmpty) {
      println("*********** Messages ko **********")
      messagesKo.foreach(m => printKoMessage(m))
    }
  }

  def addInfo(): Unit = {
    if (messagesOk.nonEmpty) {
      info("*** Messages ok ***")
      messagesOk.foreach(m => printOkMessage(m))
    }
    if (messagesKo.nonEmpty) {
      info("*********** Messages ko **********")
      messagesKo.foreach(m => printKoMessage(m))
    }

  }
  def printOkMessage(m: ProcessingMessage): Unit = {
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

  def printKoMessage(m: ProcessingMessage): Unit = {
    val s =
      s"""
        |trackId: ${m.trackId}
        |docType: ${m.message.documentType}
        |status: ${m.status}
        |error: ${m.error}
        |""".stripMargin
    info(s)
  }
}
