package command;

import beans.Aircompany;
import beans.Flight;

import java.util.ArrayList;

public class UpdateFlightCommand extends Command {
    @Override
    public void execute() throws Exception {
        int airCode = Integer.valueOf(req.getParameter("airCode"));
        int code = Integer.valueOf(req.getParameter("code"));
        String newCode = req.getParameter("newCode");
        String newFrom = req.getParameter("newFrom");
        String newTo = req.getParameter("newTo");

        Aircompany aircompany = airport.getAirCompany(airCode);
        Flight flight = aircompany.findFlightByCode(code);
        if(newFrom.equals("")){
            newFrom = aircompany.name;
        }
        if(newTo.equals("")){
            newTo = aircompany.name;
        }
        if(newCode.equals("")){
            newCode = String.valueOf(aircompany.code);
        }

        client.updateFlight(airCode, code, Integer.valueOf(newCode), newFrom ,newTo);


        flight.code =  Integer.valueOf(newCode);
        flight.from = newFrom;
        flight.to = newTo;
        req.setAttribute("aircompanies", airport.getaircompanies());
    }
}
