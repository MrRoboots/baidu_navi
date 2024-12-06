import 'package:flutter/material.dart';

class InputPhoneCode extends StatefulWidget {
  const InputPhoneCode({super.key});

  @override
  State<InputPhoneCode> createState() => _InputPhoneCodeState();
}

class _InputPhoneCodeState extends State<InputPhoneCode> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: TextField(
          decoration: const InputDecoration(
            labelText: '输入内容',
            border: OutlineInputBorder(),
          ),
          onChanged: (value) {
            // 处理输入变化
          },
        ),
      ),
    );
  }
}
