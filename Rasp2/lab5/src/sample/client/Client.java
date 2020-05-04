package sample.client;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import sample.Airport;

import javax.jms.*;
import java.io.*;
import java.net.Socket;

import static sample.OperationCodes.*;


public class Client {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String toServer = "TO_SERVER";
    private static String fromServer = "FROM_SERVER";
    MessageConsumer consumer;
    MessageProducer producer;
    Destination destinationToServer;
    Destination destinationFromServer;
    XStream xstream;
    Session session;
    public Client() throws JMSException {
        xstream = new XStream(new StaxDriver());
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false,
                Session.AUTO_ACKNOWLEDGE);
        destinationToServer = session.createQueue(toServer);
        consumer = session.createConsumer(destinationToServer);
        destinationFromServer = session.createQueue(fromServer);
        producer = session.createProducer(destinationFromServer);
    }
    public Airport getAll() throws  JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(GET_ALL));
        producer.send(messageToServer);

        Message message = consumer.receive();
        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            return (Airport)xstream.fromXML(textMessage.getText());
        }
        return null;
    }
    public void updateAircompany(int code, int newCode, String newName) throws  JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(UPDATE_AIRCOMPANY));
        producer.send(messageToServer);
        messageToServer = session.createTextMessage(code + "#" + newCode + "#" + newName);
        producer.send(messageToServer);
    }
    public void updateFlight(int airCode, int code, int newCode, String newFrom, String newTo) throws  JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(UPDATE_FLIGHT));
        producer.send(messageToServer);
        messageToServer = session.createTextMessage(airCode + "#" +code + "#" + newCode + "#" + newFrom + "#" + newTo);
        producer.send(messageToServer);
    }
    public void addAircompany(int code, String name) throws  JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(ADD_AIRCOMPANY));
        producer.send(messageToServer);
        messageToServer = session.createTextMessage(code + "#" + name);
        producer.send(messageToServer);
    }

    public void addFlight(int airCode, int code, String from, String to) throws JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(ADD_FLIGHT));
        producer.send(messageToServer);
        messageToServer = session.createTextMessage(airCode + "#" + code + "#" + from + "#" + to);
        producer.send(messageToServer);
    }
    public void deleteAircompany(int code) throws JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(UPDATE_AIRCOMPANY));
        producer.send(messageToServer);
        messageToServer = session.createTextMessage(String.valueOf(code));
        producer.send(messageToServer);
    }
    public void deleteFlight(int airCode, int code) throws JMSException {
        TextMessage messageToServer = session.createTextMessage(String.valueOf(UPDATE_AIRCOMPANY));
        producer.send(messageToServer);
        messageToServer = session.createTextMessage(airCode + " #" + code);
        producer.send(messageToServer);
    }

}
