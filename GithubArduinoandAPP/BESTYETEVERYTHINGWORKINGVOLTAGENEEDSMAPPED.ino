#include <LiquidCrystal.h>
#include <SoftwareSerial.h>
#include <Robojax_WCS.h>
#include <WinsonLib.h>

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

// Added motor driver board pins
const int motorInPin = A4;       // IR2104 IN pin
const int motorSdPin = 3;        // IR2104 SD pin
const int electricBrakePin = 4;
const int throttlePin = A5;

const int motorSpeed = 0; // Set motor speed (0-100)

const int voltageSensorPin = A2;
//
// const int AMPS_IN_PIN = A3;


const int thermistorMotorPin = A0;
const int thermistorBatteryPin = A1;
const int hallEffect1Pin = 5;
const int hallEffect2Pin = 6;
const float wheelCircumference = 1.178;
const int numMotorMagnets = 3;
const int numWheelMagnets = 6;
const unsigned long debounceDelay = 50;
unsigned long lastDebounceTime;

 const float CAL_REFERENCE_VOLTAGE   = 5;     // Voltage seen on the arduino 5V rail
 const float CAL_CURRENT             = 37.55; // Current Multiplier - See documentation for calibration method
unsigned long previousMillis = 0;
float ampHoursUsed = 0.0;


/* CURRENT */
 const int AMPSENSOR_CAL_DELAY  = 3000;    // calibration delay for current sensor (ms)

LiquidCrystal lcd(7, 8, 12, 11, 10, 9);
 SoftwareSerial btSerial(0, 1); // RX, TX

 const char CURRENT_ID          = 'i';
 int             currentSensorOffset         = 0;    //offset value for the current sensor
 float current               = 0;

//Current Smoothing Variables:

 const int currentSmoothingSetting = 4; //current is sampled every 250ms, therefore 4 makes 1s of smoothing
 int currentSmoothingArray[currentSmoothingSetting];
 int currentSmoothingCount = 0;

//Speed Smoothing Variables:

const int speedSmoothingSetting = 3; //speed is sampled every 1s, therefore 3 makes 3 seconds of smoothing
int speedSmoothingArray[speedSmoothingSetting];
int speedSmoothingCount = 0;



void setMotorSpeed(int speed);
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
 
pinMode(KEYPIN, OUTPUT);
  digitalWrite(KEYPIN, LOW);
  delay(50);
  pinMode(motorInPin, OUTPUT);
  pinMode(motorSdPin, OUTPUT);
  pinMode(electricBrakePin, OUTPUT);
  pinMode(hallEffect1Pin, INPUT);
  pinMode(hallEffect2Pin, INPUT);
 //  pinMode(AMPS_IN_PIN, INPUT); 

  Serial.begin(9600);

 btSerial.begin(38400); // Initialize the HC-05 Bluetooth communication at 38400 baud rate
 delay(50);
  
  digitalWrite(KEYPIN, HIGH);
  delay(50);

  

  // Initially off function for motor
  digitalWrite(motorInPin, LOW);
  digitalWrite(motorSdPin, LOW);

  lcd.begin(16, 2);
  lcd.setCursor(0, 0);
  lcd.print("GET READY! *****");
  lcd.setCursor(0, 1);
  lcd.print("GCU F24");
  delay(5000);
  lcd.clear();

  long temp = 0;
 // int nReadings = AMPSENSOR_CAL_DELAY / 100; //Ten Readings Per Second
 // for (int i = 0; i < nReadings; i++)
 // {
 //   temp += analogRead(AMPS_IN_PIN);
 //   delay(AMPSENSOR_CAL_DELAY / nReadings);
 // }
 // currentSensorOffset = (float) temp / nReadings; 

}


void loop() {
  
 // current = readCurrent(); 
//  sendData(CURRENT_ID, current);
//float current = getCurrent();
//  updateAmpHours(current);
    // Measure DC Current
sensor.readCurrent();//this must be inside loop
  sensor.printCurrent();

  //does somethig when current is equal or greator than 12.3A
  if(sensor.getCurrent() >= 12.3)
  {
    // does somethig here
     lcd.clear();
    lcd.setCursor(0, 0);
    lcd.print("* * HIGH CURRENT * *");
    lcd.setCursor(0, 1);
    lcd.print("DETECTED!!!");
    delay(5000);
  
  }
  delay(500);
	//sensor.printDebug();


  float batteryTemperature = getTemperature(thermistorBatteryPin);
  float motorTemperature = getTemperature(thermistorMotorPin);
  checkTemperatureWarnings(motorTemperature, batteryTemperature);
  float batteryVoltage = getVoltage();
  float wheelRPM = getWheelRPM();
  float motorRPM = getMotorRPM();
  float motorMPH = rpmToMph(motorRPM);

  static bool firstReading = true;
   static unsigned long lastDisplaySwitch = 0;

  static bool displayAmpHours = false;

  if (millis() - lastDisplaySwitch >= (displayAmpHours ? 30000 : 300000)) {
   displayAmpHours = !displayAmpHours;
   lastDisplaySwitch = millis();
  }

  if (ampHoursUsed >= 30.0) {
    displayChargeSoon();
    delay(5000);
    ampHoursUsed = 0; // Reset the ampHoursUsed counter
  } else if (displayAmpHours) {
    displayAmpHoursUsed();
  } 

  if (firstReading) {
    motorMPH = 0;
    
    firstReading = false;
  } else {
    motorMPH = getMotorRPM();
  
  }
  

 if (digitalRead(hallEffect1Pin) == HIGH || digitalRead(hallEffect2Pin) == HIGH) {
    motorRPM = getMotorRPM();
  } else {
    motorRPM = 0;
  }

  

// ** Change to 27.5 if cannot ZERO

  if (motorMPH < 5) {
    motorMPH = 0;
  }
 

  lcd.setCursor(0, 0);
  lcd.print("Battery:   ");
  lcd.print(batteryVoltage);
  lcd.print("%");
  lcd.setCursor(0, 1);
  if (motorMPH > 2) {
      lcd.print("Speed:       MPH");
      lcd.print(motorMPH, 1);
    } else {
      lcd.print("Speed:  0    MPH");
    }

  sendData('B', batteryTemperature);
  sendData('M', motorTemperature);
  sendData('V', batteryVoltage);
  sendData('W', wheelRPM);
  sendData('R', motorRPM);

int throttleValue = analogRead(throttlePin);
  int motorSpeed = map(throttleValue, 0, 1023, 0, 100);
  setMotorSpeed(motorSpeed);

  
  delay(250);

  setMotorSpeed(motorSpeed);
  
  /*Serial.print("motorTemperature : ");   // dont uncomment while sending data to bluetooth
  Serial.println(motorTemperature);
  Serial.print("batteryTemperature : ");
  Serial.println(batteryTemperature);
  Serial.print("motorRPM : ");
  Serial.println(motorRPM);
  Serial.print("wheelRPM : ");
  Serial.println(wheelRPM);
  Serial.print("batteryVoltage : ");
  Serial.println(batteryVoltage);
  Serial.print("current : ");
  Serial.println(current);
  Serial.println("------------------");*/

  String my_String = "{'1':'"+String(motorTemperature)+"','2':'"+String(batteryTemperature)+"','3':'"+String(motorRPM)+"','4':'"+String(wheelRPM)+"','5':'"+String(batteryVoltage)+"','6':'"+String(current)+"'}";
  Serial.print(my_String);          
  my_String = "";
  
  
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

float getVoltage() {
  float voltage = analogRead(voltageSensorPin) * (5.0 / 1023.0);
  float inputVoltage = (voltage / 5.0) * 24.0; // Use floating-point arithmetic for better precision
  float batteryPercentage = ((inputVoltage - 23.2) / (27.6 - 23.2)) * 100.0; // Calculate the percentage using floating-point arithmetic
  batteryPercentage = constrain(batteryPercentage, 0, 100); // Constrain the percentage to the 0-100 range
  return batteryPercentage;
}

float getWheelRPM() {
static unsigned long lastTime = 0;
unsigned long currentTime;
float rpm;
int hallValue = digitalRead(hallEffect2Pin);

if (hallValue == HIGH) {
currentTime = millis();
rpm = (60000.0 * numWheelMagnets) / (currentTime - lastTime);
lastTime = currentTime;
}

return rpm;
}


float getMotorRPM() {
static unsigned long lastTime = 0;
unsigned long currentTime;
float rpm;
int hallValue = digitalRead(hallEffect1Pin);

if (hallValue == HIGH) {
currentTime = millis();
rpm = (60000.0 * numMotorMagnets) / (currentTime - lastTime);
lastTime = currentTime;
}

return rpm;
}

// float readCurrent()
// {
//  float tempCurrent = analogRead(AMPS_IN_PIN);
//
//  tempCurrent = (tempCurrent / 1024) * CAL_REFERENCE_VOLTAGE; //gives voltage output of current sensor.

 // tempCurrent = tempCurrent * CAL_CURRENT; //calibration value for LEM current sensor on eChook board.

//  currentSmoothingArray[currentSmoothingCount] = tempCurrent; //updates array with latest value

 // The next 5 lines manage the smoothing count for the averaging:

//  currentSmoothingCount ++; //increment smoothing count

//  if (currentSmoothingCount >= currentSmoothingSetting)
//  {
//    currentSmoothingCount = 0; // if current smoothing count is higher than max, reset to 0
//  }

 // Now back to the current calculations:

//  tempCurrent = 0; //reset temp current to receive sum of array values

//  for (int i = 0; i < currentSmoothingSetting; i++)
//  {
//    tempCurrent += currentSmoothingArray[i]; //sum all values in the current smoothing array
//  }

//  tempCurrent = tempCurrent / currentSmoothingSetting; //divide summed value by number of samples to get mean

//  return (tempCurrent); //return the final smoothed value
// }




// float getCurrent() {
//  int rawValue = analogRead(A3);
//  float voltage = rawValue * (5.0 / 1023.0);
//  float current = (voltage - 2.5) / 0.066; // WCS1700 sensitivity is 66mV/A
//  return current;
// }

 //void updateAmpHours(float current) {
 // unsigned long currentMillis = millis();
 // float timeDeltaHours = (currentMillis - previousMillis) / 3600000.0;
 // ampHoursUsed += current * timeDeltaHours;
 // previousMillis = currentMillis;
// }

 void displayAmpHoursUsed() {
  lcd.clear();
 lcd.setCursor(0, 0);
 lcd.print("Amp Hours Used:");
 lcd.setCursor(0, 1);
 lcd.print(ampHoursUsed, 2);
 lcd.print(" Ah");
 }

 void displayChargeSoon() {
  lcd.clear();
  lcd.setCursor(0, 0);
  lcd.print("CHARGE SOON");
  lcd.setCursor(0, 1);
  lcd.print("30Ah USED");
 }


void sendData(char identifier, float value) {
 if (!DEBUG_MODE) // Only runs if debug mode is LOW (0)
  {
    byte dataByte1;
    byte dataByte2;

    if (value == 0)
    {
      // It is impossible to send null bytes over SerialA connection
      // so instead we define zero as 0xFF or 11111111 i.e. 255
      dataByte1 = 0xFF;
      dataByte2 = 0xFF;

    }
    else if (value <= 127)
    {
      // Values under 128 are sent as a float
      // i.e. value = dataByte1 + dataByte2 / 100
      int integer;
      int decimal;
      float tempDecimal;

      integer = (int) value;
      tempDecimal = (value - (float) integer) * 100;
      decimal = (int) tempDecimal;

      dataByte1 = (byte) integer;
      dataByte2 = (byte) decimal;

      if (decimal == 0)
      {
        dataByte2 = 0xFF;
      }

      if (integer == 0)
      {
        dataByte1 = 0xFF;
      }

    }
    else
    {
      // Values above 127 are sent as integer
      // i.e. value = dataByte1 * 100 + dataByte2
      int tens;
      int hundreds;

      hundreds = (int)(value / 100);
      tens = value - hundreds * 100;

      dataByte1 = (byte)hundreds;
      //dataByte1 = dataByte1 || 0x10000000; //flag for integer send value
      dataByte1 += 128;
      dataByte2 = (byte) tens;

      if (tens == 0)
      {
        dataByte2 = 0xFF;
      }

      if (hundreds == 0)
      {
        dataByte1 = 0xFF;
      }
    }

    // Send the data in the format { [id] [1] [2] }
    /*SerialA.write(123);
    SerialA.write(identifier);
    SerialA.write(dataByte1);
    SerialA.write(dataByte2);
    SerialA.write(125);*/

  }

}

/** override for integer values*/
void sendData(char identifier, int value)
{
  if (!DEBUG_MODE)
  {
    byte dataByte1;
    byte dataByte2;

    if (value == 0)
    {
      dataByte1 = 0xFF;
      dataByte2 = 0xFF;

    }
    else if (value <= 127)
    {

      dataByte1 = (byte)value;
      dataByte2 = 0xFF; //we know there's no decimal component as an int was passed in
    }
    else
    {
      int tens;
      int hundreds;

      hundreds = (int)(value / 100);
      tens = value - hundreds * 100;

      dataByte1 = (byte)hundreds;
      dataByte1 += 128;   //sets MSB High to indicate Integer value
      dataByte2 = (byte) tens;

      if (tens == 0)
      {
        dataByte2 = 0xFF;
      }

      if (hundreds == 0)
      {
        dataByte1 = 0xFF;
      }
    }

    /*SerialA.write(123);
    SerialA.write(identifier);
    SerialA.write(dataByte1);
    SerialA.write(dataByte2);
    SerialA.write(125);*/
  }

}

// Updated setMotorSpeed function to control IR2104 motor driver board
void setMotorSpeed(int speed) {
  digitalWrite(electricBrakePin, LOW);
  analogWrite(motorInPin, speed);
  digitalWrite(motorSdPin, HIGH);
}

// Updated applyElectricBrake function to work with IR2104 motor driver board
void applyElectricBrake() {
  digitalWrite(electricBrakePin, HIGH);
  digitalWrite(motorInPin, LOW);
  digitalWrite(motorSdPin, LOW);
}


float rpmToMph(float rpm) {
  float metersPerMinute = (rpm * wheelCircumference);
  float metersPerHour = metersPerMinute * 60;
  float milesPerHour = metersPerHour * 0.000621371;
  return milesPerHour;
}

