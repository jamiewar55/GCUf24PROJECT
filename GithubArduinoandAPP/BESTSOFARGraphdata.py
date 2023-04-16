import serial
import matplotlib.pyplot as plt
from time import sleep

ser = serial.Serial('COM4', 9600, timeout=1)  

def read_data():
    data = ser.read(12)
    return data

# Set up the plot
plt.ion()
fig, ax = plt.subplots(6, sharex=True)
time_data, current_data, voltage_data, temp1_data, temp2_data, hall_effect1_data, hall_effect2_data = [], [], [], [], [], [], []

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

            time_data.append(len(time_data))
            current_data.append(current)
            voltage_data.append(voltage)
            temp1_data.append(temp1)
            temp2_data.append(temp2)
            hall_effect1_data.append(hall_effect1)
            hall_effect2_data.append(hall_effect2)

            # Update the plots
            ax[0].clear()
            ax[1].clear()
            ax[2].clear()
            ax[3].clear()
            ax[4].clear()
            ax[5].clear()

            ax[0].plot(time_data, current_data)
            ax[0].set_title("Current")

            ax[1].plot(time_data, voltage_data)
            ax[1].set_title("Voltage")

            ax[2].plot(time_data, temp1_data)
            ax[2].set_title("Motor Temperature")

            ax[3].plot(time_data, temp2_data)
            ax[3].set_title("Battery Temperature")

            ax[4].plot(time_data, hall_effect1_data)
            ax[4].set_title("Hall Effect 1")

            ax[5].plot(time_data, hall_effect2_data)
            ax[5].set_title("Hall Effect 2")

            plt.pause(0.01)

        else:
            print("Invalid data received.")
            
    except Exception as e:
        print(f"Error: {e}")

    sleep(1)



