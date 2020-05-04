package sample.client;

import sample.Airport;

import java.io.*;
import java.net.Socket;

import static sample.OperationCodes.*;


public class Client {
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    ObjectInputStream  objectInputStream;
    public Client(String ip, int port) throws IOException
    {
        socket = new Socket(ip, port);
        in = new DataInputStream(socket.getInputStream( ));
        out = new DataOutputStream(socket.getOutputStream());
        InputStream inputStream = socket.getInputStream();
        objectInputStream = new ObjectInputStream(inputStream);
    }
    public Airport getAll() throws IOException, ClassNotFoundException {
        out.writeInt(GET_ALL);
        return(Airport)objectInputStream.readObject();
    }
    public void updateAircompany(int code, int newCode, String newName) throws IOException {
        out.writeInt(UPDATE_AIRCOMPANY);
        out.writeInt(code);
        out.writeInt(newCode);
        out.writeUTF(newName);
    }
    public void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo) throws IOException {
        out.writeInt(UPDATE_FLIGHT);
        out.writeInt(airCode);
        out.writeInt(code);
        out.writeInt(newCode);
        out.writeUTF(newFrom);
        out.writeUTF(newTo);
    }
    public void addAircompany(int code, String name) throws IOException {
        out.writeInt(ADD_AIRCOMPANY);
        out.writeInt(code);
        out.writeUTF(name);
    }

    public void addFlight(int airCode, int code, String from, String to) throws IOException {
        out.writeInt(ADD_FLIGHT);
        out.writeInt(airCode);
        out.writeInt(code);
        out.writeUTF(from);
        out.writeUTF(to);
    }
    public void deleteAircompany(int code) throws IOException {
        out.writeInt(DELETE_AIRCOMPANY);
        out.writeInt(code);
    }
    public void deleteFlight(int airCode, int code) throws IOException {
        out.writeInt(DELETE_FLIGHT);
        out.writeInt(airCode);
        out.writeInt(code);
    }

}
