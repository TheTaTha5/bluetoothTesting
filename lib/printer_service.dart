import 'package:flutter/services.dart';

class PrinterService {
  static const platform = MethodChannel('speedy.bt.test');

  Future<void> printLabel(String printerName) async {
    try {
      final int result = await platform.invokeMethod('printLabel', {'printerName': printerName});
      print('Battery level: $result');
    } on PlatformException catch (e) {
      print("Failed to print label: '${e.message}'.");
    }
  }
}
