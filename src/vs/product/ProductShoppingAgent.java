package vs.product;

import vs.product.refillinfo.ProductRefillInfo;
import vs.product.sensor.Sensor;
import vs.shopservice.ShopService;

import java.sql.Connection;
import java.util.List;

/**
 * Class to refill the fridge depending on the refill information of each product
 * and the cheapest price of the specified shops.
 */
public class ProductShoppingAgent {
    private List<Sensor> sensors;
    private List<ShopService.Client> clients;
    private List<ProductRefillInfo> productsRefillInfo;

    public ProductShoppingAgent(List<Sensor> sensors, List<ShopService.Client> clients, List<ProductRefillInfo> productsRefillInfo) {
        this.sensors = sensors;
        this.clients = clients;
        this.productsRefillInfo = productsRefillInfo;
    }

    public void purchase() {
        //TODO iterate through products and check if product needs to be refilled
        //TODO if product needs to be refilled buy the cheapest product offered by the shops
    }
}
