package sample.dao;

import sample.Airport;
import sample.Flight;

import java.sql.*;

public class ConcreteDAO implements DAO {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/airport?serverTimezone=UTC";

    static final String USER = "root";
    static final String PASS = "max12398";

    Connection conn = null;
    Statement stmt = null;
    public ConcreteDAO() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        conn = DriverManager.getConnection(DB_URL,USER,PASS);
    }

    @Override
    public Airport getAll() {
        Airport airport = new Airport();
        try {

            stmt = conn.createStatement();
            String sql;
            sql = "SELECT *  FROM aircompany";
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                int code = rs.getInt("code");
                String name = rs.getString("name");
                airport.addAircompany(code, name);
            }


            sql = "SELECT *  FROM flight";
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                int airCode = rs.getInt("airCode");
                int code = rs.getInt("code");
                String from = rs.getString("cityFrom");
                String to = rs.getString("cityTo");
                airport.getAirCompany(airCode).addFlight(new Flight(code, from, to));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return airport;
    }

    @Override
    public void updateAircompany(int code, int newCode, String newName) {
        try {
            stmt = conn.createStatement();
            String sql;
            sql = " update aircompany set code=" + newCode + ", name=\"" + newName + "\" where code=" + code + ";";
            int status = stmt.executeUpdate(sql);
            if(status < 0){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo){
        try {
            stmt = conn.createStatement();
            String sql;
            sql = " update flight set code=" + newCode + ", cityFrom=\"" + newFrom +
                    "\", cityTo=\"" + newTo+ "\" where code=" + code + " and airCode=" + airCode +";";
            System.out.println(sql);
            int status = stmt.executeUpdate(sql);
            if(status < 0){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addAircompany(int code, String name) {
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "insert into aircompany values (" + code + ", \"" + name + "\");";
            int status = stmt.executeUpdate(sql);
            if(status < 0){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFlight(int airCode, int code, String from, String to) {
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "insert into flight values (" + airCode + ", " + code + ",\"" + from + "\",\"" + to + "\");";
            int status = stmt.executeUpdate(sql);
            if(status < 0){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAircompany(int code) {
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "delete from aircompany where code = " + code + ";";
            int status = stmt.executeUpdate(sql);
            if(status < 0){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteFlight(int airCode, int code) {
        try {
            stmt = conn.createStatement();
            String sql;
            sql = "delete from flight where code = " + code + " and airCode=" + airCode + ";";
            int status = stmt.executeUpdate(sql);
            if(status < 0){
                System.out.println("Error");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
