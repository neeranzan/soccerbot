/**
 * author niranjan humagain
 */

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SessionClearer
 */
@WebServlet(description = "Class to clear the session", urlPatterns = { "/clearSession" })
public class SessionClearer extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletContext context;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SessionClearer() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String sessionId=request.getSession().getId();
		boolean isNew=request.getSession().isNew();
		if(!isNew){
			request.getSession().invalidate();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
