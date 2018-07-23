#include <SoftwareSerial.h>
SoftwareSerial BT_Serial(0,1);

int LEFT_MOTOR_DIGITAL = 8;
int LEFT_MOTOR_PWM = 9;
int RIGHT_MOTOR_DIGITAL = 4;
int RIGHT_MOTOR_PWM = 5;
int MIDLE_MOTOR_DIGITAL = 7;
int MIDLE_MOTOR_PWM = 6;

void setup() {
  // put your setup code here, to run once:
  BT_Serial.begin(9600);
  pinMode(LEFT_MOTOR_DIGITAL, OUTPUT);
  pinMode(LEFT_MOTOR_PWM, OUTPUT);
  pinMode(RIGHT_MOTOR_DIGITAL, OUTPUT);
  pinMode(RIGHT_MOTOR_PWM, OUTPUT);
  pinMode(MIDLE_MOTOR_DIGITAL, OUTPUT);
  pinMode(MIDLE_MOTOR_PWM, OUTPUT);
}

void loop() {
  if(BT_Serial.available()>0){
    String data = BT_Serial.readStringUntil(';');

    String motor =getValue(data, '-', 0);
    String s_power =getValue(data, '-', 1);
    String s_direction =getValue(data, '-', 2);

   setMotor(motor, s_power.toInt(), s_direction.toInt());
        
  }
}

void setMotor(String motor, short power, short voltage) {
    if(motor == "L"){
      setLeftMotor(power, voltage);
    }
    else if(motor == "R"){
      setRightMotor(power, voltage);
    }
    else if(motor == "M") {
      setMidleMotor(power, voltage);
    }
    else {
      setLeftMotor(0, HIGH);
      setRightMotor(0, HIGH);
      setMidleMotor(0, HIGH);
    }
}

void setLeftMotor(short power, short voltage){
  digitalWrite(LEFT_MOTOR_DIGITAL, voltage);  
  analogWrite(LEFT_MOTOR_PWM, power);
}
void setRightMotor(short power, short voltage){
  digitalWrite(RIGHT_MOTOR_DIGITAL, voltage);  
  analogWrite(RIGHT_MOTOR_PWM, power);
}
void setMidleMotor(short power, short voltage){
  digitalWrite(MIDLE_MOTOR_DIGITAL, voltage);  
  analogWrite(MIDLE_MOTOR_PWM, power);
}


String getValue(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = data.length()-1;

  for(int i=0; i<=maxIndex && found<=index; i++){
    if(data.charAt(i)==separator || i==maxIndex){
        found++;
        strIndex[0] = strIndex[1]+1;
        strIndex[1] = (i == maxIndex) ? i+1 : i;
    }
  }

  return found>index ? data.substring(strIndex[0], strIndex[1]) : "";
}


