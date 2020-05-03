package sample;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class XmlWorker {
    private Airport airport;
    public ArrayList<Aircompany> getaircompanies(){return airport.getaircompanies();}
    XmlWorker() throws ParserConfigurationException, IOException, SAXException {
        airport = new Airport();
        File inputFile = new File("src/sample/airport.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(inputFile);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("aircompany");
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element eAirCompany = (Element) nNode;
                Aircompany aircompany = new Aircompany(Integer.valueOf(eAirCompany.getAttribute("code")),
                        eAirCompany.getAttribute("name"));
                airport.addAircompany(aircompany);
                NodeList flights = eAirCompany.getElementsByTagName("flight");

                for(int j = 0; j < flights.getLength(); j++){
                    Node fNode = flights.item(j);

                    if (fNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eFlight = (Element) fNode;
                        Flight flight = new Flight(Integer.valueOf(eFlight.getAttribute("code")),
                                eFlight.getAttribute("from"), eFlight.getAttribute("to"));
                        aircompany.addFlight(flight);
                    }
                }

            }
        }
    }
    public void saveToXml(){
        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("airport");
            document.appendChild(root);
            for(int i = 0; i < airport.countAircompanies(); i++){
                Element aircompanyElement = document.createElement("aircompany");
                aircompanyElement.setAttribute("code", String.valueOf(airport.getaircompanies().get(i).code));
                aircompanyElement.setAttribute("name", airport.getaircompanies().get(i).name);
                for(int j = 0; j < airport.getaircompanies().get(i).getFlights().size(); j++){
                    Flight flight = airport.getaircompanies().get(i).getFlights().get(j);
                    Element flightElement = document.createElement("flight");
                    flightElement.setAttribute("code", String.valueOf(flight.code));
                    flightElement.setAttribute("from", flight.from);
                    flightElement.setAttribute("to", flight.to);
                    aircompanyElement.appendChild(flightElement);
                }
                root.appendChild(aircompanyElement);
            }
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File("src/sample/airport.xml"));
            transformer.transform(domSource, streamResult);
        }catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }
    public void addAircompany(Aircompany aircompany){
        airport.addAircompany(aircompany);
    }
    public void addFlight(int code, Flight flight){
        Aircompany aircompany = airport.getAirCompany(code);
        aircompany.addFlight(flight);
    }
    public Aircompany findAircompanyByCode(int code){
        return airport.getAirCompany(code);
    }
    public void updateCompany(int code, int newCode, String newName){
        Aircompany aircompany = airport.getAirCompany(code);
        aircompany.name = newName;
        aircompany.code = newCode;
    }
    public void updateFlight(int codeAircompany, int codeFlight,  int newCode, String newFrom, String newTo){
        Aircompany aircompany = airport.getAirCompany(codeAircompany);
        Flight flight = aircompany.findFlightByCode(codeFlight);
        flight.code = newCode;
        flight.from = newFrom;
        flight.to = newTo;
    }
    public void deleteCompany(int code){
        Aircompany aircompany = airport.getAirCompany(code);
        getaircompanies().remove(aircompany);
    }
    public void deleteFlight(int codeAircompany, int codeFlight){
        Aircompany aircompany = airport.getAirCompany(codeAircompany);
        Flight flight = aircompany.findFlightByCode(codeFlight);
        aircompany.getFlights().remove(flight);
    }
}
