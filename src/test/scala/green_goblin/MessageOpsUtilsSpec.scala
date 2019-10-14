package green_goblin

import org.scalatest.FunSpec

/**
 * Created by tetio on 24/03/2017.
 */
class MessageOpsUtilsSpec extends FunSpec {

  describe("Green Goblin tries to sabotage the file system") {
    val token = "aa"

    it("A testing dir should contain message files"){
      val messages = MessageOps.messagesInFolder("/messages/sample")
      assert(messages.size === 2)
    }

    it("A testing dir folder does not exist") {
      val messages = MessageOps.messagesInFolder("/no_folder")
      assert(messages.size === 0)
    }

    it("Get the messages in a folder") {
      val messages = MessageOps.loadMessages("/messages/sample")
      assert(messages.size === 2)
      //COPARNE01.ESQ33333333.ESQ22222222.DOC00001.1.EDIXML.N.msg
      val m0 = messages.head
      assert(m0.documentType === "COPARNE01")
      assert(m0.sender === "ESQ33333333")
      assert(m0.receiver === "ESQ22222222")
      assert(m0.docNumber === "DOC00001")
      assert(m0.messageFormat === "EDIXML")
    }
  }

}
