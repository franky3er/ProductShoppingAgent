package vs.product.sensor;

import vs.product.Milk;
import vs.product.ScannedProduct;
import vs.product.sensor.scanoption.ScanOption;

public class MilkSensor extends Sensor {

	public MilkSensor(ScanOption scanOption) {
		super(scanOption);
	}

	@Override
	public ScannedProduct scan() {
		System.out.println("INFO : MilkSensor.scan()");
		return super.getScanOption().scan(new Milk());
	}
	
}
