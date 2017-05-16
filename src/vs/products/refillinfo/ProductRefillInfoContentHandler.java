package vs.products.refillinfo;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

import java.util.List;

/**
 * Sax ContentHandler to parse every product refill information from the xml source
 */
public class ProductRefillInfoContentHandler implements ContentHandler {
    private List<ProductRefillInfo> productsRefillInfo;
    private ProductRefillInfo currentProductRefillInfo;
    private String currentValue;

    public ProductRefillInfoContentHandler(List<ProductRefillInfo> productsRefillInfo){
        this.productsRefillInfo = productsRefillInfo;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        this.currentValue = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("name")){
            this.currentProductRefillInfo.setProductName(currentValue);
        }

        if(localName.equals("unit")){
            this.currentProductRefillInfo.setProductUnit(currentValue);
        }

        if(localName.equals("amountMin")) {
            this.currentProductRefillInfo.setAmountMinimum(currentValue);
        }

        if(localName.equals("amountDesired")) {
            this.currentProductRefillInfo.setAmountDesired(currentValue);
        }

        if(localName.equals("product")){
            this.productsRefillInfo.add(this.currentProductRefillInfo);
        }
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if(localName.equals("product")) {
            this.currentProductRefillInfo = new ProductRefillInfo();
        }
    }


    public void endDocument() throws SAXException {}
    public void endPrefixMapping(String prefix) throws SAXException {}
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {}
    public void processingInstruction(String target, String data) throws SAXException {}
    public void setDocumentLocator(Locator locator) {  }
    public void skippedEntity(String name) throws SAXException {}
    public void startDocument() throws SAXException {}
    public void startPrefixMapping(String prefix, String uri) throws SAXException {}
}
