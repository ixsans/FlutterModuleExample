import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

const platform = MethodChannel('com.ixsans.flutermodule');

void main() {
  print("Flutter client running...");
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {

  @override
  void initState() {
    super.initState();
    _getData();
  }

  Future<void> _getData() async {
    platform.invokeMethod('setLoading');

    // Simulate async operation
    await Future.delayed(const Duration(seconds: 4));
    var data = "1234";

    platform.invokeMethod('setSuccess', { "data": data } );

    // Close the screen once the job finished
    SystemNavigator.pop();
  }
  
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        debugShowCheckedModeBanner: false,
        home: Container()
    );
  }
}
