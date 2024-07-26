import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.ParcelFileDescriptor
import androidx.annotation.NonNull
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import com.example.tscdll.TSCActivity
import java.io.File
import java.io.FileNotFoundException

class MainActivity: FlutterActivity() {
 private val channel = "speedy.app/printer"
 override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
   super.configureFlutterEngine(flutterEngine)
   MethodChannel(
     flutterEngine.dartExecutor.binaryMessenger,
     channel
   ).setMethodCallHandler { call, result ->
     if (call.method == "printLabel") {
       val printerName: String? = call.argument("printerName")
       if (printerName != null) {
         printLabel(printerName)
       } else {
         result.error("INVALID_ARGUMENT", "Printer name is required.", null)
       }
     } else {
       result.notImplemented()
     }
   }
 }

 private val filePath = "libs/testpdf/CR_AllSpeedy.pdf"
 val parcelFileDescriptor = openFileDescriptor(filePath)
 private fun printLabel(printerName:String) {

   val tscPrinterName :TSCActivity = TSCActivity()
   tscPrinterName.openport_onpair_btname(printerName)
   tscPrinterName.setup(70, 130, 4, 12, 0, 0, 0)
   tscPrinterName.clearbuffer()
   tscPrinterName.printPDFbyFile(File(filePath), 10, 20, 170, 1)
   tscPrinterName.printlabel(1,2)
   tscPrinterName.closeport()
 }

 fun openFileDescriptor(filePath: String): ParcelFileDescriptor? {
   return try {
     val file = File(filePath)
     ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
   } catch (e: FileNotFoundException) {
     e.printStackTrace()
     null
   }
 }
}