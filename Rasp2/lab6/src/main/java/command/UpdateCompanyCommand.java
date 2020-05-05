package command;

import beans.Aircompany;

import java.util.ArrayList;

public class UpdateCompanyCommand extends Command {
    @Override
    public void execute() throws Exception {
        int code = Integer.valueOf(req.getParameter("code"));
        String newCode = req.getParameter("newCode");
        String newName = req.getParameter("newName");
        Aircompany aircompany = airport.getAirCompany(code);
        if(newName.equals("")){
            newName = aircompany.name;
        }
        if(newCode.equals("")){
            newCode = String.valueOf(aircompany.code);
        }
        client.updateAircompany(code, Integer.valueOf(newCode), newName);


        aircompany.code = Integer.valueOf(newCode);
        aircompany.name = newName;
        req.setAttribute("aircompanies", airport.getaircompanies());
    }
}
