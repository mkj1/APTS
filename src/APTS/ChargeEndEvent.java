package APTS;

import APTS.Car;
import desmoj.core.simulator.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the service end event in the EventsExample model. It
 * occurs when a van carrier finishes loading a truck.
 *
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class ChargeEndEvent extends Event<Car> {

    /**
     * A reference to the model this event is a part of. Useful shortcut to
     * access the model's static components
     */
    private APTS myModel;

    /**
     * Constructor of the service end event
     *
     * Used to create a new service end event
     *
     * @param owner the model this event belongs to
     * @param name this event's name
     * @param showInTrace flag to indicate if this event shall produce output
     * for the trace
     */
    public ChargeEndEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        // store a reference to the model this event is associated with
        myModel = (APTS) owner;
    }

    /**
     * This eventRoutine() describes what happens when a van carrier finishes
     * loading a truck.
     *
     * The truck leaves the system. The van carrier will then check if there is
     * another truck waiting for service. If there is another truck waiting it
     * will service it. If not it will wait on its parking spot for the next
     * customer to arrive.
     */
    public void eventRoutine(Car car) {
        sendTraceNote(car + " finished charging.");
        myModel.chargingCarQueue.remove(car);
        car.remainingRange = myModel.getBatteryDistance();

        // check if there are other passengers waiting
        if (!myModel.departurePassengerQueue.isEmpty()) {
            // YES, there is at least one other p waiting

            // remove the first waiting p from the queue
            Passenger nextPassenger = myModel.departurePassengerQueue.first();
            if (car.remainingRange > nextPassenger._gate.distance) {
                
                myModel.departurePassengerQueue.remove(nextPassenger);
                //myModel.queueLength.update(myModel.departurePassengerQueue.length());
                //myModel.returnQueueLength.update(myModel.arrivalPassengerQueue.length());

                // create a new service end event
                ServiceEndEvent event = new ServiceEndEvent(myModel, "ServiceEndEvent", true);
                // and schedule it for the car at the appropriate time

                event.schedule(car, nextPassenger, new TimeSpan(myModel.getServiceTime(nextPassenger._gate.distance), TimeUnit.MINUTES));
            } else {
                myModel.chargingCarQueue.insert(car);
                // create a service end event
                ChargeEndEvent chargeEnd = new ChargeEndEvent(myModel, "ServiceEndEvent", true);
                chargeEnd.schedule(car, new TimeSpan(myModel.getChargeTime(), TimeUnit.MINUTES));
            }

        } else if (!myModel.arrivalPassengerQueue.isEmpty()) {

            // remove the first waiting p from the queue
            Passenger nextPassenger = myModel.arrivalPassengerQueue.first();
            if (car.remainingRange > nextPassenger._gate.distance) {

                myModel.arrivalPassengerQueue.remove(nextPassenger);
                //myModel.queueLength.update(myModel.departurePassengerQueue.length());
                //myModel.returnQueueLength.update(myModel.arrivalPassengerQueue.length());

                // create a new service end event
                ServiceEndEvent event = new ServiceEndEvent(myModel, "ServiceEndEvent", true);
                // and schedule it for the car at the appropriate time

                event.schedule(car, nextPassenger, new TimeSpan(myModel.getServiceTime(nextPassenger._gate.distance), TimeUnit.MINUTES));
            } else {
                myModel.chargingCarQueue.insert(car);
                // create a service end event
                ChargeEndEvent chargeEnd = new ChargeEndEvent(myModel, "ServiceEndEvent", true);
                chargeEnd.schedule(car, new TimeSpan(myModel.getChargeTime(), TimeUnit.MINUTES));
            }

        }  else {
            // NO, there are no passengers waiting

            // --> the van carrier is placed on its parking spot
            myModel.idleCarQueue.insert(car);

            // the VC is now waiting for a new customer to arrive
        }

    }
}
