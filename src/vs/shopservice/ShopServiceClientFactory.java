package vs.shopservice;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Factory to create clients.
 */
public class ShopServiceClientFactory {

    /**
     * Parses the json source to create client-objects from the information within.
     *
     * @param jsonSource
     * @return List<ShopService.Client>
     */
    public static List<ShopService.Client> createClientsFromJSON(String jsonSource) {
        List<ShopService.Client> clients = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(jsonSource));
            JSONArray jsonArray = (JSONArray) jsonObject.get("productShopServers");
            Iterator<JSONObject> iter = jsonArray.iterator();
            while (iter.hasNext()) {
                JSONObject server = iter.next();
                //String name = (String) server.get("name");
                TTransport transport = new TSocket((String) server.get("ipAddr"), (int)((long)((Long) server.get("port"))));
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                clients.add(new ShopService.Client(protocol));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            System.err.println("ERROR : Error in ProductShopServers.json");
            e.printStackTrace();
        }
        return clients;
    }
}
