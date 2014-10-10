DataLogger
==========

Data collection with Android devices

## Purpose

This is a simple Android app that collects data from sensors such as GPS, accelerometer and magnetometer on Android devices and write the data to different files. I find it very helpful in quickly collecting some data, at least for my research. 

## Usage

- You can choose the folder name where data will be saved.
	- By default, a string with the following format "phone-YYYYMMDDhhmm" will be used.
	- YYYY for 4-digit year, MM for 2-digit month, DD for 2-digit date, hh for 2-digit hour, mm for 2-digit minute)
- The "Setting" button will bring you to the sensor settings. 
	- The default setting is to collect data at maximal rate, for the assumption that you are a data aficionado and battery is not a concern.
- Press the "Start" button to start.
- When data are collected, log messages on how often different data are recorded will be displayed on the phone screen, so you can monitor the data collection process.
- When you are done, press the "Finish" button.
- Data are saved in the folder you specified.
- There are four files in each folder
	- Acc.csv
		- Accelerometer data
		- Format: system epoch time [milliseconds], X value, Y value, Z value, accuracy
	- Mag.csv
		- Magnetometer data
		- Format: system epoch time [milliseconds], X value, Y value, Z value, accuracy
	- Gps.csv
		- GPS based location data
		- Format: epoch time of the location [milliseconds], system epoch time [milliseconds], latitude [degrees], longitude [degrees], accuracy [meters], speed [meter/second], bearing [degrees], altitude [meters]
	- Net.csv
		- Wi-Fi based location data
		- Format: epoch time of the location [milliseconds], system epoch time [milliseconds], latitude [degrees], longitude [degrees], accuracy [meters], speed [meter/second], bearing [degrees], altitude [meters]
		
