package vs.product.sensor;

import vs.product.Butter;
import vs.product.ScannedProduct;
import vs.product.sensor.scanoption.ScanOption;

public class ButterSensor extends Sensor {

	public ButterSensor(ScanOption scanOption) {
		super(scanOption);
	}

	@Override
	public ScannedProduct scan() {
		System.out.println("INFO : ButterSensor.scan()");
		return super.getScanOption().scan(new Butter());
	}

}
