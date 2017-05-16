package vs.products;

public class Milk implements Product {
    public final static String PRODUCT_NAME = "Milk";

    private double ammount;

    @Override
    public String getName() {
        return PRODUCT_NAME;
    }

    @Override
    public String getUnit() {
        return "Liter";
    }

    @Override
    public String getStateAsString() {
        return String.format("%s;%s;%s", this.getName(), Double.toString(this.ammount), getUnit());
    }

    @Override
    public String getAmmount() {
        return Double.toString(ammount);
    }

    public void setAmmount(double ammount) {
        this.ammount = ammount;
    }

    @Override
    public void setAmmount(String ammount) {
        this.setAmmount(Double.parseDouble(ammount));
    }
}