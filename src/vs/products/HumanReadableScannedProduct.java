package vs.products;

/**
 * Created by franky3er on 18.04.17.
 */
public class HumanReadableScannedProduct extends ScannedProduct {

    public HumanReadableScannedProduct(ScannedProduct scannedProduct) {
        super(scannedProduct.getProduct());
        super.setTimeStamp(scannedProduct.getTimeStamp());
    }

    public String getHumanReadableProductState() {
        return String.format("%s   %s   %s   %s",
                ScannedProduct.SCANNED_PRODUCT_DATE_FORMAT.format(super.getTimeStamp()),
                super.getName(),
                super.getAmmount(),
                super.getUnit());
    }
}
