package vs.product.sensor;


import vs.product.Beer;
import vs.product.ScannedProduct;
import vs.product.sensor.scanoption.ScanOption;

public class BeerSensor extends Sensor {

	public BeerSensor(ScanOption scanOption) {
		super(scanOption);
	}

	@Override
	public ScannedProduct scan() {
		System.out.println("INFO : BeerSensor.scan()");
		return super.getScanOption().scan(new Beer());
	}
	
	

}
