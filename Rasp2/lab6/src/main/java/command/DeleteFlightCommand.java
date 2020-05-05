package command;

import beans.Aircompany;

import java.util.ArrayList;

public class DeleteFlightCommand extends Command {
    @Override
    public void execute() throws Exception {
        int airCode = Integer.valueOf(req.getParameter("airCode"));
        int code = Integer.valueOf(req.getParameter("code"));
        client.deleteFlight(airCode, code);
        Aircompany aircompany = airport.getAirCompany(airCode);
        aircompany.getFlights().remove(aircompany.findFlightByCode(code));
        req.setAttribute("aircompanies", airport.getaircompanies());
    }
}
