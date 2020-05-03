package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.xml.sax.SAXException;
import sample.dao.ConcreteDAO;
import sample.dao.DAO;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Controller {
    @FXML
    private TreeView treeView;
    @FXML
    private Button addCompany;
    @FXML
    private Button addFlight;
    @FXML
    private Button updateCompany;
    @FXML
    private Button updateFlight;
    @FXML
    private Button deleteCompany;
    @FXML
    private Button deleteFlight;
    @FXML
    private Button save;
    @FXML
    private TextField codeCompany;
    @FXML
    private TextField nameCompany;
    @FXML
    private TextField codeFlight;
    @FXML
    private TextField fromFlight;
    @FXML
    private TextField toFlight;
    @FXML
    private Label information;

    DAO dao;
    @FXML
    public void initialize() {
        try {
            dao = new ConcreteDAO();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        printToTreeView();


    }
    public void printToTreeView(){
        treeView.setRoot(null);
        ArrayList<Aircompany> aircompanyes = dao.getAll().getaircompanies();
        //ArrayList<TreeItem<String>> aircompanyesTree = new ArrayList<>();
        TreeItem<String> root = new TreeItem<>("Airport");

        for(int i = 0; i < aircompanyes.size(); i++){
            String info;
            info = "Code: " + aircompanyes.get(i).code + " Name: " + aircompanyes.get(i).name;
            ArrayList<Flight> flights = aircompanyes.get(i).getFlights();
            TreeItem<String> aircompany = new TreeItem<>(info);
            for(int j = 0; j < flights.size(); j++){
                String flightInfo = flights.get(j).code + " " + flights.get(j).from + " - " + flights.get(j).to;
                TreeItem<String> flight = new TreeItem<>(flightInfo);
                aircompany.getChildren().add(flight);
            }
            // aircompanyesTree.add(aircompany);
            root.getChildren().add(aircompany);
        }
        treeView.setRoot(root);
    }
    @FXML
    private void addCompany(ActionEvent event) {
        String code = codeCompany.getText();
        String name = nameCompany.getText();
        //Aircompany aircompany = new Aircompany(Integer.valueOf(code), name);
        dao.addAircompany(Integer.valueOf(code), name);
        printToTreeView();
    }
    @FXML
    private void addFlight(ActionEvent event) {
        String code = codeFlight.getText();
        String from = fromFlight.getText();
        String to = toFlight.getText();
        TreeItem<String> selectedItem = (TreeItem<String>)treeView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            information.setText("Not selected aircompany");
            return;
        }
        String codeAircompany = selectedItem.getValue().split(" ")[1];
        //Aircompany aircompany = xmlWorker.findAircompanyByCode(Integer.valueOf(codeAircompany));
        //xmlWorker.addFlight(Integer.valueOf(codeAircompany), new Flight(Integer.valueOf(code), from, to));

        dao.addFlight(Integer.valueOf(codeAircompany), Integer.valueOf(code), from, to);
        printToTreeView();

    }
    @FXML
    private void updateCompany(ActionEvent event) {
        String newCode = codeCompany.getText();
        String newName = nameCompany.getText();
        TreeItem<String> selectedItem = (TreeItem<String>)treeView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            information.setText("Not selected aircompany");
            return;
        }
        String codeAircompany = selectedItem.getValue().split(" ")[1];
        String nameAircompany = selectedItem.getValue().split(" ")[3];
        //Aircompany aircompany = xmlWorker.findAircompanyByCode(Integer.valueOf(codeAircompany));
        if(newCode.equals("")){
            newCode = codeAircompany;
        }
        if(newName.equals("")){
            newName = nameAircompany;
        }
        dao.updateAircompany(Integer.valueOf(codeAircompany), Integer.valueOf(newCode), newName);
        printToTreeView();
    }
    @FXML
    public void updateFlight(ActionEvent event){
        String newCode = codeFlight.getText();
        String newFrom = fromFlight.getText();
        String newTo = toFlight.getText();
        TreeItem<String> selectedItem =(TreeItem<String>)treeView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            information.setText("Not selected aircompany");
            return;
        }
        String codeAircompany = selectedItem.getParent().getValue().split(" ")[1];
        String codeFlight = selectedItem.getValue().split(" ")[0];
        String fromFlight = selectedItem.getValue().split(" ")[1];
        String toFlight = selectedItem.getValue().split(" ")[3];
        if(newCode.equals("")){
            newCode = codeFlight;
        }
        if(newFrom.equals("")){
            newFrom = fromFlight;
        }
        if(newTo.equals("")){
            newTo = toFlight;
        }
        dao.updateFlight(Integer.valueOf(codeAircompany), Integer.valueOf(codeFlight),
                Integer.valueOf(newCode), newFrom, newTo);
        printToTreeView();
    }
    @FXML
    public void deleteCompany(ActionEvent event){
        TreeItem<String> selectedItem =(TreeItem<String>)treeView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            information.setText("Not selected aircompany");
            return;
        }
        String codeAircompany = selectedItem.getValue().split(" ")[1];
        dao.deleteAircompany(Integer.valueOf(codeAircompany));
        printToTreeView();
    }
    @FXML
    public void deleteFlight(ActionEvent event){
        TreeItem<String> selectedItem =(TreeItem<String>)treeView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            information.setText("Not selected aircompany");
            return;
        }
        String codeAircompany = selectedItem.getParent().getValue().split(" ")[1];
        String codeFlight = selectedItem.getValue().split(" ")[0];
        dao.deleteFlight(Integer.valueOf(codeAircompany), Integer.valueOf(codeFlight));
        printToTreeView();
    }

}
