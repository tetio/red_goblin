package green_goblin

import org.scalatest.{BeforeAndAfterAll, FunSpec}


class mta_spec extends FunSpec with BeforeAndAfterAll{

  var token = ""
  val username = "aaaa" // found in resources
  val companyCode = "aaaa" // found in resources
  val password = "aaaa" // found in resources

  override def beforeAll(): Unit = {
    token = HttpOps.authenticate(username, password, companyCode)
    HttpOps.dbScript(token, companyCode, "data.sql")
  }

  describe("Green Goblin tries to sabotage the mta subsystem") {

    it("A testing dir should contain message files"){
      val messages = MessageOps.messagesInFolder("/messages/sample")

      assert(messages.size === 2)
    }

    it("A testing dir folder does not exist") {
      val messages = MessageOps.messagesInFolder("/no_folder")
      assert(messages.size === 0)
    }
  }
}
