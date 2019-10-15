package green_goblin

import scala.annotation.tailrec

object RedGoblin {
  var securityToken: String = ""
  var companyCode: String = ""

  def throwPumpkin(aSecurityToken: String, aCompanyCode: String, messages: List[Message]): (List[ProcessingMessage], List[ProcessingMessage]) = {
    securityToken = aSecurityToken
    companyCode = aCompanyCode
    val sentMessages = send(messages)
    val processedMessages = process(sentMessages)
    processedMessages
  }

  private def send(messages: List[Message]): List[ProcessingMessage] = {
    val processingMessages = messages.map(m => {
      val trackId = HttpOps.sendMessage(securityToken, companyCode, m)
      ProcessingMessage(m, trackId, "-", "-")
    })
    processingMessages
  }

  def isProcessed(status: String, error: String): Boolean =
    status == "OKLB" || status == "OKS" || status == "OKIP" || status == "OKPC" || status == "" || status == "null"

  def process(messages: List[ProcessingMessage]): (List[ProcessingMessage], List[ProcessingMessage]) = {

    @tailrec def doProcessingAcc(messages: List[ProcessingMessage], result: List[ProcessingMessage], times: Int): (List[ProcessingMessage], List[ProcessingMessage]) = {
      var tmpPending: List[ProcessingMessage] = List()
      var tmpProcessed: List[ProcessingMessage] = result
      messages.foreach(m => {
        val result = HttpOps.messageStatus(securityToken, companyCode, m.trackId, "1")
        val pm = ProcessingMessage(m.message, m.trackId, result.getOrElse("status", "-"), result.getOrElse("error", "-"))
        if (isProcessed(pm.status, pm.error)) {
          tmpProcessed = pm :: tmpProcessed
        } else {
          tmpPending = pm :: tmpPending
        }
      })
      if (times > 10 || tmpPending.isEmpty) {
        (tmpPending, tmpProcessed)
      } else {
        Thread.sleep(times * 1000L)
        doProcessingAcc(tmpPending, tmpProcessed, times + 1)
      }
    }

    doProcessingAcc(messages, List(), 0)
  }

}
