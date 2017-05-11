package vs.product;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory to create ProductRefillInfos
 */
public class ProductRefillInfoFactory {
    /**
     * Parses the xml source to create productRefillInfos from the information within.
     *
     * @param xmlSource
     * @return List<ProductRefillInfo>
     */
    public static List<ProductRefillInfo> createProductsRefillInfoFromXML(String xmlSource) {
        List<ProductRefillInfo> productRefillInfos = new ArrayList<>();
        //TODO Parse xml
        //TODO create List<ProductRefillInfo> from parsed xml information
        return productRefillInfos;
    }
}
