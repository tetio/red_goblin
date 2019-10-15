package green_goblin

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
    // TODO prepare report
  }
}
