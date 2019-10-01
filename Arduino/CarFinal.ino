#include<SoftwareSerial.h>
SoftwareSerial BT(10,11);
String readData;

//MOTORS Config
int LEFT_MOTOR_FORWARD = 8;
int LEFT_MOTOR_BACKWARD = 7;
int RIGHT_MOTOR_FORWARD = 13;
int RIGHT_MOTOR_BACKWARD = 12;

// Ultrasonic Config
int TrigPin = 6;
int EchoPin = 5;
long duration;
int distance;


void setup() {
  // Ultrasonic Initialize
  pinMode(TrigPin,OUTPUT);
  pinMode(EchoPin,INPUT);
  
  // Bluetooth Initialize
  BT.begin(9600);
  Serial.begin(9600);
  
  // MOTORS Initialize
  pinMode(RIGHT_MOTOR_FORWARD,OUTPUT);
  pinMode(RIGHT_MOTOR_BACKWARD,OUTPUT);
  pinMode(LEFT_MOTOR_FORWARD,OUTPUT);
  pinMode(LEFT_MOTOR_BACKWARD,OUTPUT);
}

//MOTOR FUNCTIONS

void forward(){
  digitalWrite(RIGHT_MOTOR_FORWARD,HIGH);
  digitalWrite(LEFT_MOTOR_FORWARD,HIGH);
}
void backward(){
  digitalWrite(RIGHT_MOTOR_BACKWARD,HIGH);
  digitalWrite(LEFT_MOTOR_BACKWARD,HIGH);
}
void right_forward(){
  digitalWrite(LEFT_MOTOR_FORWARD,HIGH);
  digitalWrite(RIGHT_MOTOR_BACKWARD,HIGH);
}
void right_backward(){
  digitalWrite(LEFT_MOTOR_BACKWARD,HIGH);
}
void left_forward(){
  digitalWrite(RIGHT_MOTOR_FORWARD,HIGH);
  digitalWrite(LEFT_MOTOR_BACKWARD,HIGH);
}
void left_backward(){
  digitalWrite(RIGHT_MOTOR_BACKWARD,HIGH);
}

void resetAll(){
  digitalWrite(RIGHT_MOTOR_FORWARD,LOW);
  digitalWrite(LEFT_MOTOR_FORWARD,LOW);
  digitalWrite(RIGHT_MOTOR_BACKWARD,LOW);
  digitalWrite(LEFT_MOTOR_BACKWARD,LOW);
}

int count(String s, char c) 
{ 
    // Count variable 
    int res = 0;
    for (int i=0;i<s.length();i++) 
        // checking character in string 
        if (s[i] == c) 
            res++; 
    return res; 
} 

int analysisSize = 62;
int Distances[62];
int maxim,index;

void printCustom(String s){
  BT.println(s);
  Serial.println(s);
}

void printWithoutLineCustom(String s){
  BT.print(s);
  Serial.print(s);
}

boolean selfDrive = false;

// MAIN LOOP
void loop() {
    while(BT.available()){
  //        delay(10);
          char c = BT.read();
          readData += c;
    }
  if(!selfDrive){
    if(readData.length() > 0){
      printCustom(readData);
      if(readData.indexOf("a") >= 0){
        for (int i=0;i<count(readData,'a');i++){
          forward();
          delay(50);
        }
      }else if(readData.indexOf("b") >= 0){
        for (int i=0;i<count(readData,'b');i++){
          backward();
          delay(50);
        }
      }else if(readData.indexOf("c") >= 0){
        for (int i=0;i<count(readData,'c');i++){
          right_forward();
          delay(50);
        }
      }else if(readData.indexOf("d") >= 0){
        for (int i=0;i<count(readData,'d');i++){
          left_forward();
          delay(50);
        }
      }else if(readData.indexOf("e") >= 0){
        for (int i=0;i<count(readData,'e');i++){
          right_backward();
          delay(50);
        }
      }else if(readData.indexOf("f") >= 0){
        for (int i=0;i<count(readData,'f');i++){
          left_backward();
          delay(50);
        }
      }else if(readData == "g"){
        for(int i=0;i<analysisSize;i++){
          right_forward();
          delay(30);
                
          digitalWrite(TrigPin,LOW);
          delayMicroseconds(2);
        
          digitalWrite(TrigPin,HIGH);
          delayMicroseconds(10);
          digitalWrite(TrigPin,LOW);
        
          duration = pulseIn(EchoPin,HIGH);
          distance = duration * 0.034/2;
          Distances[i] = distance;
        }
        // Print Distances
        int i;
        maxim = Distances[0];
        index = 0;
        for (i = 0; i < analysisSize; i++) {
          if(Distances[i] > maxim && Distances[i] < 400){
            maxim = Distances[i];
            index = i;
          }
          printWithoutLineCustom((String)Distances[i]+",");
        }
        printCustom((String)"\nMaximum: "+maxim);
        printCustom((String)"MaximumIndex: "+index);
        printCustom((String)"finishanalysis "+(360/analysisSize*index));
      }else if(readData.indexOf("h") >= 0){
        for(int i=analysisSize;i>=index-10;i--){
          left_forward();
          delay(50);
        }
  //      resetAll();
  //      delay(500);
  //      for(int i=0;i<(Distances[index]);i++){
  //        forward();
  //        delay(50);
  //      }
      }else if(readData.indexOf("i") >= 0){ 
        selfDrive = true;
      }
      resetAll();
      readData = "";
    }
  }else{
    if(readData.length() > 0){
      printCustom((String)"SelfDrive "+readData);
      if(readData.indexOf("j") >= 0){
        printCustom((String)"SelfDrive Stop");
        selfDrive = false;
      }
    }
    
    digitalWrite(TrigPin,LOW);
    delayMicroseconds(2);
  
    digitalWrite(TrigPin,HIGH);
    delayMicroseconds(10);
    digitalWrite(TrigPin,LOW);
  
    duration = pulseIn(EchoPin,HIGH);
    distance = duration * 0.034/2;
    if (distance >= 20)  {
      digitalWrite(RIGHT_MOTOR_FORWARD,HIGH);
      digitalWrite(LEFT_MOTOR_FORWARD,HIGH);
      digitalWrite(RIGHT_MOTOR_BACKWARD,LOW);
      digitalWrite(LEFT_MOTOR_BACKWARD,LOW);
    }else{
      digitalWrite(LEFT_MOTOR_FORWARD,HIGH);
      digitalWrite(RIGHT_MOTOR_BACKWARD,HIGH);
      digitalWrite(RIGHT_MOTOR_FORWARD,LOW);
      digitalWrite(LEFT_MOTOR_BACKWARD,LOW);
      delay(600);
    }
  }
}
