package command;

import beans.Aircompany;

import java.util.ArrayList;

public class DeleteCompanyCommand extends Command {
    @Override
    public void execute() throws Exception {
        int code = Integer.valueOf(req.getParameter("code"));
        client.deleteAircompany(code);
        airport.deleteAircompany(code);
        req.setAttribute("aircompanies", airport.getaircompanies());
    }
}
