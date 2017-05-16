package vs.products;

public class Butter implements Product {
    public final static String PRODUCT_NAME = "Butter";
    private double ammount;

    @Override
    public String getName() {
        return PRODUCT_NAME;
    }

    @Override
    public String getUnit() {
        return "Gramms";
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