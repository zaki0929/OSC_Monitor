# OSC Monitor
Gradle project which can send and receive arbitrary OSC on Websocket server.

## Demo
* [OSC Monitor - YouTube](https://youtu.be/F-RZwj9cGRc)

## Requirements
* Gradle
  * Gradle 4.8.1
* Application Server
  * Tomcat 8

## Installation
```
git clone git@github.com:zaki0929/OSC_Monitor.git
```

## Usage
First of all, build the Gradle project and generate a war file.
```
gradle run
```
Next, deploy the war file generated in the previous step.
For example, if you are using Tomcat 8 with Ubuntu, do as follows.
```
sudo cp -r build/libs/OSC-Monitor.war /var/lib/tomcat8/webapps/.
```
Finally, access http://&lt;your IP address&gt;:8080/OSC-Monitor/ from the browser.
