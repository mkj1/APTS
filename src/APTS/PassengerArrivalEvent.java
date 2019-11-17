package APTS;

import APTS.Car;
import desmoj.core.simulator.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the truck arrival event in the EventsExample model. It
 * occurs when a truck arrives at the terminal to request loading of a
 * container.
 *
 * @author Olaf Neidhardt, Ruth Meyer
 */
public class PassengerArrivalEvent extends Event<Passenger> {

    /**
     * a reference to the model this event is a part of. Useful shortcut to
     * access the model's static components
     */
    private APTS myModel;

    /**
     * Constructor of the truck arrival event
     *
     * Used to create a new truck arrival event
     *
     * @param owner the model this event belongs to
     * @param name this event's name
     * @param showInTrace flag to indicate if this event shall produce output
     * for the trace
     */
    public PassengerArrivalEvent(Model owner, String name, boolean showInTrace) {
        super(owner, name, showInTrace);
        // store a reference to the model this event is associated with
        myModel = (APTS) owner;
    }

    /**
     * This eventRoutine() describes what happens when a truck enters the
     * terminal.
     *
     * On arrival, the truck will enter the queue (parking lot). It will then
     * check if the van carrier is available. If this is the case, it will
     * occupy the van carrier and schedule a service end event. Otherwise the
     * truck just waits (does nothing).
     */
    public void eventRoutine(Passenger p) {

        // p enters airport entrance
        myModel.passengerQueue.insert(p);
        sendTraceNote("PassengerQueueLength: " + myModel.passengerQueue.length());

        // check if a car is available
        if (!myModel.idleCarQueue.isEmpty()) {
            // yes, it is

            // get a reference to the first car from the idle car queue
            Car car = myModel.idleCarQueue.first();
            if (car.batteryDistance > 2 * p._gate.distance) {
                // remove it from the queue
                myModel.idleCarQueue.remove(car);

                // remove the p from the queue
                myModel.queueLength.update(myModel.passengerQueue.length());
                myModel.passengerQueue.remove(p);

                // create a service end event
                ServiceEndEvent serviceEnd = new ServiceEndEvent(myModel, "ServiceEndEvent", true);
                Random rand = new Random();
                var r = rand.nextInt(5);
                // and place it on the event list

                serviceEnd.schedule(car, p, new TimeSpan(myModel.getServiceTime(p._gate.distance), TimeUnit.MINUTES));
            }
            else{
                myModel.idleCarQueue.remove(car);
                myModel.chargingCarQueue.insert(car);
                // create a service end event
                ChargeEndEvent chargeEnd = new ChargeEndEvent(myModel, "ServiceEndEvent", true);
                chargeEnd.schedule(car, new TimeSpan(myModel.getChargeTime(), TimeUnit.MINUTES));
            }

        }

    }
}
