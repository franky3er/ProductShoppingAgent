package vs.product.sensor;


import vs.product.Eggs;
import vs.product.ScannedProduct;
import vs.product.sensor.scanoption.ScanOption;

public class EggsSensor extends Sensor {

	public EggsSensor(ScanOption scanOption) {
		super(scanOption);
	}

	@Override
	public ScannedProduct scan() {
		System.out.println("INFO : ButterSensor.scan()");
		return super.getScanOption().scan(new Eggs());
	}

}
