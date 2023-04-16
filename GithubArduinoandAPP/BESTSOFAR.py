import serial
from time import sleep

ser = serial.Serial('COM4', 9600, timeout=1)  # Replace 'COM4' with the port your Arduino is connected to

def read_data():
    data = ser.read(12)
    return data

while True:
    try:
        data_bytes = read_data()

        if len(data_bytes) == 12:
            current = int.from_bytes(data_bytes[0:2], 'little')
            voltage = int.from_bytes(data_bytes[2:4], 'little')
            temp1 = int.from_bytes(data_bytes[4:6], 'little')
            temp2 = int.from_bytes(data_bytes[6:8], 'little')
            hall_effect1 = int.from_bytes(data_bytes[8:10], 'little')
            hall_effect2 = int.from_bytes(data_bytes[10:12], 'little')

            print(f"Current: {current}, Voltage: {voltage}, Temp1: {temp1}, Temp2: {temp2}, Hall Effect 1: {hall_effect1}, Hall Effect 2: {hall_effect2}")

        else:
            print("Invalid data received.")
            
    except Exception as e:
        print(f"Error: {e}")

    sleep(1)
