===============================================================================
      README.TXT - AASIS
===============================================================================

   Adaptive Architecture for Systems of Intelligent Systems (AASIS)
   PhD Software Engineering Project
   Kansas State University
   
   Multiagent architecture for agents participating and under the direction of
   multiple groups concurrently.
   
   Unless required by applicable law or agreed to in writing, software
   is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
   CONDITIONS OF ANY KIND, either express or implied.

   Denise Case
   dmcase@ksu.edu
   denisecase@gmail.com

===============================================================================
       ABOUT THIS PROJECT
===============================================================================
   This project provides core classes for the multigroup agent architecture.

   It uses OMACS and GMoDS and is based on the OBAA++ architecture also 
   developed as part of this research.

===============================================================================
      DEPENDENCIES - DEVELOPMENT
===============================================================================

Install the following development software:

  JDK 1.8 from
    http://www.oracle.com/technetwork/java/javase/downloads/index.html.
    Set your JAVA_HOME environment variable to 
    C:\Program Files\Java\jdk1.8.0_25 (or your jdk location)
    Add  %JAVA_HOME%\bin to your path.
    I installed jdk-8u5-windows-x64.exe
    to :\Program Files\Java\jdk1.8.0_25.
   
  
===============================================================================
      Integrated Development Environment (IntelliJ or Eclipse)
===============================================================================
   
Install at least one IDE.

  I recommend IntellijIDEA 13 with support for Java 1.8.
    You can use the free community version: 
    http://www.jetbrains.com/idea/
    Or apply for a free fully-functional classroom version:
    http://www.jetbrains.com/idea/buy/choose_edition.jsp?license=CLASSROOM.
    Intellij integrates with our other option, Eclipse.  
 
  Eclipse Kepler SR2: Standard 4.3.2 with Java 8 support
    http://www.eclipse.org/downloads/index-java8.php
    eclipse-standard-kepler-SR2-Java8-win32-x86_64.zip
    I extracted the contents to:
    C:\eclipse-standard-kepler-SR2-Java8-win32-x86_64
    Note: Java and Eclipse must be be either BOTH 32-bit OR BOTH 64-bit.
    The modified eclipse.ini file is included below. 

  Then through "new software":
    Java8 Support: 
    http://download.eclipse.org/eclipse/updates/4.3-P-builds/
	
The following Eclipse plugins are used for modifying goal & role 
models:
	
    AgentTool3 Eclipse plugin (Core and Process Editor) from
        http://agenttool.cis.ksu.edu/
        
   Groovy Eclipse plugin from 
        http://dist.springsource.org/release/GRECLIPSE/e4.3/

===============================================================================
      ECLIPSE WORKSPACE FOLDERS
=============================================================================== 
  Eclipse uses workspace folders to hold IDE configuration information that 
  we do not want shared across machines. Our current location is a 
  sub folder of the associated workspace. See:
  http://eclipse.dzone.com/articles/eclipse-workspace-tips
  for more information. 
        
===============================================================================
      SOURCE CONTROL
===============================================================================        

Install Git for source control.  

===============================================================================
       QUICK START
===============================================================================
	
Check out code.
	Go to the folder where you want to keep your code, say the "projects" folder.
	Use Git to clone the code from https://ksucase@bitbucket.org/ksucase/aasis.git.
	This will create a new folder called "aasis".


Start RabbitMQ.
	Open a command window as administrator. From
	C:\Program Files (x86)\RabbitMQ Server\rabbitmq_server-3.2.1\sbin	
    type "rabbitmq-service start" and click Enter.
	    rabbitmq-server.bat starts the broker as an application.
	    rabbitmq-service.bat manages the service and starts the broker.
	    rabbitmqctl.bat manages a running broker.
	
	Point a browser to (the final slash is required): http://localhost:15672
	Login with:  guest / guest
  
Start simulation.
	Open a command window.  Type:
	gradlew run


Verify display.
	When everything is running successfully, you'll see screens appear showing
	complex AASIS agents with their subagent personae running assigned tasks.
		
Stop rabbitmq with:
    rabbitmq_service stop


===============================================================================
       RabbitMQ for Messaging 
===============================================================================  

RabbitMQ 3.1.5 and the management plugin from
   http://www.rabbitmq.com/install-windows.html/
   Requires Erlang: http://www.erlang.org/download.html
   Set RABBITMQ_SERVER environment variable and add to PATH.
   Open a command window as administrator and type the following commands from
   C:\Program Files (x86)\RabbitMQ Server\rabbitmq_server-3.2.1\sbin:
      rabbitmq-plugins enable rabbitmq_management  
      rabbitmq-server start

To delete all queues and start completely fresh after running, use:
  rabbitmqctl stop_app
  rabbitmqctl reset
  rabbitmqctl start_app
                 
===============================================================================
     Gradle build tool (optional)
===============================================================================    
   
Completely updating the project involves a variety of tasks:
     - Deleting the compiled artefacts and starting a fresh build.
     - Compiling Java, Groovy, and Scala files.
     - Run the Java JUnit and Groovy Spock tests to be sure everything works.
     - Update the JavaDocs, GroovyDocs, and ScalaDocs.
     - Create a new executable jar file.
   There are a variety of good build tools that will automate those tasks (and more).
      Ant or Maven are examples of build tools.  Our current choice
      is Gradle.  It'stringBuilder easy to get started, it allows us to
      remove many of the standard library jar files from our IPDS 
      src/main/resources/lib folder, and it offers a quicker, standard
      way of checking out the IPDS project and getting up to speed.  
   Download and install gradle from http://www.gradle.org/.
   Set GRADLE_HOME environment variable to C:\gradle-1.10
   and include %GRADLE_HOME%/bin in your Windows Environment path variable.
   Open an Administrator Command Window in the root ipds folder, and type:  
                 gradle clean build assemble


===============================================================================
       CONFIGURATION FILES
===============================================================================
		
The following configuration files are required for each local group (a system,
such as a holonic multiagent system, may have multiple local groups).

	Agent.xm  		- local organization agents and their capabilities.
	Environment.xml - objects in the IPDS environment
	Initialize.xml 	- provides custom goal parameters.
	GoalModel.goal 	- describes the objectives of the system
	RoleModel.role 	- describes  capabilities needed to play roles and
                          roles that agents can play to achieve specific goals.
                          
The utils package has builder programs to assist with auto-generation of the 
many agent and initialize files used in the test cases.

===============================================================================
       EDITING CONFIGURATION FILES
===============================================================================

Two of the configuration files - the goal models (.goal) and the
role models (.role) can be easily edited using the recently updated
agentTool3 modeling plugins.

See http://agenttool.cis.ksu.edu for more information.

===============================================================================
       PROJECT REPOSITORY
===============================================================================

 The following module is required and can be checked out with your Git client
 from https://bitbucket.org/ksucase.  

 The IPDS project includes jar files for:
	Goal Model for Dynamic Systems (GMoDS).
	Organization Model for Adaptive Computational Systems (OMACS).
	agentTool3
	

===============================================================================
       ADDING NEW BEHAVIOR
===============================================================================

To add new behavior to the system:

Add goals to goal model(s).
    Use agentTool3 on the models in src/main/resources/configs/standardmodels.
    Include goal(s) under AND or OR.
    Add parameters.
    Make sure parameters get passed from top goal to child goals to triggered goals etc.
    Add triggers, precedes, etc as appropriate.
Add roles to roles model(s).
    Use agentTool3 on the models in src/main/resources/configs/standardmodels.
    Refer the role to the goal and the required capabilities.
    All leaf goals in the goal model must have an associated role in the role model.
Update goal identifiers.
Update goal events.
Update goal parameters.
Create new goal parameter "guideline" classes as needed.
Update GuidelineManager to read guideline information from XML file.
Update role indentifers.
Create new message classes as needed.
Create new capabilities.
Create new plan(s) and plan states.
Update CapabilityUniqueIndentifier provider in agent/persona/process.
Update MasterCapabilityUniqueIndentifier provider in agent/cc_master/process.
Update plan_selector.

Update the Builder utility to add the new capabilities and/or agent types.
Update the Builder utility to create custom agent guidelines.

Distributing updated jar, etc. Type:
    gradle distZip and copy to a derivative project.

===============================================================================
  END
===============================================================================
