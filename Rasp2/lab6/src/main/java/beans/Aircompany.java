package beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Aircompany implements Serializable {
    public int code;
    public String name;
    private ArrayList<Flight> flights = new ArrayList<>();
    public Aircompany(int code, String name){
        this.code = code;
        this.name = name;
    }
    public ArrayList<Flight> getFlights(){
        return flights;
    }
    public int getCode(){ return code;}
    public String getName(){ return name;}
    public void addFlight(Flight flight){
        flights.add(flight);
    }
    public Flight findFlightByCode(int code){
        for(int i = 0; i  < flights.size(); i++){
            if(flights.get(i).code == code){
                return flights.get(i);
            }
        }
        return null;
    }

}
