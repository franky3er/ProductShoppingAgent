package vs.products.refillinfo;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.FileReader;
import java.io.IOException;
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
    public static List<ProductRefillInfo> createProductsRefillInfoFromXML(String xmlSource) throws SAXException, IOException {
        List<ProductRefillInfo> productsRefillInfo = new ArrayList<>();
        XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(new ProductRefillInfoContentHandler(productsRefillInfo));
        xmlReader.parse(new InputSource(new FileReader(xmlSource)));
        return productsRefillInfo;
    }
}
