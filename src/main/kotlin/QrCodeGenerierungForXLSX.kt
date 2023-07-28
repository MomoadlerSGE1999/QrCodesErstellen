import net.glxn.qrgen.QRCode
import net.glxn.qrgen.image.ImageType
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun generateQRCodesFromExcel(inputFilePath: String, sheetName: String, columnNumber: Int, outputDirectory: String) {
    val excelFile = File(inputFilePath)
    val workbook: Workbook
    val sheet: Sheet
    var fileInputStream: FileInputStream? = null

    try {
        fileInputStream = FileInputStream(excelFile)
        workbook = XSSFWorkbook(fileInputStream)
        sheet = workbook.getSheet(sheetName)

        // Get the column based on the provided columnNumber (assuming it's 0-indexed)
        val column: Int = columnNumber - 1

        // Iterate through each row in the column and create QR codes
        for (rowIndex in 0..sheet.lastRowNum) {
            val currentRow = sheet.getRow(rowIndex)
            val cell = currentRow?.getCell(column)

            // Skip if cell is empty
            if (cell == null || cell.cellType == CellType.BLANK) {
                continue
            }

            // Get the numeric value from the cell
            val numericValue = cell.numericCellValue.toLong()

            // Create QR code using QRGen library
            val qrCode = QRCode.from(numericValue.toString())
                .withSize(250, 250)
                .to(ImageType.PNG)
                .stream()

            // Save the QR code as PNG
            val qrCodeFile = File("$outputDirectory/QRCode_$numericValue.png")
            qrCodeFile.createNewFile()
            FileOutputStream(qrCodeFile).use { qrCodeStream ->
                qrCodeStream.write(qrCode.toByteArray())
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        fileInputStream?.close()
    }
}

fun main() {
    val inputFilePath = "C:\\Users\\Moritz\\Downloads\\Furhat (2).xlsx"
    val sheetName = "Furhat (2)"
    val columnNumber = 1 // Annahme: Die erste Spalte enthält die Nummern (1-basiert)
    val outputDirectory = "C:\\data\\Test\\QR-Codes"

    generateQRCodesFromExcel(inputFilePath, sheetName, columnNumber, outputDirectory)
}
