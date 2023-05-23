#include <Wire.h>
#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <Robojax_WCS.h>


#define MODEL 11 //see list above
#define SENSOR_PIN A3 //pin for reading sensor
#define SENSOR_VCC_PIN 2 //pin for powring up the sensor
#define ZERO_CURRENT_LED_PIN 13 //zero current LED pin

#define ZERO_CURRENT_WAIT_TIME 5000 //wait for 5 seconds to allow zero current measurement
#define CORRECTION_VLALUE 164 //mA
#define MEASUREMENT_ITERATION 100
#define VOLTAGE_REFERENCE  5000.0 //5000mv is for 5V
#define BIT_RESOLUTION 10
#define DEBUT_ONCE true

#define DEBUG_MODE 0
#define RXPIN 0
#define TXPIN 1
#define KEYPIN 13

unsigned long previousMillis = 0;
float ampHoursUsed = 0.0;

// Define the LCD connections
LiquidCrystal lcd(7, 8, 12, 11, 10, 9);

// Define the Bluetooth connection
SoftwareSerial bt(0, 1); // RX, TX

// Define the sensor pins
#define VOLTAGE_SENSOR A2
#define  thermistorMotorPin A0
#define thermistorBatteryPin A1
#define HALL_SENSOR_1 5
#define HALL_SENSOR_2 6
#define MOTOR_DRIVER_IN A4
#define MOTOR_DRIVER_SD 3
#define THROTTLE A5
#define BRAKE 4

// Define the wheel circumference and magnet counts
#define WHEEL_CIRCUMFERENCE 1.178
#define MOTOR_MAGNETS 3
#define WHEEL_MAGNETS 6

// Define the temperature thresholds
#define MOTOR_TEMP_THRESHOLD 110
#define BATTERY_TEMP_THRESHOLD 75

// Define the battery usage threshold
#define BATTERY_USAGE_THRESHOLD 30

// Define the read interval
#define READ_INTERVAL 250

// Define the thermistor parameters
#define THERMISTOR_RESISTANCE 10000
#define THERMISTOR_BETA 3950
#define THERMISTOR_ROOM_TEMP 298.15

// temp warnings
void checkTemperatureWarnings(float motorTemperature, float batteryTemperature) {
  if (motorTemperature > 110) {
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("* *MOTOR TEMP * *");
    lcd.setCursor(0, 1);
    lcd.print("HIGH!!!");
    delay(5000);
  }
  if (batteryTemperature > 75) {
    lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("* * BATT TEMP * *");
    lcd.setCursor(0, 1);
    lcd.print("HIGH!!!");
    delay(5000);
  }
}
Robojax_WCS sensor(
          MODEL, SENSOR_PIN, SENSOR_VCC_PIN, 
          ZERO_CURRENT_WAIT_TIME, ZERO_CURRENT_LED_PIN,
          CORRECTION_VLALUE, MEASUREMENT_ITERATION, VOLTAGE_REFERENCE,
          BIT_RESOLUTION, DEBUT_ONCE           
          );

void setup() {
  // Initialize the LCD
  lcd.begin(16, 2);

  // Initialize the Bluetooth
  bt.begin(9600);

  // Display the initial message
  lcd.print("GCU    F24    RN");
  lcd.setCursor(0, 1);
  lcd.print("GET READY! *****");
  delay(3000);

  // Set the sensor pins as inputs
  pinMode(VOLTAGE_SENSOR, INPUT);
  pinMode(thermistorMotorPin, INPUT);
  pinMode(thermistorBatteryPin, INPUT);
  pinMode(HALL_SENSOR_1, INPUT);
  pinMode(HALL_SENSOR_2, INPUT);
  pinMode(MOTOR_DRIVER_IN, OUTPUT);
  pinMode(MOTOR_DRIVER_SD, OUTPUT);
  pinMode(THROTTLE, INPUT);
  pinMode(BRAKE, INPUT);

  // Turn off the motor initially
  digitalWrite(MOTOR_DRIVER_SD, LOW);

   long temp = 0;
}

void loop() {
  // Read the sensor values
  int voltage = analogRead(VOLTAGE_SENSOR);
  int temp1 = analogRead(thermistorMotorPin);
  int temp2 = analogRead(thermistorBatteryPin);
  int throttle = analogRead(THROTTLE);
  int brake = digitalRead(BRAKE);

  // Calculate the battery percentage and voltage
  int batteryPercent = map(voltage, 0, 1023, 0, 100);
  float batteryVoltage = voltage * (24.0 / 1023.0);   // change to 24.0n for 24V

  // Calculate the temperatures
 // float motorTemp = calculateTemp(temp1);
 // float batteryTemp = calculateTemp(temp2);

  float batteryTemperature = getTemperature(thermistorBatteryPin);
  float motorTemperature = getTemperature(thermistorMotorPin);
  checkTemperatureWarnings(motorTemperature, batteryTemperature);

  // Calculate the speed
  float wheelRPM = getWheelRPM();
  float motorRPM = getMotorRPM();
  float speed = rpmToMph(wheelRPM);

 // Measure DC Current
sensor.readCurrent();//this must be inside loop
  sensor.printCurrent();
  {
    // does somethig here
    // lcd.clear();
    // lcd.setCursor(0, 0);
    // lcd.print("* * HIGH CURRENT * *");
    // lcd.setCursor(0, 1);
    // lcd.print("DETECTED!!!");
    // delay(5000);
  
  }
  delay(500);
  //does somethig when current is equal or greator than 12.3A
  if(sensor.getCurrent() >= 12.3)

  // Control the motor driver
  controlMotor(throttle, brake);

  // Display the battery status
  lcd.clear();
  lcd.print("Battery %: ");
  lcd.print(batteryPercent);
  lcd.setCursor(0, 1);
  lcd.print("Battery V: ");
  lcd.print(batteryVoltage);

  static bool firstReading = true;
   static unsigned long lastDisplaySwitch = 0;

  static bool displayAmpHours = false;

  // Check the battery usage
   if (millis() - lastDisplaySwitch >= (displayAmpHours ? 30000 : 300000)) {
   displayAmpHours = !displayAmpHours;
   lastDisplaySwitch = millis();
  }

 


  // Check the temperatures
 // if (motorTemp > MOTOR_TEMP_THRESHOLD) {
  //  lcd.clear();
  //  lcd.print("MOTOR TEMP");
  //  lcd.setCursor(0, 1);
  //  lcd.print("!!HIGH!!");
 // }
 // if (batteryTemp > BATTERY_TEMP_THRESHOLD) {
  //  lcd.clear();
   // lcd.print("BATTERY TEMP");
   // lcd.setCursor(0, 1);
   // lcd.print("!!HIGH!!");
  // }

  // Display the speed
  // lcd.clear();
  // lcd.print("Speed: ");
  // lcd.print(speed);
  // lcd.print(" MPH");

  // Send the data over Bluetooth
  sendBluetoothData(voltage, motorTemperature, batteryTemperature, wheelRPM, motorRPM);

  // Wait for the read interval
  delay(READ_INTERVAL);
}

float getTemperature(int thermistorPin) {
  float R = 10000.0; // 10k ohm
  float T0 = 298.15; // 25 degrees Celsius in Kelvin
  float B = 3950.0; // B value of the thermistor

  float reading = analogRead(thermistorPin);
  float resistance = R * (1023.0 / reading - 1.0);
  float temperature = 1.0 / (log(resistance / R) / B + 1.0 / T0) - 273.15;

  return temperature;
}


void controlMotor(int throttle, int brake) {
  // Control the motor driver based on the throttle and brake readings here
  // This will depend on the specific motor driver and throttle you're using
  if (brake == HIGH) {
    // Set the SD pin to LOW to turn off the motor
    digitalWrite(MOTOR_DRIVER_SD, LOW);
  } else {
    // Set the SD pin to HIGH to turn on the motor
    digitalWrite(MOTOR_DRIVER_SD, HIGH);
    // Set the IN pin based on the throttle
    int pwm = map(throttle, 0, 1023, 0, 255);
    analogWrite(MOTOR_DRIVER_IN, pwm);
  }
}

void sendBluetoothData(int voltage, int temp1, int temp2, float wheelRPM, float motorRPM) {
  // Send the data as integers
  bt.write((byte*)&voltage, 2);
  bt.write((byte*)&temp1, 2);
  bt.write((byte*)&temp2, 2);
  bt.write((byte*)&wheelRPM, 2);
  bt.write((byte*)&motorRPM, 2);

  // Send the data as graph form (integer and string)
  bt.print(String(voltage) + "," + String(temp1) + "," + String(temp2) + "," + String(wheelRPM) + "," + String(motorRPM));
}

float getWheelRPM() {
  static unsigned long lastTime = 0;
  unsigned long currentTime;
  float rpm;
  int hallValue = digitalRead(HALL_SENSOR_2);

  if (hallValue == HIGH) {
    currentTime = millis();
    rpm = (60000.0 * WHEEL_MAGNETS) / (currentTime - lastTime);
    lastTime = currentTime;
  }

  return rpm;
}

float getMotorRPM() {
  static unsigned long lastTime = 0;
  unsigned long currentTime;
  float rpm;
  int hallValue = digitalRead(HALL_SENSOR_1);

  if (hallValue == HIGH) {
    currentTime = millis();
    rpm = (60000.0 * MOTOR_MAGNETS) / (currentTime - lastTime);
    lastTime = currentTime;
  }

  return rpm;
}

float rpmToMph(float rpm) {
  float metersPerMinute = (rpm * WHEEL_CIRCUMFERENCE);
  float metersPerHour = metersPerMinute * 60;
  float milesPerHour = metersPerHour * 0.000621371;
  return milesPerHour;
}
