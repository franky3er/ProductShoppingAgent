package vs.products;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScannedProduct implements Product {
    public final static SimpleDateFormat SCANNED_PRODUCT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Product product;
    private Date timeStamp;

    public ScannedProduct(Product product) {
        this.setProduct(product);
    }

    @Override
    public String getName() {
        return this.getProduct().getName();
    }

    @Override
    public String getUnit() {
        return this.getProduct().getUnit();
    }

    @Override
    public String getStateAsString() {
        return String.format("%s;%s", this.getProduct().getStateAsString(), SCANNED_PRODUCT_DATE_FORMAT.format(this.timeStamp));
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public void setAmmount(String ammount) {
        this.getProduct().setAmmount(ammount);
    }

    @Override
    public String getAmmount() {
        return this.product.getAmmount();
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
