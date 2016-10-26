# chatbot

Go to http://nsa-temp.cs.unm.edu:8080/chatbot to connect to our chatbot

You need a WebServer Glassfish or Servlet Container Tomcat for this application to run . I am using Tomcat 8 for this.

JSP file is the presentation.
InputHandler is the servlet that handles the javascript ajax request and provides with XML response.

********
Basic design of our chatbot. data.xml in war/resources contains all of the dialog information with Phrases (direct matches)
Keywords (regex matches) and responses which are selected at random (Stochasticity). The xml file also contains features
such as conversation starters that are used when parsing an unknown input, memory thresholds which changes responses based
off how often you've used a type of input, and multipart responses which require an intermediate user input to finish.
The xml data is read into the program by the XMLParser class at startup and populates the Record class data structure.
The ChatbotParser is the top level of the chatbot logic portion and contains the parse(String) method used to get a response.
********
