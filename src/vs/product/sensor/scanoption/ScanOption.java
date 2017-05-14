package vs.product.sensor.scanoption;


import vs.product.Product;
import vs.product.ScannedProduct;

public interface ScanOption {
	/**
	 * This method scans a product depending on the implementation.
	 * 
	 * @author franky3er
	 * @param product
	 * @return ScannedProduct
	 */
	ScannedProduct scan(Product product);
}
