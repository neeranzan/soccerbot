/**
 * author Niranjan Humagain
 */

import javax.servlet.*;

import java.io.*;
import java.net.URL;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InputHandler
 */
@WebServlet(description = "Class to handle the input and process for the response", urlPatterns = { "/getResponse" })
public class InputHandler extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private ServletContext context;
	private ChatbotParser chatbot;
	private Date initialDate= new Date();

	@Override
	public void init(ServletConfig config) throws ServletException {
	    this.context = config.getServletContext();
	}

    /**
     * Default constructor. 
     */
    public InputHandler() {
        // TODO Auto-generated constructor stub
    }
    
   

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String inputMessage=request.getParameter("msg");
		//context.addServlet("chatbot", chatbot);
		String contextPath=context.getContextPath();
		URL inputData = context.getResource("/resources/soccer.xml");
		URL inputLog = context.getResource("/resources/log.txt");
		boolean isNew=request.getSession().isNew();
		String sessionId=request.getSession().getId();
		//inputLog.
		try{
			if(chatbot == null || request.getSession().isNew()) {
				chatbot = new ChatbotParser(inputData.getPath(), inputLog.getPath());
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}

		String outputMessage = "";
		Date now = new Date();
		if(!chatbot.shouldQuit()) outputMessage=chatbot.parse(inputMessage, (now.getTime()-initialDate.getTime())/1000);
		
		//create memory.xml
		//URL memoryData = context.getResource("/resources/memory.xml");
		
		
		//File memoryFile=new File(context.getRealPath(("/resources") + "/memory.xml"));
		//if(memoryFile.createNewFile()){
			//MemoryRecord mr=new MemoryRecord(inputMessage,outputMessage);
			//Utility.addRecord(memoryFile, mr);
		//}
		//memoryFile.createNewFile();
		//contextPath

		response.setContentType("text/xml");
        response.setHeader("Cache-Control", "no-cache");
        response.getWriter().write("<Message>" + outputMessage + "</Message>");
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Set response content type
	      response.setContentType("text/html");
	     // PrintWriter out = response.getWriter();
	     // out.println(request.getParameter("msg"));
	      String inputText=(String)request.getParameter("msg");
	      request.setAttribute("inputText", inputText); // Will be available as ${students} in JSP
	        request.getRequestDispatcher("/index.jsp").forward(request, response);
	      
		// TODO Auto-generated method stub
	}
	
	/**
	 * TODO logic to be implemented
	 * function to get the response text
	 */
	protected String getResponse(String msg){
		return "This is the sample response";
		
	}

}
