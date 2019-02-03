#include <NewPing.h>
#include <Servo.h>
#include <SoftwareSerial.h>

const byte rxPin = 10;
const byte txPin = 11;
SoftwareSerial mySerial (rxPin, txPin);
int enB = 3;
int in3 = 53;
int in4 = 52;
int enA = 2;
int in1 = 50;
int in2 = 51;
int xAxis, yAxis;
int  x = 0;
int  y = 0;
int motorSpeedA = 0;
int motorSpeedB = 0;
char command = 'S';
unsigned long timer0;  //Stores the time (in millis since execution started)
unsigned long timer1 = 0;  //Stores the time when the last command was received from the phone

int lazer = 49;

#define echo_pin 8
#define trigger_pin 9
#define max_cm_distance 100

boolean noObj = true;

NewPing sonar(trigger_pin, echo_pin, max_cm_distance);

String data;
int prePanVal = 108;
int pretTiltVal = 109;
int panVal = 120;
int tiltVal = 90;

Servo pan;
Servo tilt;
void setup() {
  // set up a new serial object
  pinMode(enA, OUTPUT);
  pinMode(enB, OUTPUT);
  pinMode(in1, OUTPUT);
  pinMode(in2, OUTPUT);
  pinMode(in3, OUTPUT);
  pinMode(in4, OUTPUT);
  pinMode(lazer, OUTPUT);
  mySerial.begin(38400); // Default communication rate of the Bluetooth module
  Serial.begin(38400);
  pan.attach(7);
  tilt.attach(6);
  //  pan.write(0);
  //  tilt.write(0);
  pan.write(108);
  delay(50);
  tilt.write(109);
  delay(50);
  //  Serial.println("Hello");
}

boolean noObstacle() {
  delay(50);
  pan.write(108);
  delay(50);
  tilt.write(80);
  delay(50);
  int distanceToObj = 0;
  distanceToObj = sonar.ping_cm();

  if (distanceToObj != 0 and distanceToObj < 80) {
    Serial.println(distanceToObj);
    noObj = false;
    return noObj;
  } else if (distanceToObj == 0) {
    Serial.println("No obstacle");
    noObj = true;
    return noObj;
  } else {
    Serial.println("No obstacle");
    noObj = true;
    return noObj;
  }
}


void moveForward() {
  Serial.println("Came to forward");

  while (noObstacle()) {
    Serial.println("moving");
    // Set Motor A forward
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    motorSpeedA = 220;
    analogWrite(enA, motorSpeedA); // Send PWM signal to motor Move forward
  }
  stopMoving();
}

void moveForwardTrack() {
  Serial.println("Came to forward");
  timer0 = millis();  //Get the current time (millis since execution started).
  timer1 = millis();
  //  Check if it has been 500ms since we received last command.
  while ((timer1 - timer0) < 800) {
    Serial.println("moving");
    // Set Motor A forward
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    motorSpeedA = 180;
    analogWrite(enA, motorSpeedA); // Send PWM signal to motor Move forward
    timer1 = millis();
  }
  stopMoving();
}

void moveBackward() {
  Serial.println("Came to backward");
  timer0 = millis();  //Get the current time (millis since execution started).
  timer1 = millis();
  //Check if it has been 500ms since we received last command.
  while ((timer1 - timer0) < 2000) {
    Serial.println("Came to interval");
    digitalWrite(in1, LOW);
    digitalWrite(in2, HIGH);
    motorSpeedA = 220;
    analogWrite(enA, motorSpeedA); // Send PWM signal to motor Move
    timer1 = millis();
  }
  // Set Motor A forward
  Serial.println("Came to else");
  //    digitalWrite(in1, LOW);
  //    digitalWrite(in2, HIGH);
  //    motorSpeedA = 220;
  //    analogWrite(enA, motorSpeedA); // Send PWM signal to motor Move
  stopMoving();
}

void moveLeft() {
  Serial.println("Came to left");
  timer0 = millis();  //Get the current time (millis since execution started).
  timer1 = millis();
  while ((timer1 - timer0) < 2000) {
    // Set Motor A forward
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    // Set Motor B forward
    digitalWrite(in3, HIGH);
    digitalWrite(in4, LOW);
    motorSpeedA = 150;
    motorSpeedB = 255;
    int i = 150;
    for (i = 150; i < 255; i++) {
      motorSpeedA = i;
      analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
    }
    analogWrite(enB, motorSpeedB); // Send PWM signal to motor B
    timer1 = millis();
  }
  stopMoving();
  //analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
}

void moveLeftTrack() {
  Serial.println("Came to left");
  timer0 = millis();  //Get the current time (millis since execution started).
  timer1 = millis();
  // Set Motor B forward
  digitalWrite(in3, HIGH);
  digitalWrite(in4, LOW);
  analogWrite(enB, motorSpeedB); // Send PWM signal to motor B
  motorSpeedB = 255;
  while ((timer1 - timer0) < 500) {
    // Set Motor A forward
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    motorSpeedA = 150;
    int i = 150;
    for (i = 150; i < 255; i++) {
      motorSpeedA = i;
      analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
    }

    timer1 = millis();
  }
  stopMoving();
  //analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
}

void moveRight() {
  Serial.println("Came to RIght");
  timer0 = millis();  //Get the current time (millis since execution started).
  timer1 = millis();
  while ((timer1 - timer0) < 2000) {
    // Set Motor A forward
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    // Set Motor B forward
    digitalWrite(in3, LOW);
    digitalWrite(in4, HIGH);
    motorSpeedA = 150;
    motorSpeedB = 255;
    int i = 150;
    for (i = 150; i < 255; i++) {
      motorSpeedA = i;
      analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
    }
    analogWrite(enB, motorSpeedB); // Send PWM signal to motor B
    timer1 = millis();
    //analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
  }
  stopMoving();

}
void moveRightTrack() {
  Serial.println("Came to RIght");
  timer0 = millis();  //Get the current time (millis since execution started).
  timer1 = millis();
      // Set Motor B forward
    digitalWrite(in3, LOW);
    digitalWrite(in4, HIGH);
        motorSpeedB = 255;
            analogWrite(enB, motorSpeedB); // Send PWM signal to motor B
  while ((timer1 - timer0) < 100) {
    // Set Motor A forward
    digitalWrite(in1, HIGH);
    digitalWrite(in2, LOW);
    motorSpeedA = 150;
    int i = 150;
    for (i = 150; i < 255; i++) {
      motorSpeedA = i;
      analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
    }
    timer1 = millis();
    //analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
  }
  stopMoving();

}
void stopMoving() {
  motorSpeedA = 0;
  motorSpeedB = 0;
  analogWrite(enA, motorSpeedA); // Send PWM signal to motor A
  analogWrite(enB, motorSpeedB); // Send PWM signal to motor B
}

void trackFace(String x, String y) {
  digitalWrite(lazer, HIGH);
  timer0 = millis();
  if (timer0 - timer1 > 2000) {
    int valX = x.toInt();
    int valY = y.toInt();
    //  int panVal = map(valX, 100, 1920, 10, 170);
    //  int tiltVal = map(valY, 0, 1080, 90, 120);

    if (valX >= prePanVal) { //Write the Pan value
      for (prePanVal = prePanVal; prePanVal <= valX; prePanVal += 1) {
        pan.write(valX);
        delay(50);
      }
      if (valY >= pretTiltVal) { // write the tilt value
        for (pretTiltVal = pretTiltVal; pretTiltVal <= valY; pretTiltVal += 1) {
          tilt.write(valY);
          delay(50);
        }
      } else if (valY < pretTiltVal) {
        for (pretTiltVal = pretTiltVal; pretTiltVal >= valY; pretTiltVal -= 1) {
          tilt.write(valY);
          delay(50);
        }
      }

    } else if (valX < prePanVal) {
      for (prePanVal = prePanVal; prePanVal >= valX; prePanVal -= 1) {
        pan.write(valX);
        delay(50);
      }
      if (valY >= pretTiltVal) { // write the tilt value
        for (pretTiltVal = pretTiltVal; pretTiltVal <= valY; pretTiltVal += 1) {
          tilt.write(valY);
          delay(50);
        }
      } else if (valY < pretTiltVal) {
        for (pretTiltVal = pretTiltVal; pretTiltVal >= valY; pretTiltVal -= 1) {
          tilt.write(valY);
          delay(50);
        }
      }
    }
        pan.write(valX);
        delay(50);
        tilt.write(valY);
        delay(50);
    //    Serial.print("Pan = ");
    //    Serial.println(valX);
    //    Serial.print("Tilt = ");
    //    Serial.println(valY);
    digitalWrite(lazer, HIGH);
    int distanceToPerson = sonar.ping_cm();
    Serial.println(distanceToPerson);
    if (distanceToPerson == 0 or distanceToPerson > 80) {
      moveForwardTrack();
    }
    //    stopMoving();
    timer1 = timer0;
    digitalWrite(lazer, LOW);
    return;
  } else {
    //    Serial.println("came to else");
  }

}



void loop() {
  Serial.println("Came to loop");
  // Read the incoming data from the Smartphone Android App
  digitalWrite(rxPin, HIGH);
  digitalWrite(txPin, HIGH);
  mySerial.listen();
  mySerial.flush();



  if (mySerial.isListening()) {
    //Serial.println("Port One is listening!");
    //   if (mySerial.available() >= 2) {
    //    timer1 = millis();
    while (mySerial.available() < 2) {
      stopMoving();
    };
    command = mySerial.read();
    //Serial.println(command);

    switch (command) {

      case 'Z': {
          if (mySerial.available() > 0) {
            data = mySerial.readStringUntil('>');
          }
          if (data != "") {
            //          Serial.println(data);
          }

          int offset = data.indexOf("<");
          if (offset >= 0) {
            String faceX = data.substring(offset + 1, data.indexOf(','));
            //          Serial.println(faceX);
            String faceY = data.substring(data.indexOf(',') + 1);
            //          Serial.println(faceY);
            trackFace(faceX, faceY);
            //          preFaceX = faceX;
            //          preFaceY = faceY;
            //          Serial.println("trackface called");

          }

          break;
        }
      case 'F':
        moveForward();
        break;
      case 'B':
        moveBackward();
        break;
      case 'L':
        moveLeft();
        break;
      case 'R':
        moveRight();
        break;
      case 'P':
        moveLeftTrack();
        break;
      case 'Q':
        moveRightTrack();
        break;
        //      case 'S':
        //        yellowCar.Stopped_4W();
        //        break;
        //      case 'I':  //FR
        //        yellowCar.ForwardRight_4W(velocity);
        //        break;
        //      case 'J':  //BR
        //        yellowCar.BackRight_4W(velocity);
        //        break;
        //      case 'G':  //FL
        //        yellowCar.ForwardLeft_4W(velocity);
        //        break;
        //      case 'H':  //BL
        //        yellowCar.BackLeft_4W(velocity);
        //        break;

    }
    //      }  else {
    //        timer0 = millis();  //Get the current time (millis since execution started).
    //        //Check if it has been 500ms since we received last command.
    //        if ((timer0 - timer1) > 5000) {
    //          //More tan 1000ms have passed since last command received, car is out of range.
    //          //Therefore stop the car and turn lights off.
    //          stopMoving();
    //        }
    //      }
  } else {
    //Serial.println("Port One is not listening!");
  }

}

