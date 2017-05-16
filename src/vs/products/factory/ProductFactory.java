package vs.products.factory;

import vs.products.*;

/**
 * This factory produces concrete Product objects.
 */
public class ProductFactory {

    /**
     * Creates a Product object out of the given product name
     *
     * @param productName
     * @return Product
     */
    public static Product build(String productName) {
        System.out.println(String.format("INFO : Create new Product out of product name: %s", productName));
        Product product = null;
        switch (productName) {
            case (Beer.PRODUCT_NAME): {
                product = new Beer();
                break;
            }
            case (Butter.PRODUCT_NAME): {
                product = new Butter();
                break;
            }
            case (Eggs.PRODUCT_NAME): {
                product = new Eggs();
                break;
            }
            case (Milk.PRODUCT_NAME): {
                product = new Milk();
                break;
            }
        }
        return product;
    }
}
