package sample.server;

import sample.Airport;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    Airport getAll() throws RemoteException;
    void updateAircompany(int code, int newCode, String newName) throws RemoteException;
    void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo) throws RemoteException;
    void addAircompany(int code, String name) throws RemoteException;
    void addFlight(int airCode, int code, String from, String to) throws RemoteException;
    void deleteAircompany(int code) throws RemoteException;
    void deleteFlight(int airCode, int code) throws RemoteException;
}
