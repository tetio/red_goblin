package green_goblin

import io.circe.syntax._
import org.scalatest.FunSpec

class SpecHelper extends FunSpec {

  var messagesOk: List[ProcessingMessage] = List()
  var messagesKo: List[ProcessingMessage] = List()

  def expectTest(messages: (List[ProcessingMessage], List[ProcessingMessage]), numExpectedOK: Int, expectedWithIssues: Int = 0): Unit = {
    val maxMessagesWithIssues = messages._2.size - expectedWithIssues
    assert(maxMessagesWithIssues == numExpectedOK, "Some messages have processing issues")
    messagesOk = messages._2 ++ messagesOk
    messagesKo = messages._1 ++ messagesKo
  }

  def finalCheck(expectedWithIssues: Int = 0): Unit = {
    if (expectedWithIssues > 0) {
      assert(expectedWithIssues < messagesKo.size, "Too many messages with issues.")
    } else {
      assert(messagesKo.isEmpty, "Too many messages with issues.")
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
  def printOkMessage(m: ProcessingMessage): Unit = {
    val s =
      s"""
         |trackId: ${m.trackId}
         |docType: ${m.message.documentType}
         |""".stripMargin
    print(s)
  }
  def printKoMessage(m: ProcessingMessage): Unit = {
    val s =
      s"""
        |trackId: ${m.trackId}
        |docType: ${m.message.documentType}
        |status: ${m.status}
        |error: ${m.error}
        |""".stripMargin
    print(s)
  }
}
