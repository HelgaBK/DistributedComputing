package command.factory;

import beans.Airport;
import command.Command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface CommandFactory {
    Command getCommand(String name, Airport airport, HttpServletRequest request, HttpServletResponse response) throws IllegalArgumentException;
}
