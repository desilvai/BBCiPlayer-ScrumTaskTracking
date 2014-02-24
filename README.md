BBCiPlayer-ScrumTaskTracking
============================


This file describes the Scrum Task Tracking project and is organized as follows:
- Build and Execution Instructions
- Discussion of Solution


##System Requirements
To execute the application you will need:
- Java 7 SE
- Apache Ant


##Directory Structure
| Path | Description |
|:----:|:-----------:|
| `/` | The project directory |
| `/BBC-iPlayer-ScrumTaskTracking.java` | The application sub-project directory |
| `/BBC-iPlayer-ScrumTaskTracking.test` | The testing sub-project directory |
| `/../src` | The application source code |
| `/../test` | The test source code |
| `/../lib` | The libraries used by the sub-project |
| `/../antBin` | The compiled code (.class files) |
| `/../dist` | The generated jar |
| `/../testOutput/results` | The JUnit test execution results (in XML format) |
| `/../testOutput/reports` | The JUnit test execution report (in HTML no-frames format -- NOTE: This fails if executed from eclipse) |



##Build and Execution Instructions
After downloading the source, you will need to:
- Navigate to `/BBC-iPlayer-ScrumTaskTracking.java/` in the project directory.
- Execute `ant`.  This will compile and package the application code.
- Navigate to `/BBC-iPlayer-ScrumTaskTracking.test/` in the project directory.
- Execute `ant`.  This will compile, package, and execute the JUnit test code.

To clean the project directories, substitute the command `ant clean` instead of `ant` in the above instructions.
