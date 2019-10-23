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

    info("info: "+messageReport(messagesOk, messages._1 ++  messagesKo))
    note("note: "+messageReport(messagesOk, messages._1 ++  messagesKo))
    alert("alert: "+messageReport(messagesOk, messages._1 ++  messagesKo))

    if (expectedWithIssues == 0) {
      assert(messagesOk.size == numExpectedOK, "Not all expected messages are Ok")
    } else {
      assert(numMessagesWithIssues == 0, "Too many messages have issues")
    }
    assert(messages._1.isEmpty, "Some messages are still processing")

  }


  def messageReport(messagesOk: List[ProcessingMessage], messagesKo: List[ProcessingMessage]): String = {
    val ok = messagesOk.map(message2string).foldRight("")(_ +", "+ _).orElse("-")
    val ko = messagesKo.map(message2string).foldRight("")(_ +", "+ _).orElse("-")
    //s"Messages OK/KO: $ok / $ko"
    s"Messages OK/KO: ${messagesOk.map(message2string).foldRight("")(_ +", "+ _)} / ${messagesKo.map(message2string).foldRight("")(_ +", "+ _)}"
  }

  def message2string(m: ProcessingMessage): String =
    //s"(${m.trackId}, ${m.message.documentType}, ${m.status}, ${m.error}, ${m.message.path})"
    s"(${m.trackId}, ${m.status}, ${m.error}, ${m.message.fileName})"

}
