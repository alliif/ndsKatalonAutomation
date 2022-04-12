import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys

String popText = WebUI.getText(findTestObject('Popup PN/div_Informasi Login'))

if (popText.contains('Informasi Login')) {
    WebUI.click(findTestObject('Object Repository/Popup PN/span_OK'))
} else {
    WebUI.refresh()
}

WebUI.click(findTestObject('TarikTunai/btnFinansial'))

WebUI.delay(1)

WebUI.click(findTestObject('TarikTunai/tarikTunaiDev'))

WebUI.delay(1)

//WebUI.refresh()
//
//WebUI.delay(1)
TestData data = findTestData('TarikTunai')

for (int i = 1; i <= data.getRowNumbers(); i++) {
    WebUI.click(findTestObject('TarikTunai/inputNoRek'))

    WebUI.setText(findTestObject('TarikTunai/inputNoRek'), data.getValue('noRek', i))

    WebUI.delay(1)

    WebUI.sendKeys(findTestObject('TarikTunai/inputNoRek'), Keys.chord(Keys.ENTER))

    WebUI.delay(6)

    String noRekening = data.getValue('noRek', i)

    String hasilRek = noRekening.substring(12, 13)

    if (hasilRek == '5') {
        WebUI.callTestCase(findTestCase('05 - Tarik Tunai/rekSA'), null)
    } else if (hasilRek == '3') {
        WebUI.callTestCase(findTestCase('05 - Tarik Tunai/rekCA'), null)
    }
    
    WebUI.callTestCase(findTestCase('ApprovalSystem/ApprovalSPV'), null)

    WebUI.delay(3)

    String tapakValidasi = WebUI.getText(findTestObject('TarikTunai/TapakValidasi'))

    if (tapakValidasi.equalsIgnoreCase('Mencetak Tapak Validasi')) {
        WebUI.delay(1)

        WebUI.takeFullPageScreenshot()

        WebUI.delay(1)

        WebUI.click(findTestObject('TarikTunai/btnOK'))

        //Remote Print Agent
        WebUI.callTestCase(findTestCase('AgentRemote/printTapakValidasi'), null)

        if (hasilRek == '5') {
            String bukuTabungan = WebUI.getText(findTestObject('TarikTunai/BukuTabungan'))

            if (bukuTabungan.equalsIgnoreCase('Mencetak Buku Tabungan')) {
                WebUI.delay(1)

                WebUI.takeFullPageScreenshot()

                WebUI.delay(1)

                WebUI.click(findTestObject('TarikTunai/btnButab'))

                //Remote Print Agent
                WebUI.callTestCase(findTestCase('AgentRemote/printBukuTabungan'), null)

                WebUI.delay(2)
                    
				WebUI.callTestCase(findTestCase('Python/AATR'), [:], FailureHandling.STOP_ON_FAILURE)
                
            } else {
                WebUI.delay(2)

                WebUI.callTestCase(findTestCase('Python/AATR'), [:], FailureHandling.STOP_ON_FAILURE)
            }
        } else if (hasilRek == '3') {
            WebUI.delay(2)

            WebUI.takeFullPageScreenshot()

            WebUI.delay(5)

            WebUI.callTestCase(findTestCase('Python/AATR'), [:], FailureHandling.STOP_ON_FAILURE)
        }
    } else {
        WebUI.click(findTestObject('TarikTunai/btnOK'))
    }
}

