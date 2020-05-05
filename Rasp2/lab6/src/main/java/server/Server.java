package server;

import beans.Airport;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import dao.ConcreteDAO;
import dao.DAO;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;


import javax.jms.*;
import java.io.IOException;
import java.sql.SQLException;

public class Server {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String toServer = "TO_SERVER";
    private static String fromServer = "FROM_SERVER";
    MessageConsumer consumer;
    MessageProducer producer;
    Destination destinationToServer;
    Destination destinationFromServer;
    XStream xstream;
    Session session;
    DAO dao;
    public Server() throws SQLException, ClassNotFoundException, JMSException, IOException {
        dao = ConcreteDAO.getInstance();
        xstream = new XStream(new StaxDriver());
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        destinationToServer = session.createQueue(toServer);
        producer = session.createProducer(destinationToServer);
        destinationFromServer = session.createQueue(fromServer);
        consumer = session.createConsumer(destinationFromServer);
        start();
    }
    public void start() throws IOException, JMSException {
        while (true) {
            int code = -1;
            Message message = consumer.receive();
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                code = Integer.valueOf(textMessage.getText());
            }
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
    public void getAll() throws  JMSException {
        Airport airport = dao.getAll();
        TextMessage message = session.createTextMessage(xstream.toXML(airport));
        producer.send(message);

    }
    public void updateAircompany() throws  JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String input = textMessage.getText();
            String[] parts = input.split("#");
            dao.updateAircompany(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), parts[2]);
        }
       else{
           System.out.println("Error");
        }
    }
    public void updateFlight() throws  JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String input = textMessage.getText();
            String[] parts = input.split("#");
            dao.updateFlight(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), Integer.valueOf(parts[2]),
                    parts[3], parts[4]);
        }
        else{
            System.out.println("Error");
        }

    }
    public void addAircompany() throws JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String input = textMessage.getText();
            String[] parts = input.split("#");
            dao.addAircompany(Integer.valueOf(parts[0]), parts[1]);
        }
        else{
            System.out.println("Error");
        }

    }
    public void addFlight() throws JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String input = textMessage.getText();
            String[] parts = input.split("#");
            dao.addFlight(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]), parts[2], parts[3]);
        }
        else{
            System.out.println("Error");
        }

    }
    public void deleteAircompany() throws JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String input = textMessage.getText();
            String[] parts = input.split("#");
            dao.deleteAircompany(Integer.valueOf(parts[0]));
        }
        else{
            System.out.println("Error");
        }
    }

    public void deleteFlight() throws JMSException {
        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            String input = textMessage.getText();
            String[] parts = input.split("#");
            dao.deleteFlight(Integer.valueOf(parts[0]), Integer.valueOf(parts[1]));
        }
        else{
            System.out.println("Error");
        }
       // dao.deleteFlight(airCode, code);
    }
    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException, JMSException {
        Server server = new Server();
    }

}
