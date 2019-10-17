package report.creator
import scala.io.Source
import java.io.File
import scala.xml.XML
import java.io.FileWriter
import scala.collection.mutable.Buffer
import java.io.{ BufferedReader, FileOutputStream, File }
import java.util.zip.{ ZipEntry, ZipOutputStream }

class HtmlReport() {

  val index1 = new FileWriter(s"target/test-reports/index.html", false)
  index1.close()
  val index = new FileWriter(s"target/test-reports/index.html", true)

  index.write("<!DOCTYPE html><html><head>" + " <script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>" + "<style> .mashery {margin-top: -0.7cm;margin-left: -0.7cm;margin-right: -0.7cm;} .papi {color: white; font-family:arial; font-size: 40pt;background-color:black;} h1 {font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;fontsize: 14px;line-height: 1.42857;color: white;background-color:#515151} body {font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;fontsize: 14px;line-height: 1.42857;color: #333333;background-color:#515151} a {text-decoration:none;font-size: 10pt} a:hover {text-decoration:underline;} p { color: #91BCBF;font-size: 10pt }</style></head><center><div class= \"mashery\"><div class= \"papi\"><p style=\"font-size: 20pt\"><font style=\"color:white; font-size: 20pt\">Test</font> Suite Results</p></font></div><body><h1><center><font> Test Suite Results</font></center></h1><center>")
  var totalPass, totalFail, totalCancel = 0
  var totalTime = 0.0
  var name = ""

  for (file <- new File("target/test-reports").listFiles) {
    if (file.getName().endsWith(".xml") && file.getName().contains("TEST-")) {
      val xml = XML.loadFile(file)
      val str = file.getName().replace(".xml", "")
      val fws = new FileWriter(s"target/test-reports/$str.html", false)
      fws.close()
      val fw = new FileWriter(s"target/test-reports/$str.html", true)
      fw.write("<html><script type=\"text/javascript\">function toggleFailureDisplay(ev){var str =  ev.nextSibling; var isOn= str.style.display;if(isOn === \"none\"){str.style.display=\"block\"}else{str.style.display=\"none\"}}</script><style>p { color: #91BCBF;font-size: 10pt } h1 {font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;fontsize: 14px;line-height: 1.42857;color: white;background-color:#515151} body {font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;fontsize: 14px;line-height: 1.42857;color: #333333;background-color:#515151}</style><body> <h1><center>Test Results For Class " + str.replace("TEST-", "") + "</center></h1><table width=\"100%\" border= \"1px solid black\"><tr><th><p style=\"color:#AEAEAE\">S.No.</p></th><th><p style=\"color:#AEAEAE\">Test Case Name</></th><th><p style=\"color:#AEAEAE\">Time To Execute</p></th><th><p style=\"color:#AEAEAE\">Status</p></th><th><p style=\"color:#AEAEAE\">Failure Message</p></th></tr>")
      val x = xml \\ "testcase"
      var testNo, pass, fail, cancel = 0
      val testSuite = xml \\ "testsuite"
      val totalTimeToExecute = testSuite(0).attribute("time").get.mkString.toDouble
      for (y <- x) {
        testNo = testNo + 1

        val names = y.attribute("name").get.mkString
        var i = 0;
        name = names.mkString.map(f => { i = i + 1; if (i == 200) { i = 0; s" $f" } else f }).mkString
        val timeToExcute = y.attribute("time").get
        val failures = y \\ "failure"
        val cancels = y \\ "skipped"
        i = 0
        val failure = failures.mkString.map(f => { i = i + 1; if (i == 70) { i = 0; s" $f" } else f })
        val testResult = "<td><p style=\"color:#AEAEAE\">" + testNo + "</p></td>" + "<td><p>" + name + "</p></td>" + "<td><p>" + timeToExcute + "</p></td>"
        if (failure.isEmpty && cancels.isEmpty) {
          pass = pass + 1
          fw.write("<tr>" + testResult + "<td><p style=\"color:#00CC00\">Pass</p></td>" + "<td></td>" + "</tr>")
        } else if (cancels.isEmpty) {
          fail = fail + 1
          fw.write("<tr>" + testResult + "<td><p style=\"color:#CC3333\">Fail</p></td>" + "<td width=\"60%\";\"><a style=\"text-decoration:underline;color:#CC3333\" onclick=\"toggleFailureDisplay(event.currentTarget)\"><p style=\"color:#CC3333\">Click for failure message</p></a><p id=\"failureContainer\" style=\"display:none\">" + failure.mkString + "</p></td>" + "</tr>")
        } else {
          cancel = cancel + 1;
          fw.write("<tr>" + testResult + "<td><p style=\"color:orange\">Cancel</p></td>" + "<td>" + cancels + "</td>" + "</tr>")
        }
      }
      totalTime = totalTime + totalTimeToExecute
      totalPass = totalPass + pass
      totalFail = totalFail + fail
      totalCancel = totalCancel + cancel
      val total = pass + fail + cancel
      fw.write("<table border= \"1px solid black\"><tr>" + "<th><p style=\"color:white\">" + s"Total Tests Executed : $total</p></th>" + "<th><p style=\"color:white\">" + s"Total Tests Pass : $pass</p></th>" + "<th><p style=\"color:white\">" + s"Total Tests Fail : $fail</p></th>" + "<th><p style=\"color:white\">" + s"Total Tests Cancel : $cancel</p></th>" + "<th><p style=\"color:white\">" + s"Total Time For Execution  : $totalTimeToExecute seconds</p></th></tr></table></table></body></html>")
      if (fail == 0 && cancel == 0)
        index.write("<table width=\"75% \" border= \"1px solid black\"><tr>" + "<th width=\"20% \"><a href=\"" + file.getName().replace(".xml", "") + ".html\"" + "style=\"color:#00CC00\">" + file.getName().replace("TEST-mashery.papi.Papi", "").replace(".xml", "") + "</a></th><th width=\"20% \"><p>Total Tests Executed" + s" : $total</p></th>" + "<th width=\"14% \"><p>Total Tests Pass" + s" : $pass" + "</p></th><th width=\"14%\"><p>" + s"Total Tests Fail : $fail</p></th>" + "<th width=\"17%\"><p>" + s"Total Tests Cancel : $cancel</p></th>" + "</tr></table></table></body></html>")
      else if (cancel == 0)
        index.write("<table width=\"75% \" border= \"1px solid black\"><tr>" + "<th width=\"20% \"><a href=\"" + file.getName().replace(".xml", "") + ".html" + "\"style=\"color:#CC3333\">" + file.getName().replace("TEST-mashery.papi.Papi", "").replace(".xml", "") + "</a></th><th width=\"20% \"><p>Total Tests Executed" + s" : $total</p></th>" + "<th width=\"14% \"><p>Total Tests Pass" + s" : $pass" + "</p></th><th width=\"14%\"><p>" + s"Total Tests Fail : $fail</p></th>" + "<th width=\"17%\"><p>" + s"Total Tests Cancel : $cancel</p></th>" + "</tr></table></table></body></html>")
      else index.write("<table width=\"75% \" border= \"1px solid black\"><tr>" + "<th width=\"20% \"><a href=\"" + file.getName().replace(".xml", "") + ".html" + "\"style=\"color:orange\">" + file.getName().replace("TEST-mashery.papi.Papi", "").replace(".xml", "") + "</a></th><th width=\"20% \"><p>Total Tests Executed" + s" : $total</p></th>" + "<th width=\"14% \"><p>Total Tests Pass" + s" : $pass" + "</p></th><th width=\"14%\"><p>" + s"Total Tests Fail : $fail</p></th>" + "<th width=\"17%\"><p>" + s"Total Tests Cancel : $cancel</p></th>" + "</tr></table></table></body></html>")
      fw.close()
    }
  }
  val totalTests = totalPass + totalFail + totalCancel
  index.write("<table width=\"75% \" border= \"1px solid black\"><tr>" + "<th><p style=\"color:white\">" + s"Total Tests Executed : $totalTests</p></th><th>" + "<p style=\"color:white\">" + s"Total Tests Pass : $totalPass</p></th><th>" + "<p style=\"color:white\">" + s"Total Tests Fail : $totalFail</p></th><th>" + "<p style=\"color:white\">" + s"Total Tests Cancel : $totalCancel</p></th>" + "<th><p style=\"color:white\">" + s"Execution Time: $totalTime seconds</p></th></tr></table></center>")
  index.write("<head>" +
    "<script type=\"text/javascript\">" +
    "google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});" +
    "google.setOnLoadCallback(drawChart);" +
    "function drawChart() {" +
    "var data = google.visualization.arrayToDataTable([" +
    "['Task', 'Hours per Day'], ['Pending',0], ['Fail'," + totalFail + "],['Cancel',  " + totalCancel + "],['Pass', " + totalPass + "]]);" +
    "var options = {title: 'Test Suite Results',titleTextStyle: { color: 'white', fontName: 'arial', fontSize: 20, bold: false, italic: false },backgroundColor: '#515151'};var chart = new google.visualization.PieChart(document.getElementById('piechart'));chart.draw(data, options);}" +
    "</script></head><center><div class= \"piechart\" id=\"piechart\" style=\"width: 100%;height: 500px;border-top:1px solid; border-top-style: solid;border-top: 10px solid black\"></div></center></body></div></center></html>")
  index.close()

}

class HtmlReportInZip {

  new HtmlReport()
  compress("target/test-reports/Test_Report.zip", new File("target/test-reports").listFiles.toList)

  def compress(zipFilepath: String, files: List[File]) {
    def readByte(bufferedReader: BufferedReader): Stream[Int] = {
      bufferedReader.read() #:: readByte(bufferedReader);
    }
    val zip = new ZipOutputStream(new FileOutputStream(zipFilepath));
    try {
      for (file <- files) {
        if (file.getName.endsWith(".html")) {
          zip.putNextEntry(new ZipEntry(file.getName));

          val in = Source.fromFile(file.getCanonicalPath).bufferedReader();
          try {
            readByte(in).takeWhile(_ > -1).toList.foreach(zip.write(_));
          } finally {
            in.close();
          }
          file.delete()
          zip.closeEntry();
        }

      }
    } finally {
      zip.close();
    }
  }
}