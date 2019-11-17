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
public class ReturnPassengerGeneratorEvent extends ExternalEvent {

    private APTS myModel;

    /**
     * Constructs a new TruckGeneratorEvent.
     *
     * @param owner the model this event belongs to
     * @param name this event's name
     * @param showInTrace flag to indicate if this event shall produce output
     * for the trace
     */
    public ReturnPassengerGeneratorEvent(Model owner, String name, boolean showInTrace) {
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
        
        Queue<Passenger> queueRef = null;
        
        switch(myModel.nextArrivalGate){
            case "A":
                queueRef = myModel.passengerQueueA;
                myModel.nextArrivalGate = "B";
                break;
             case "B":
                 queueRef = myModel.passengerQueueB;
                 myModel.nextArrivalGate = "C";
                break;
             case "C":
                 queueRef = myModel.passengerQueueC;
                 myModel.nextArrivalGate = "D";
                break;
             case "D":
                 queueRef = myModel.passengerQueueD;
                 myModel.nextArrivalGate = "F";
                break;
             case "F":
                 queueRef = myModel.passengerQueueF;
                 myModel.nextArrivalGate = "A";
                break;   
        }


        for (int i = 0; i < 5; i++) {
            // create a new passenger
            Passenger passenger = new Passenger(myModel, "Passenger", true, null);
            // p exits a plane and enters the gate ready for pickup
            queueRef.insert(passenger);
        }

        sendTraceNote(queueRef.getName() + " length: " + queueRef.length());

        schedule(new TimeSpan(myModel.getPassengerGateArrivalTime(), TimeUnit.MINUTES));

    }
}
