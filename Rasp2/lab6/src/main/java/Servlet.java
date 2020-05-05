import beans.Aircompany;
import beans.Airport;
import beans.Flight;
import client.Client;
import command.Command;
import command.factory.CommandFactory;
import command.factory.CommandFactoryImpl;
import dao.ConcreteDAO;
import dao.DAO;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/serv")
public class Servlet extends HttpServlet {
    Client client = Client.getInstance();
    DAO dao;
    Airport airport = new Airport();
    boolean fromRedirect = true;
    public Servlet() throws JMSException {
        dao = null;
        try {
            dao = ConcreteDAO.getInstance();
            airport.setAircompanies(client.getAll().getaircompanies());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try{
            String commandName=req.getParameter("command");
            if (commandName==null){
                req.setAttribute("Error","Wrong command!");
                req.getRequestDispatcher("index.jsp").forward(req,resp);
                return;
            }
            CommandFactory factory = CommandFactoryImpl.getFactory();
            Command command=factory.getCommand(commandName,airport, req,resp);
            command.execute();
            } catch (Exception e1) {
            e1.printStackTrace();
        }
        req.getRequestDispatcher("index.jsp").forward(req,resp);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("aircompanies", airport.getaircompanies());
        req.setAttribute("firstStartToken", false);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
