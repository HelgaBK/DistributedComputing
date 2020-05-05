package command;

import beans.Aircompany;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

public class AddCompanyCommand extends Command {
    @Override
    public void execute() throws ServletException, IOException, JMSException {
        int code = Integer.valueOf(req.getParameter("newCode"));
        String name = req.getParameter("newName");
        client.addAircompany(code, name);
        airport.addAircompany(new Aircompany(code, name));
        req.setAttribute("aircompanies", airport.getaircompanies());
    }
}
