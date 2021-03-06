package servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import beans.UserBean;

@WebServlet("/UpdateProfileServlet")
public class UpdateProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	final static Logger logger = Logger.getLogger(UpdateProfileServlet.class);   
	
    public UpdateProfileServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.setContentType("text/json");
		PrintWriter out=response.getWriter();
		
		String username=request.getParameter("inputUsername");
		String firstName=request.getParameter("inputFirstName");
		String lastName=request.getParameter("inputLastName");
		String address1=request.getParameter("inputAddress1");
		String email=request.getParameter("inputEmail");
		String phone=request.getParameter("inputPhoneNumber");
		String address2=request.getParameter("inputAddress2");
		String dateofbirth=request.getParameter("inputDate");
		String country=request.getParameter("inputCountry");
		String state=request.getParameter("inputState");
		String city=request.getParameter("inputCity");
		String gender=request.getParameter("Gender");
		String password=request.getParameter("inputPassword");
		
		UserBean userBean = new UserBean();
		userBean.setUserName(username);
		userBean.setPassword(password);
		userBean.setFirstName(firstName);
		userBean.setLastName(lastName);
		userBean.setAddress1(address1);
		userBean.setEmail(email);
		userBean.setPhone(phone);
		userBean.setAddress2(address2);
		userBean.setDateOfBirth(dateofbirth);
		userBean.setCountry(country);
		userBean.setState(state);
		userBean.setCity(city);
		userBean.setGender(gender);
		
		request.setAttribute("bean",userBean);
		
		Boolean status = false;
		try {
			 
			Client client = Client.create();
			WebResource webResource = client.resource("http://localhost:9090/OnlineBiddingServices/rest/updateprofile/edituser");
			
			Gson userJson = new Gson();
			String data = userJson.toJson(userBean);
			
			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("username", username);
			formData.add("password", password);
			formData.add("firstName", firstName);
			formData.add("lastName", lastName);
			formData.add("address1", address1);
			formData.add("email", email);
			formData.add("phone", phone);
			formData.add("address2",address2);
			formData.add("city", city);
			formData.add("state", state);
			formData.add("country", country);
			formData.add("dateofbirth", dateofbirth);
			formData.add("gender", gender);
			
			ClientResponse restResponse = webResource
												.type(MediaType.APPLICATION_JSON)
												.post(ClientResponse.class, data);
			
			if (restResponse.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + restResponse.getStatus());
			}
 
			String statusString = restResponse.getEntity(String.class);
			status = Boolean.parseBoolean(statusString);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(status){

			//System.out.println("Entered here");
			RequestDispatcher rd=request.getRequestDispatcher("UpdateProfile.jsp");
			rd.forward(request, response);
			logger.info("Profile update by: "+ username+": SUCCESS");
		}
		else{
			RequestDispatcher rd=request.getRequestDispatcher("Error.jsp");
			rd.forward(request, response);
			logger.info("Profile update by: "+ username+": FAIL");
		}
	
	}

}
