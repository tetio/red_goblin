package green_goblin

import scala.annotation.tailrec

object RedGoblin {
  var securityToken: String = ""
  var companyCode: String = ""

  def throwPumpkin(aSecurityToken: String, aCompanyCode: String, messages: List[Message]): (List[ProcessingMessage], List[ProcessingMessage]) = {
    securityToken = aSecurityToken
    companyCode = aCompanyCode
    val sentMessages = send(messages)

    Thread.sleep(3 * 1000L)

    val processedMessages = process(sentMessages)
    processedMessages
  }

  private def send(messages: List[Message]): List[ProcessingMessage] = {
    val processingMessages = messages.map(m => {
      val trackId = HttpOps.sendMessage(securityToken, companyCode, m)
      ProcessingMessage(m, trackId, "", "")
    })
    processingMessages
  }

  def isProcessing(m: ProcessingMessage): Boolean =
    m.status == "OKLB" || m.status == "OKS" || m.status == "OKIP" || m.status == "OKPC" || m.status == "" || m.status == "null"

  def hasErrors(m: ProcessingMessage): Boolean =
    m.status == "OKNO" || m.status == "ERR" && (m.error != "ERMO" && m.error != "ERMA")

  def isOk(m: ProcessingMessage): Boolean = !hasErrors(m) && !isProcessing(m)

  def process(messages: List[ProcessingMessage]): (List[ProcessingMessage], List[ProcessingMessage]) = {

    @tailrec def doProcessingAcc(messages: List[ProcessingMessage], result: List[ProcessingMessage], times: Int): (List[ProcessingMessage], List[ProcessingMessage]) = {
      var tmpPending: List[ProcessingMessage] = List()
      var tmpProcessed: List[ProcessingMessage] = result
      messages.foreach(m => {
        val result = HttpOps.messageStatus(securityToken, companyCode, m.trackId, "1")
        val pm = ProcessingMessage(m.message, m.trackId, result.getOrElse("status", ""), result.getOrElse("error", ""))
        if (hasErrors(pm) || !isProcessing(pm)) {
          tmpProcessed = pm :: tmpProcessed
        } else {
          tmpPending = pm :: tmpPending
        }
      })
      if (times > 3 || tmpPending.isEmpty) {
        (tmpPending, tmpProcessed)
      } else {
        Thread.sleep(times * 1000L)
        doProcessingAcc(tmpPending, tmpProcessed, times + 1)
      }
    }

    doProcessingAcc(messages, List(), 0)
  }

}
