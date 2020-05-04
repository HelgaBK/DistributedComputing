package sample.client;

import sample.Airport;
import sample.server.RemoteServer;

import java.io.*;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;



public class Client {
    RemoteServer server = null;
    public Client() throws RemoteException, NotBoundException, MalformedURLException {
        String url = "//localhost:65000/Server";
        server = (RemoteServer) Naming.lookup(url);
        System.out.println("RMI object found");
    }
    public Airport getAll() throws RemoteException{
        return server.getAll();
    }
    public void updateAircompany(int code, int newCode, String newName) throws RemoteException  {
        server.updateAircompany(code, newCode, newName);
    }
    public void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo) throws RemoteException {
        server.updateFlight(airCode, code, newCode, newFrom, newTo);
    }
    public void addAircompany(int code, String name)  throws RemoteException {
        server.addAircompany(code, name);
    }

    public void addFlight(int airCode, int code, String from, String to) throws RemoteException {
        server.addFlight(airCode, code, from, to);
    }
    public void deleteAircompany(int code) throws RemoteException{
        server.deleteAircompany(code);
    }
    public void deleteFlight(int airCode, int code) throws RemoteException {
        server.deleteFlight(airCode, code);
    }

}
