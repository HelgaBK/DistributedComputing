package sample.server;

import sample.Airport;
import sample.dao.ConcreteDAO;
import sample.dao.DAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;

public class RemoteServerImpl extends UnicastRemoteObject implements RemoteServer {

    DAO dao;
    public RemoteServerImpl() throws RemoteException, SQLException, ClassNotFoundException {
        super();
        dao = new ConcreteDAO();
    }


    public Airport getAll(){
        Airport airport = dao.getAll();
        return airport;
    }

    public void updateAircompany(int code, int newCode, String newName) {
        dao.updateAircompany(code, newCode, newName);
    }
    public void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo)  {
        dao.updateFlight(airCode, code, newCode, newFrom, newTo);
    }
    public void addAircompany(int code, String name)  {
        dao.addAircompany(code, name);
    }
    public void addFlight(int airCode, int code, String from, String to)  {
        dao.addFlight(airCode, code, from, to);
    }
    public void deleteAircompany(int code) {
        dao.deleteAircompany(code);
    }

    public void deleteFlight(int airCode, int code)  {
        dao.deleteFlight(airCode, code);
    }


}
