package APTS;

import desmoj.core.simulator.*;
import java.util.concurrent.TimeUnit;

/**
 * This class represents an entity (and event) source, which continually
 * generates trucks (and their arrival events) in order to keep the simulation
 * running.
 *
 * It will create a new truck, schedule its arrival at the terminal (i.e. create
 * and schedule an arrival event) and then schedule itself for the point in time
 * when the next truck arrival is due.
 *
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class ArrivalPassengerGeneratorEvent extends ExternalEvent {

    private APTS myModel;

    /**
     * Constructs a new TruckGeneratorEvent.
     *
     * @param owner the model this event belongs to
     * @param name this event's name
     * @param showInTrace flag to indicate if this event shall produce output
     * for the trace
     */
    public ArrivalPassengerGeneratorEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        myModel = (APTS) owner;
    }

    /**
     * The eventRoutine() describes the generating of a new truck.
     *
     * It creates a new truck, a new TruckArrivalEvent and schedules itself
     * again for the next new truck generation.
     */
    public void eventRoutine() {
        // get a reference to the model
        APTS model = (APTS) getModel();

        // create a new passenger
        Passenger passenger = new Passenger(model, "Passenger", true, model.getReturnGate());
        // create a new truck arrival event
        ArrivalPassengerEvent returnPassengerArrival = new ArrivalPassengerEvent(model, "PassengerReturnArrivalEvent", true);
        // and schedule it for the current point in time
        returnPassengerArrival.schedule(passenger, new TimeSpan(0.0));

        schedule(new TimeSpan(myModel.getPassengerArrivalTime(), TimeUnit.MINUTES));

    }
}
