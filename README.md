# Gradle dependencies viewer (tool)
A simple web UI to analyze dependencies for your project based on the text data generated from ```gradle dependencies``` command.
![Image of HDFS-Shell](https://github.com/avast/gradle-dependencies-viewer/blob/master/web/screenshot.png)

[![Build Status - Master](https://travis-ci.org/avast/gradle-dependencies-viewer.svg?branch=master)](https://travis-ci.org/avast/gradle-dependencies-viewer) ![Linux](https://img.shields.io/badge/os-linux-green.svg?style=flat) ![Windows](https://img.shields.io/badge/os-windows-green.svg?style=flat) ![Apache 2](https://img.shields.io/badge/license-Apache2-blue.svg?style=flat)
## Purpose
Since there is still no good support for Gradle dependencies view in IntelliJ IDEA (at least not good as for Maven is) and it's really difficult to browse (especially for larger projects with tens of listed dependencies) we decided to create this very simple tool.
This tool helps us to solve such common dependency-hell problem like *"Where this dependency/artifact came from?"* or *"Which dependencies are coming with this included artifact?"*.

## Using dependencies viewer
1. Run command ```gradle dependencies > dep.txt``` inside of your project directory.`
2. Drag&Drop dep.txt file into input area. File will be automatically uploaded and parsed. Alternativaly you can copy&paste the output of gradle dependencies command into the input area.
3. Browse dependencies tree in the left panel and/or use search input box to find artifact you need to explore
4. If you want to generate gradle code to exclude some artifact, press right mouse button to view context menu and select *Exclude artifact*.

### Working demo
[You can try to use it here](http://gradle.vity.cz/)

### Launching 
Default port value has been set to 8090.

#### Requirements:
- JDK 1.8

#### Building from sources
```gradle build``` or ```gradlew.bat build```

#### Running binary
##### Running application 
Extract ```build/distributions/gradle-dependency-viewer-x.x.x.zip``` (or .tar on Linux) and use ```/bin/gradle-dependency-viewer.bat``` (Win) or ```/bin/gradle-dependency-viewer``` (Linux)

**OR**
##### Running single fat jar
```java -jar gradle-dependency-viewer-1.0.0.jar --server.port=8090```

File ```gradle-dependency-viewer-x.x.x.jar``` is located in ```build/libs```.

Then open [http://localhost:8090/](http://localhost:8090/) in your web browser.

##Project programming info
The project is using Gradle 3.x to build. The project is based on Spring Boot using Thymeleaf template engine. 

##License
Apache 2 License.

###Contact
Author&Maintainer: Ladislav Vitasek  aka Vity - vitasek/@/avast.com

