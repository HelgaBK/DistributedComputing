package dao;


import beans.Airport;

public interface DAO {
    Airport getAll();
    void updateAircompany(int code, int newCode, String newName);
    void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo);
    void addAircompany(int code, String name);
    void addFlight(int airCode, int code, String from, String to);
    void deleteAircompany(int code);
    void deleteFlight(int airCode, int code);
}
