package ru.example.service;

import ru.example.common.OrderInbound;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.WebEndpoint;

@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface OrderService extends java.rmi.Remote{

    @WebMethod
    public long createOrder(@WebParam(name = "OrderLines") OrderInbound orderlinesInbound) throws java.rmi.RemoteException;

    @WebMethod
    public String testBean();
}
