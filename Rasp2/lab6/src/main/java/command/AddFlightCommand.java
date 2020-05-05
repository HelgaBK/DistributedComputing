package command;

import beans.Aircompany;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;

public class AddFlightCommand extends Command {
    @Override
    public void execute() throws Exception {
        int airCode = Integer.valueOf(req.getParameter("airCode"));
        int code = Integer.valueOf(req.getParameter("newCode"));
        String from = req.getParameter("newFrom");
        String to = req.getParameter("newTo");
        client.addFlight(airCode,code, from, to);
        airport.addFlight(code, from, to, airCode);
        req.setAttribute("aircompanies", airport.getaircompanies());
    }
}
