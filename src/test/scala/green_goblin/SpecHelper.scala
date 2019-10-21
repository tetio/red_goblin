package green_goblin

import org.scalatest.FunSpec

class SpecHelper extends FunSpec {
  var securityToken = ""
  val username = "NESTA" // found in resources file
  val companyCode = "ESA61961488" // found in resources file
  val password = "CALL" // found in resourcesv file

  def expectTest(messages: (List[ProcessingMessage], List[ProcessingMessage]), numExpectedOK: Int, expectedWithIssues: Int = 0): Unit = {
    val messagesOk = messages._2.filter(RedGoblin.isOk)
    val messagesKo =  messages._2.filter(RedGoblin.hasErrors)
    val numMessagesWithIssues =  messagesKo.size - expectedWithIssues

    info(messageReport(messagesOk, messages._1 ++  messagesKo))

    assert(messagesOk.size == numExpectedOK, "Not all expected messages are Ok")
    assert(numMessagesWithIssues == 0, "Too many messages have issues")
    assert(messages._1.isEmpty, "Some messages are still processing")

  }


  def messageReport(messagesOk: List[ProcessingMessage], messagesKo: List[ProcessingMessage]): String = {
    var s = ""
    if (messagesOk.nonEmpty) {
      s = s + "Messages OK: "
      s = s + messagesOk.map(message2string).foldRight("")(_ +", "+ _)
    }
    if (messagesKo.nonEmpty) {
      s = s + "Messages KO: "
      s = s + messagesKo.map(message2string).foldRight("")(_ +", "+ _)
    }
    s
  }

  def message2string(m: ProcessingMessage): String =
    s"(${m.trackId}, ${m.message.documentType}, ${m.status}, ${m.error})"

}
