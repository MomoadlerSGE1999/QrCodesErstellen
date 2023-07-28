import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.File
import java.io.FileReader
import java.io.FileOutputStream
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter


fun generateQRCodesFromCSV(inputFilePath: String, columnNumber: Int, outputDirectory: String) {
    val csvFile = File(inputFilePath)
    val fileReader = FileReader(csvFile)
    val csvParser = CSVParser(fileReader, CSVFormat.DEFAULT)
    val qrCodeWriter = QRCodeWriter()

    for (csvRecord in csvParser) {
        val cellValue = csvRecord.get(columnNumber - 1)
        val numericValue = cellValue.toLongOrNull()

        if (numericValue != null) {
            val bitMatrix = qrCodeWriter.encode(numericValue.toString(), BarcodeFormat.QR_CODE, 250, 250)
            val qrCodeFile = File("$outputDirectory/QRCode_$numericValue.png")
            qrCodeFile.createNewFile()
            FileOutputStream(qrCodeFile).use { qrCodeStream ->
                MatrixToImageWriter.writeToStream(bitMatrix, "PNG", qrCodeStream)
            }
        }
    }

    csvParser.close()
    fileReader.close()
}

fun main() {
    val inputFilePath = "C:\\Users\\Moritz\\Downloads\\Furhat (2).csv"
    val columnNumber = 1 // Annahme: Die erste Spalte enthält die Nummern (1-basiert)
    val outputDirectory = "C:\\data\\Test\\QR-Codes"

    generateQRCodesFromCSV(inputFilePath, columnNumber, outputDirectory)
}
