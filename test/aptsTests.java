
import apts.*;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

public class aptsTests {

    @Test
    public void CarShouldTake12MinutesToGateAndBack() {
        var car = new Car("TestCar", 10);
        var passenger = new Passenger("John", new Gate(1000, "A"), 0);
        car.TakeNewPassenger(passenger);
        for (int i = 0; i < 60 * 12; i++) {
            car.Update();
        }
        // assert statements
        assertTrue("hey", car.ticksToGate < 1);
        assertTrue("hey", car.ticksToEntrance < 2);
    }

    @Test
    public void CarShouldNotBeBackAfter10Minutes() {
        var car = new Car("TestCar", 10);
        var passenger = new Passenger("John", new Gate(1000, "A"), 0);
        car.TakeNewPassenger(passenger);
        for (int i = 0; i < 60 * 10; i++) {
            car.Update();
        }
        // assert statements
        assertTrue("hey", car.ticksToGate < 1);
        assertTrue("hey", car.ticksToEntrance > 2);
    }
}
