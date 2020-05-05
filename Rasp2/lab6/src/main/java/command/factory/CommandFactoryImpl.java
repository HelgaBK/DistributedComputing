package command.factory;

import javax.jms.JMSException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Airport;
import client.Client;
import command.*;
import dao.ConcreteDAO;

import java.sql.SQLException;

public class CommandFactoryImpl implements CommandFactory {
    private CommandFactoryImpl(){};
    private static CommandFactoryImpl factory=new CommandFactoryImpl();
    public static CommandFactory getFactory(){
        return factory;
    }
    @Override
    public Command getCommand(String name, Airport airport, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException {
        if (name == null) {
            throw new IllegalArgumentException("Name cannot be NULL");
        } else {
            Command command = null;

            //----------------------------//
            if (name.equals("addFlight")) {
                command = new AddFlightCommand();
            } else if (name.equals("addCompany")) {
                command = new AddCompanyCommand();
            } else if (name.equals("updateCompany")) {
                command = new UpdateCompanyCommand();
            } else if (name.equals("updateFlight")) {
                command = new UpdateFlightCommand();
            } else if (name.equals("deleteCompany")) {
                command = new DeleteCompanyCommand();
            } else if (name.equals("deleteFlight")) {
                command = new DeleteFlightCommand();
            }

            //---------------------------//


            if (command == null) {
                throw new IllegalArgumentException("Wrong command name");
            }

            command.setResp(response);
            command.setReq(request);
            try {
                command.setDao(ConcreteDAO.getInstance());
                command.setClient(Client.getInstance());
            } catch (ClassNotFoundException | SQLException | JMSException e) {
                e.printStackTrace();
            }
            command.setAirport(airport);

            return command;
        }
    }
}
