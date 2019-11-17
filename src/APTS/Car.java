package APTS;

import desmoj.core.simulator.*;
/**
 * The VanCarrier entity encapsulates all data relevant for a van carrier.
 * In our model, it only stores a reference to the truck it is currently
 * (un)loading.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class Car extends Entity {

        public double batteryDistance = 0;
        APTS myModel;
	/**
	 * Constructor of the van carrier entity.
	 *
	 * @param owner the model this entity belongs to
	 * @param name this VC's name
	 * @param showInTrace flag to indicate if this entity shall produce output for the trace
	 */
	public Car(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
                myModel = (APTS) owner;
                batteryDistance = myModel.getBatteryDistance();
	}
}