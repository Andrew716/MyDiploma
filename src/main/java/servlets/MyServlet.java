package servlets;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Andrii on 5/26/2015.
 */
@WebServlet("/s")
public class MyServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        super.doPost(request,response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        response.setContentType("text/html");
        //RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        //dispatcher.forward(request,response);
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/inputFile.jsp");
        requestDispatcher.forward(request,response);
    }
}
