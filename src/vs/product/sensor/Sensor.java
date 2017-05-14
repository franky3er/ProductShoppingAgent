package vs.product.sensor;

import vs.product.ScannedProduct;
import vs.product.sensor.scanoption.ScanOption;

public abstract class Sensor {
	private ScanOption scanOption;
	
	public Sensor(ScanOption scanOption){
		this.setScanOption(scanOption);
	}
	
	public ScanOption getScanOption() {
		return scanOption;
	}
	public void setScanOption(ScanOption scanOption) {
		this.scanOption = scanOption;
	}
	
	/**
	 * Calls the scan() method from the depending ScanOption implementation and returns a scanned product.
	 * Therefore it depends on the implementation of this class which Product is given as a parameter
	 * to the scan() method of the ScanOption.
	 * 
	 * @author franky3er
	 * @return ScannedProduct
	 */
	public abstract ScannedProduct scan();
}
