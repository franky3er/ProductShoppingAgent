package vs.productshoppingagent.main;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import vs.shopservice.ShopService;

/**
 * Created by franky3er on 10.05.17.
 */
public class MainApplication {
    public static void main(String []args) {
        //TODO ..........

        TTransport transport = new TSocket("localhost", 31337);

        try {
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            ShopService.Client client = new ShopService.Client(protocol);

            long preis = client.fetchProductPrice("Milk", "20.0");
            System.out.println(preis);
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }
}
