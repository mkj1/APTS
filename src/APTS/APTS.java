package APTS;

import desmoj.core.simulator.*;
import desmoj.core.dist.*;
import desmoj.core.statistic.*;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class APTS extends Model {

    protected static int NUM_CARS = 6;

    protected static Gate GATE_A = new Gate("A", 720);
    protected static Gate GATE_B = new Gate("B", 660);
    protected static Gate GATE_C = new Gate("C", 690);
    protected static Gate GATE_D = new Gate("D", 320);
    protected static Gate GATE_F = new Gate("F", 760);
    protected static Gate GATE_RETURN = new Gate("RETURN", 600);

    protected String nextArrivalGate = "A";
    private ContDistUniform remainingRange;
    private ContDistUniform chargeTime;
    
    protected TimeSeries queueLength;
    protected TimeSeries returnQueueLength;

    /**
     * Random number stream used to draw an arrival time for the next truck. See
     * init() method for stream parameters.
     */
    private ContDistExponential passengerDepartureTime;
    private ContDistExponential passengerArrivalTime;
    /**
     * Random number stream used to draw a service time for a truck. Describes
     * the time needed by the VC to fetch and load the container onto the truck.
     * See init() method for stream parameters.
     */

    private ContDistUniform carSpeed;
    /**
     * A waiting queue object is used to represent the parking area for the
     * trucks. Every time a truck arrives it is inserted into this queue (it
     * parks) and will be removed by the VC for service.
     *
     * This way all necessary basic statistics are monitored by the queue.
     */
    protected Queue<Passenger> departurePassengerQueue;

    protected Queue<Passenger> arrivalPassengerQueue;

    /**
     * A waiting queue object is used to represent the parking spot for the VC.
     * If there is no truck waiting for service the VC will return here and wait
     * for the next truck to come.
     *
     * This way all idle time statistics of the VC are monitored by the queue.
     */
    protected Queue<Car> idleCarQueue;
    protected Queue<Car> chargingCarQueue;

    /**
     * EventsExample constructor.
     *
     * Creates a new EventsExample model via calling the constructor of the
     * superclass.
     *
     * @param owner the model this model is part of (set to <tt>null</tt> when
     * there is no such model)
     * @param modelName this model's name
     * @param showInReport flag to indicate if this model shall produce output
     * to the report file
     * @param showInTrace flag to indicate if this model shall produce output to
     * the trace file
     */
    public APTS(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
        super(owner, modelName, showInReport, showInTrace);
    }

    /**
     * Returns a description of the model to be used in the report.
     *
     * @return model description as a string
     */
    public String description() {
        return "This model describes a queueing system located at an "
                + "airport. Passengers will arrive and "
                + "require the loading into a car. A car is "
                + "on duty and will head off to find the passenger. "
                + "It will then load the passenger into the "
                + "car. Afterwards, the car leaves the entrance. "
                + "In case the car is busy, the passenger waits "
                + "for its turn at the airport entrance. "
                + "If the car is idle, it waits on its own parking spot for a "
                + "passenger to come.";
    }

    /**
     * Activates dynamic model components (events).
     *
     * This method is used to place all events or processes on the internal
     * event list of the simulator which are necessary to start the simulation.
     *
     * In this case, the truck generator event will have to be created and
     * scheduled for the start time of the simulation.
     */
    public void doInitialSchedules() {

        // create the DeparturePassengerGeneratorEvent
        DeparturePassengerGeneratorEvent passengerGenerator
                = new DeparturePassengerGeneratorEvent(this, "PassengerGenerator", true);

        // create the ArrivalPassengerGeneratorEvent
        ArrivalPassengerGeneratorEvent returnPassengerGenerator
                = new ArrivalPassengerGeneratorEvent(this, "ReturnPassengerGenerator", true);
        
        // create the TimeSeriesGeneratorEvent
        TimeSeriesGeneratorEvent seriesGenerator
                = new TimeSeriesGeneratorEvent(this, "TimeSeriesGenerator", true);

        // schedule for start of simulation
        passengerGenerator.schedule(new TimeSpan(0));
        // schedule for start of simulation
        returnPassengerGenerator.schedule(new TimeSpan(0));
        
        seriesGenerator.schedule(new TimeSpan(0));
    }

    public Gate getRandomGate() {
        Random rand = new Random();
        var r = rand.nextInt(5);
        Gate g = null;

        switch (r) {
            case 0:
                g = GATE_A;
                break;
            case 1:
                g = GATE_B;
                break;
            case 2:
                g = GATE_C;
                break;
            case 3:
                g = GATE_D;
                break;
            case 4:
                g = GATE_F;
                break;
        }
        return g;
    }
    
    public Gate getReturnGate(){
        return GATE_RETURN;
    }

    /**
     * Initialises static model components like distributions and queues.
     */
    public void init() {

        carSpeed = new ContDistUniform(this, "CarSpeedStream", 8.0, 9.0, true, false);
        remainingRange = new ContDistUniform(this, "BatteryDistanceStream", 48000, 50000, true, false);
        chargeTime = new ContDistUniform(this, "ChargeTimeStream", 4, 5, true, false);

        // initalise the truckArrivalTimeStream
        // Parameters:
        // this                     = belongs to this model
        // "TruckArrivalTimeStream" = the name of the stream
        // 3.0                      = mean time in minutes between arrival of trucks
        // true                     = show in report?
        // false                    = show in trace?
        passengerDepartureTime = new ContDistExponential(this, "PassengerDepartureTimeStream", 3.480, true, false);

        // necessary because an inter-arrival time can not be negative, but
        // a sample of an exponential distribution can...
        passengerDepartureTime.setNonNegative(true);

        passengerArrivalTime = new ContDistExponential(this, "PassengerArrivalTimeStream", 3.459, true, false);
        passengerArrivalTime.setNonNegative(true);

        // initalise the departurePassengerQueue
        // Parameters:
        // this          = belongs to this model
        // "Truck Queue" = the name of the Queue
        // true          = show in report?
        // true          = show in trace?
        departurePassengerQueue = new Queue<Passenger>(this, "Passenger Queue", true, true);

        arrivalPassengerQueue = new Queue<Passenger>(this, "Passenger Return Queue", true, true);

        // initalise the idleCarQueue
        // Parameters:
        // this            = belongs to this model
        // "idle Car Queue" = the name of the Queue
        // true            = show in report?
        // true            = show in trace?
        idleCarQueue = new Queue<Car>(this, "idle Car Queue", true, true);
        chargingCarQueue = new Queue<Car>(this, "charging Car Queue", true, true);
        
        queueLength = new TimeSeries(this,"queue length","QueueLength.txt",new TimeInstant(0.0),new TimeInstant(86400.0),true,true);
        returnQueueLength = new TimeSeries(this,"return queue length","ReturnQueueLength.txt",new TimeInstant(0.0),new TimeInstant(86400.0),true,true);

        // place the van carriers into the idle queue
        // We don't do this in the doInitialSchedules() method because
        // we aren't placing anything on the event list here.
        Car Car;
        for (int i = 0; i < NUM_CARS; i++) {
            // create a new VanCarrier
            Car = new Car(this, "Car", true);
            // put it on his parking spot
            idleCarQueue.insert(Car);
        }
    }

    /**
     * Returns a sample of the random stream used to determine the time needed
     * to fetch the container for a truck from the storage area and the time the
     * Car needs to load it onto the truck.
     *
     * @param gateDistance
     * @return double a serviceTime sample
     */
    public double getServiceTime(double gateDistance) {
        var time = (((gateDistance / getCarSpeed()) / 1000) * 60) * 2;

        return time;
    }

    public double getCarSpeed() {
        return carSpeed.sample();
    }

    public double getBatteryDistance() {
        return remainingRange.sample();
    }

    public double getChargeTime() {
        return chargeTime.sample();
    }

    /**
     * Returns a sample of the random stream used to determine the next truck
     * arrival time.
     *
     * @return double a passengerArrivalTime sample
     */
    public double getPassengerDepartureTime() {
        return passengerDepartureTime.sample();
    }

    public double getPassengerArrivalTime() {
        return passengerArrivalTime.sample();
    }
    

    /**
     * Runs the model.
     *
     * In DESMO-J used to - instantiate the experiment - instantiate the model -
     * connect the model to the experiment - steer length of simulation and
     * outputs - set the ending criterion (normally the time) - start the
     * simulation - initiate reporting - clean up the experiment
     *
     * @param args is an array of command-line arguments (will be ignored here)
     */
    public static void main(java.lang.String[] args) {

        // create model and experiment
        APTS model = new APTS(null, "APTS", true, true);
        // null as first parameter because it is the main model and has no mastermodel

        Experiment exp = new Experiment("APTSExperiment", TimeUnit.SECONDS, TimeUnit.MINUTES, null);
        // ATTENTION, since the name of the experiment is used in the names of the
        // output files, you have to specify a string that's compatible with the
        // filename constraints of your computer's operating system. The remaing three
        // parameters specify the granularity of simulation time, default unit to
        // display time and the time formatter to use (null yields a default formatter).
        // connect both
        model.connectToExperiment(exp);

        // set experiment parameters
        exp.setShowProgressBar(true);  // display a progress bar (or not)
        exp.stop(new TimeInstant(1500, TimeUnit.MINUTES));   // set end of simulation at x minutes
        exp.tracePeriod(new TimeInstant(0), new TimeInstant(1000, TimeUnit.MINUTES));  // set the period of the trace
        exp.debugPeriod(new TimeInstant(0), new TimeInstant(50, TimeUnit.MINUTES));   // and debug output
        // ATTENTION!
        // Don't use too long periods. Otherwise a huge HTML page will
        // be created which crashes Netscape :-)

        // start the experiment at simulation time 0.0
        exp.start();

        // --> now the simulation is running until it reaches its end criterion
        // ...
        // ...
        // <-- afterwards, the main thread returns here
        // generate the report (and other output files)
        exp.report();

        // stop all threads still alive and close all output files
        exp.finish();
    }
}
/* end of model class */
