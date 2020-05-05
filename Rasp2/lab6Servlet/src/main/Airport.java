package main;

import java.io.Serializable;
import java.util.ArrayList;

public class Airport implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Aircompany> aircompanies = new ArrayList<>();
    public void addAircompany(int code, String name){
        aircompanies.add(new Aircompany(code, name));
    }
    public void addAircompany(Aircompany aircompany){ aircompanies.add(aircompany);}
    public Aircompany getAirCompany(int code){
        for(int i = 0;i  < aircompanies.size(); i++){
            if(aircompanies.get(i).code == code){
                return aircompanies.get(i);
            }
        }
        return null;
    }
    public ArrayList<Aircompany> getaircompanies(){return aircompanies;}
    public int countAircompanies(){
        return aircompanies.size();
    }
    public void deleteAircompany(int code) throws Exception{
        Aircompany aircompanyToDelete = getAirCompany(code);
        if(aircompanyToDelete == null){
            throw new Exception("main.Aircompany doesnt exist");
        }
        aircompanies.remove(aircompanyToDelete);
    }
    public void addFlight(int code, String from, String to, int aircompanyCode) throws Exception{
        Aircompany aircompany = getAirCompany(code);
        if(aircompany == null){
            throw new Exception("main.Aircompany doesnt exist");
        }
        ArrayList<Flight> flights= aircompany.getFlights();
        for(int i = 0; i < flights.size(); i++){
            if(flights.get(i).code == code){
                throw new Exception("This flight has already exist");
            }
        }
        Flight flight = new Flight(code, from, to);
        aircompany.addFlight(flight);
    }
}
