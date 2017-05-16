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

import java.io.FileNotFoundException;
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
    public static List<ShopService.Client> createClientsFromJSON(String jsonSource) throws IOException, ParseException, TTransportException {
        List<ShopService.Client> clients = new ArrayList<>();
        Iterator<JSONObject> iter = getJSONObjectIterator(jsonSource);
        while (iter.hasNext()) {
            JSONObject server = iter.next();
            clients.add(createShopServiceServer(server));
        }
        return clients;
    }

    private static Iterator<JSONObject> getJSONObjectIterator(String jsonSource) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(jsonSource));
        JSONArray jsonArray = (JSONArray) jsonObject.get("productShopServers");
        return jsonArray.iterator();
    }

    private static ShopService.Client createShopServiceServer(JSONObject server) throws TTransportException {
        return new ShopService.Client(createShopServiceProtocol(server));
    }

    private static TProtocol createShopServiceProtocol(JSONObject server) throws TTransportException {
        TTransport transport = new TSocket((String) server.get("ipAddr"), (int) ((long) ((Long) server.get("port"))));
        transport.open();
        return new TBinaryProtocol(transport);
    }
}
