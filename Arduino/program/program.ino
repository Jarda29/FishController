#include <SoftwareSerial.h>
SoftwareSerial BT(0,1);

int LEFT_MOTOR_DIGITAL = 10;
int LEFT_MOTOR_PWM = 11;
int RIGHT_MOTOR_DIGITAL = 8;
int RIGHT_MOTOR_PWM = 9;
int MIDLE_MOTOR_DIGITAL = 7;
int MIDLE_MOTOR_PWM = 6;

void setup() {
  // put your setup code here, to run once:
  //Serial.begin(9600);
  BT.begin(9600);
  pinMode(LEFT_MOTOR_DIGITAL, OUTPUT);
  pinMode(LEFT_MOTOR_PWM, OUTPUT);
  pinMode(RIGHT_MOTOR_DIGITAL, OUTPUT);
  pinMode(RIGHT_MOTOR_PWM, OUTPUT);
  pinMode(MIDLE_MOTOR_DIGITAL, OUTPUT);
  pinMode(MIDLE_MOTOR_PWM, OUTPUT);
}

void loop() {
  byte data[2];
  if(BT.available()>0){
    //Serial.println("reading");
    data[0] = BT.read();
    delay(50);
    data[1] = BT.read();
    //Serial.println(data[0]);
    //Serial.println(data[1]);
    if(data[0] == 79 && data[1] == 75){
      int d = 0;
      for(int i = 0;i<5;i++){
        d = BT.read();
        delay(50);
      }
      setLeftMotor(0, LOW);
      setRightMotor(0, LOW);
      setMidleMotor(0, LOW);
      /*if(d == 78)
        Serial.println("CONNECTED");
      if(d == 84)
        Serial.println("DISCONNECTED");*/
      return;
    }
    
    int values[8];
    int values2[8];
    CrackByte(data[0], values);
    CrackByte(data[1], values2);
    /*Serial.println("val1");
    for(int i=0;i<8;i++)
      Serial.println(values[i]);

      Serial.println("val2");
    for(int i=0;i<8;i++)
      Serial.println(values2[i]);*/

   int leftPower, rightPower, midlePower = 0;
   int leftDir, rightDir, midleDir = 0;

   leftPower = values[0] * 1;
   leftPower += values[1] * 2;
   leftPower += values[2] * 4;
   leftDir = values[3];
   //Serial.println("leftP:");
   //Serial.println(leftPower);
   rightPower = values[4] * 1;
   rightPower += values[5] * 2;
   rightPower += values[6] * 4;
   rightDir = values[7];
   //Serial.println("rightP:");
   //Serial.println(rightDir);
   midlePower = values2[0] * 1;
   midlePower += values2[1] * 2;
   midlePower += values2[2] * 4;
   midleDir = values2[3];
   //Serial.println("midleP:");
   //Serial.println(midlePower);

   setLeftMotor(bitPowerToInt(leftPower), leftDir==0?LOW:HIGH);
   setRightMotor(bitPowerToInt(rightPower), rightDir==0?LOW:HIGH);
   setMidleMotor(bitPowerToInt(midlePower), midleDir==0?LOW:HIGH);
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

void CrackByte( byte b, int variable[8] )
{
 byte i;
 
 for ( i=0; i < 8; ++i )
 {
   variable[i] = b & 1;
   b = b >> 1;
 }
}

short bitPowerToInt(int arg){
  if(arg==0)
    return 0;
  if(arg==1)
    return 37;
  if(arg==2)
    return 72;
  if(arg==3)
    return 108;
  if(arg==4)
    return 144;
  if(arg==5)
    return 180;
  if(arg==6)
    return 216;
  if(arg==7)
    return 255;
}


