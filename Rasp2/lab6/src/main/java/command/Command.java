package command;

import beans.Airport;
import client.Client;
import dao.DAO;

import javax.jms.JMSException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Command {
    protected HttpServletRequest req;
    protected HttpServletResponse resp;
    protected DAO dao;
    protected Client client;
    protected Airport airport;


    public void setReq(HttpServletRequest req) {
        this.req = req;
    }

    public void setResp(HttpServletResponse resp) {
        this.resp = resp;
    }

    public void setDao(DAO dao) {
        this.dao = dao;
    }
    public void setClient(Client client){
        this.client = client;
    }
    public void setAirport(Airport airport){
        this.airport = airport;
    }
    public abstract void execute() throws Exception;
}
