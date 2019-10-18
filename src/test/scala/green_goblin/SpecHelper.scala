package green_goblin

import org.scalatest.FunSpec

class SpecHelper extends FunSpec {

  def expectTest(messages: (List[ProcessingMessage], List[ProcessingMessage]), numExpectedOK: Int, expectedWithIssues: Int = 0): Unit = {
    val messagesOk = messages._2.filter(RedGoblin.isOk)
    val messagesKo =  messages._2.filter(RedGoblin.hasErrors)
    val numMessagesWithIssues =  messagesKo.size - expectedWithIssues

    info(messageReport(messagesOk, messages._1 ++  messagesKo))

    assert(messagesOk.size == numExpectedOK, "Not all expected messages are Ok")
    assert(numMessagesWithIssues == 0, "Too many messages have issues")
    assert(messages._1.isEmpty, "Some messages are still processing")

  }


  def messageReport(allMessagesOk: List[ProcessingMessage], allMessagesKo: List[ProcessingMessage]): String = {
    var s = ""
    if (allMessagesOk.nonEmpty) {
      s = s + "Messages OK: "
      s = s + allMessagesOk.map(m => printMessage(m)).foldRight("")(_ +", "+ _)
    }
    if (allMessagesKo.nonEmpty) {
      s = s + "Messages KO: "
      s = s + allMessagesKo.map(m => printMessage(m)).foldRight("")(_ +", "+ _)
    }
    s
  }

  def printMessage(m: ProcessingMessage): String = {
    //val isOk = !RedGoblin.hasErrors(m) && !RedGoblin.isProcessing(m)
    val s = s"(${m.trackId}, ${m.message.documentType}, ${m.status}, ${m.error})"
    s
  }

}
