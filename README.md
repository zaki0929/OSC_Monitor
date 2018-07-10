# OSC Monitor
Gradle project which can send and receive arbitrary OSC on Websocket server.

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
sudo cp -r build/libs/websocket.war /var/lib/tomcat8/webapps/.
```
Finally, access http://<your IP address>:8080/websocket/ from the browser.
