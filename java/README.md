# Java Tutor App

Java Tutor App is a simple chatbot web application using the OpenAI API.

## Requirements
- Java 8+
- Tomcat 9+
- OpenAI API Key
- Eclipse

## Project Structure
```
java/
├── src/servlet/ChatServlet.java      # Main servlet
├── WEB-INF/web.xml                   # Servlet configuration
├── WEB-INF/lib/                      # Required libraries
└── WEB-INF/jsp/chatPage.jsp          # Chat page
```

## Setup & Run (Mac)
1. Set up the OpenAI API key
   ```sh
   echo "export OPENAI_API_KEY='your_api_key_here'" >> ~/.zshrc
   source ~/.zshrc
   ```

   NOTE: On Mac, if you start Eclipse using the GUI, environment variables may not be applied.

2. Start Eclipse from the terminal:
   ```bash
   /Applications/Eclipse_version.app/Contents/MacOS/eclipse
   ```

3. Deploy on Tomcat
    Start Tomcat and access the app at: `http://localhost:8080/chat`
