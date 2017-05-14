package vs.product.refillinfo;

/**
 * Pojo for all the information needed to refill the fridge
 */
public class ProductRefillInfo {
    private String productName;
    private String productUnit;
    private String amountMinimum;
    private String amountDesired;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUnit() {
        return productUnit;
    }

    public void setProductUnit(String productUnit) {
        this.productUnit = productUnit;
    }

    public String getAmountMinimum() {
        return amountMinimum;
    }

    public void setAmountMinimum(String amountMinimum) {
        this.amountMinimum = amountMinimum;
    }

    public String getAmountDesired() {
        return amountDesired;
    }

    public void setAmountDesired(String amountDesired) {
        this.amountDesired = amountDesired;
    }
}
