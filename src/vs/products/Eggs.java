package vs.products;

public class Eggs implements Product {
    public final static String PRODUCT_NAME = "Eggs";
    private int ammount;

    @Override
    public String getName() {
        return PRODUCT_NAME;
    }

    @Override
    public String getUnit() {
        return "Pieces";
    }

    @Override
    public String getStateAsString() {
        return String.format("%s;%s;%s", this.getName(), Integer.toString(this.ammount), this.getUnit());
    }

    @Override
    public String getAmmount() {
        return Integer.toString(ammount);
    }

    public void setAmmount(int ammount) {
        this.ammount = ammount;
    }

    @Override
    public void setAmmount(String ammount) {
        this.setAmmount(Integer.parseInt(ammount));
    }


}