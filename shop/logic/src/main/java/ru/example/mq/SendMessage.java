package ru.example.mq;

import ru.example.common.OrderLineOutbound;
import ru.example.common.OrderOutbound;
import ru.example.domain.OrderLines;
import ru.example.domain.Orders;

import javax.jms.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.ArrayList;

public class SendMessage {
    private Orders order;
    private ArrayList<OrderLines> orderLines;
    private OrderOutbound orderOutbound;

    public SendMessage(Orders order, ArrayList<OrderLines> orderLines) {
        this.order=order;
        this.orderLines=orderLines;
    }

    private void mapOutboundOrder(){
        ArrayList<OrderLineOutbound> linesOutbound = new ArrayList<>();

        if (order == null)
            return;
        orderOutbound = new OrderOutbound();
        orderOutbound.setId(order.getId());
        orderOutbound.setStatus(order.getStatus());
        orderOutbound.setCreateDate(order.getCreateDate());
        orderOutbound.setAmount(order.getAmount());

        for (OrderLines line:orderLines){
            OrderLineOutbound lineOutbound = new OrderLineOutbound();
            lineOutbound.setItemID(line.getOrderLinesPK().getItems().getId());
            lineOutbound.setItemDescr(line.getOrderLinesPK().getItems().getDescr());
            lineOutbound.setUnit(line.getUnit());
            lineOutbound.setCount(line.getCount());
            lineOutbound.setCost(line.getCost());
            linesOutbound.add(lineOutbound);
        }
        if (linesOutbound.size()>0)
        orderOutbound.setOrderLines(linesOutbound);
    }

    private void sendOutboundOrder(String message) throws JMSException {
        Session session = SessionInstance.getInstance().getSession();
        MessageProducer  producer = SessionInstance.getInstance().getProducer();
        TextMessage request = session.createTextMessage(message);
        producer.send(request);
    }

    private String mapToXML() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(orderOutbound.getClass());
        Marshaller jaxbMarsheller = jaxbContext.createMarshaller();
        jaxbMarsheller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);
        jaxbMarsheller.setProperty(Marshaller.JAXB_FRAGMENT,true);
        StringWriter result = new StringWriter();
        jaxbMarsheller.marshal(orderOutbound,result);
        return result.toString();
    }

    public Boolean sendOrder(){
        String message;
        if (order == null)
            return false;

        mapOutboundOrder();
        try {
            message = mapToXML();
        } catch (JAXBException e) {
            return false;
        }

        try {
            sendOutboundOrder(message);
        } catch (JMSException e) {
            return false;
        }
        return true;
    }
}
