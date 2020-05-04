package sample.server;

import sample.Airport;
import sample.dao.ConcreteDAO;
import sample.dao.DAO;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server {
    private ServerSocket server = null;
    private Socket socket = null;
    private DataOutputStream out = null;
    private DataInputStream in = null;
    ObjectOutputStream objectOutputStream;
    OutputStream outputStream;
    DAO dao;
    public Server(){

    }
    public void start(int port) throws IOException, SQLException, ClassNotFoundException {
        dao = new ConcreteDAO();
        server = new ServerSocket(port);
        socket = server.accept();
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());
        outputStream = socket.getOutputStream();
        objectOutputStream = new ObjectOutputStream(outputStream);
        System.out.println("running...");
        while (true) {
            int code = in.readInt();
            switch(code){
                case 0:{
                    getAll();
                    break;
                }
                case 1:{
                    updateAircompany();
                    break;
                }
                case 2:{
                    updateFlight();
                    break;
                }
                case 3:{
                    addAircompany();
                    break;
                }
                case 4:{
                    addFlight();
                    break;
                }
                case 5:{
                    deleteAircompany();
                    break;
                }
                case 6:{
                    deleteFlight();
                    break;
                }
            }
        }
    }
    public void getAll() throws IOException {
        Airport airport = dao.getAll();
        objectOutputStream.writeObject(airport);
    }
    public void updateAircompany() throws IOException {
        int code = in.readInt();
        int newCode = in.readInt();
        String newName = in.readUTF();
        dao.updateAircompany(code, newCode, newName);
    }
    public void updateFlight() throws IOException {
        int airCode = in.readInt();
        int code = in.readInt();
        int newCode = in.readInt();
        String newFrom = in.readUTF();
        String newTo = in.readUTF();
        dao.updateFlight(airCode, code, newCode, newFrom, newTo);

    }
    public void addAircompany() throws IOException {
        int code = in.readInt();
        String name = in.readUTF();
        dao.addAircompany(code, name);
    }
    public void addFlight() throws IOException {
        int airCode = in.readInt();
        int code = in.readInt();
        String from = in.readUTF();
        String to = in.readUTF();
        dao.addFlight(airCode, code, from, to);
    }
    public void deleteAircompany() throws IOException {
        int code = in.readInt();
        dao.deleteAircompany(code);
    }

    public void deleteFlight() throws IOException {
        int airCode = in.readInt();
        int code = in.readInt();
        dao.deleteFlight(airCode, code);
    }
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        Server server = new Server();
        server.start(65000);
    }

}
