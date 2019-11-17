package APTS;

import desmoj.core.simulator.*;
/**
 * The Truck entity encapsulates all information associated with a truck.
 * Due to the fact that the only thing a truck wants in our model is a single
 * container, our truck has no special attributes.
 * All necessary statistical information are collected by the queue object.
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class Passenger extends Entity {
	/**
	 * Constructor of the truck entity.
	 *
	 * @param owner the model this entity belongs to
	 * @param name this truck's name
	 * @param showInTrace flag to indicate if this entity shall produce output for the trace
	 */
	public Passenger(Model owner, String name, boolean showInTrace, Gate gate) {
		super(owner, name, showInTrace);
                _gate = gate;
	}
        
        public Gate _gate;
}
