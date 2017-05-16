package vs.products.iohandler;

import vs.products.ScannedProduct;

import java.util.List;

/**
 * This interface provides two methods to either write a ScannedProduct into a persistent object
 * or read a list of ScannedProduct's from a persistent object
 */
public interface ProductIOHandler {
    /**
     * Writes the given ScannedProduct into the concrete persistent object.
     *
     * @param scannedProduct
     */
    void write(ScannedProduct scannedProduct);

    /**
     * Reads a list of ScannedProduct's from a concrete persistent object.
     *
     * @param statement
     * @return List<ScannedProduct>
     */
    List<ScannedProduct> read(String statement);
}
