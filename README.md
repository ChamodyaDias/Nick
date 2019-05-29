# Nick
Nick is an arduino &amp; android powered robot. Enjoy!


# How Nick Works!
The brain of the robot is an Android App installed in the mobile. Why use a mobile phone? Well, other than adding 5 - 10 sensors and a Raspberry Pi, 
using a mobile phone is much more easy and straight forward. It is a compact device with very high flexibility. Hence we decided to go with the android app. 

We connected it with Dialog Flow and configured Dialog Flow agent with the predefined voice commands.The android app keeps listening to the voice commands and 
is activated by "Robot Listen / Nick Hello" etc. Then once we utter the command, the voice data are sent to Dialog Flow and processed in the cloud. 
After that, the command is resolved and the correct parameters are sent by the Cloud. The android app receives the results. The mobility of the robot is 
controlled by Arduino Mega board and a L298H Motor Driver. Arduino is paired with Android app via HC -05  Bluetooth Module. Once the results are processed by the 
Android app, the commands are sent to the Arduino via bluetooth to operate the robot.
 
      Example: To make the robot go forward, I would say; "Robot Listen"....
      
      Reply from Robot ; "I'm listening"...
      
      Me ; "Go forward"
      
This command is captured by the app and sent to Dialog Flow. The agent processes the command as "Go = command / function" + "Forward = direction".
Then it will send these parameters to the app. Currently a simple IF Else logic separates these commands and send a unique character via Bluetooth Serial, 
to Arduino. Arduino will read the serial and execute the command related to that character.

#Demo
Check this out!
https://youtu.be/wdCAbxWPeLI

# Like to "Do It Yourself"?
Well there are a lot of configuration and things to make. I'll help you. Just drop me a message! chamodyadias218@gmail.com | ndhpeiris@gmail.com

Nick is make of ;)
1) Android App - (a mobile phone with internet connection + front camera preferably)
2) Arduino Mega
3) HC 05 Bluetooth Module
4) L298 H Motor Driver
5) Dialog Flow project - (Google Cloud)
6) Firebase ML Kit - (For facial recognition)
7) A lot of Jumper Cables
8) Servo motors
9) HC-SR04 - Ultrasonic sensor - (For Obstacle Detection / Measure the distance between the person and the robot)

So its quite a lot of stuff and effort, but its definitely worth it! ;)

Give it a try!
