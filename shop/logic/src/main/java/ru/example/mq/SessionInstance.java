package ru.example.mq;

import javax.enterprise.inject.spi.Producer;
import javax.jms.*;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

public class SessionInstance {
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String subject = "OrdersRq";

    private static SessionInstance instance;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    private SessionInstance() throws JMSException{
        connectionFactory = new ActiveMQConnectionFactory(url);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(subject);
        producer = session.createProducer(destination);
    }

    public static SessionInstance getInstance() throws JMSException{
        if (instance==null){
            instance = new SessionInstance();
        }
        return instance;
    }

    public Session getSession(){
        return session;
    }
    public MessageProducer getProducer(){
        return producer;
    }
}
