BBCiPlayer-ScrumTaskTracking
============================


This file describes the Scrum Task Tracking project and is organized as follows:
- System Requirements
- Project Directory Structure
- Build and Execution Instructions
- Discussion of Solution


##System Requirements
To execute the application you will need:
- Java 7 SE
- Apache Ant


##Project Directory Structure
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


##Discussion of Solution
This project takes in and stores stories in a relational database.  It then allows callers to plan a sprint based on the available stories.  Callers can also remove stories from the database (and can thus plan successive sprints by removing the stories from the last plan an generating another plan).  

I chose to use Derby's in-memory database as our relational database.  I chose this because I cannot guarantee that you have the specific database I chose to use installed on the evaluation system.  This would be replaced with a real database in production.  I chose to use a relational database because it will handle concurrent accesses for us.  If supporting multiple projects, I assume we will extend the interface to communicate which project's set of stories we are working with before we add/remove/generate a sprint.  

Since this code is supposed to be part of a web-service (per the project description), I would expect that we would use the application server's connection pool rather than opening and closing connections as we do in the source code.  
