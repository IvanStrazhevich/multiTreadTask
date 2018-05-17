package by.epam.task2.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Bus extends Thread {
    private static Logger logger = LogManager.getLogger();
    private static final int BUS_CAPACITY = 20;
    private BusRoute busRoute;
    private ArrayDeque<Passenger> passengerList = new ArrayDeque<>(BUS_CAPACITY);
    private final ReentrantLock lock = new ReentrantLock();
    private Condition passengerCanTakingOff = lock.newCondition();
    private Condition passengerCanBoarding = lock.newCondition();
    private boolean busOnStop;
    private boolean busIsFull;
    private boolean takeOf;
    private boolean boarding;

    public Bus(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    @Override
    public void run() {
        busRoute.moveOnRoute(Bus.this);
    }

    public void boardOnBus(List<Passenger> passengers) {
        lock.lock();
        try {
            while (takeOf) {
                passengerCanBoarding.await();
            }
            boarding = true;
            passengerList.addAll(passengers);
            boarding = false;
            passengerCanTakingOff.signal();
        } catch (InterruptedException e) {
            logger.log(Level.ERROR, e.getCause());
        } finally {
            lock.unlock();
        }
    }

    public void takeOffBus(ArrayList<Passenger> passengers) {
        lock.lock();
        try {
            while (boarding) {
                passengerCanTakingOff.await();
            }
            takeOf = true;
            passengerList.removeAll(passengers);
            busIsFull = false;
            takeOf = false;
            passengerCanBoarding.signal();

        } catch (InterruptedException e) {
            logger.log(Level.ERROR, e.getCause());
        } finally {
            lock.unlock();
        }
    }

    public ArrayDeque<Passenger> getPassengerArrayDeque() {
        return passengerList;
    }

    public void setPassengerList(ArrayDeque<Passenger> passengerList) {
        this.passengerList = passengerList;
    }

    public boolean isBusOnStop() {
        return busOnStop;
    }

    public void setBusOnStop(boolean busOnStop) {
        this.busOnStop = busOnStop;
    }

    public BusRoute getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    public boolean isBusIsFull() {
        return busIsFull;
    }

    public void setBusIsFull(boolean busIsFull) {
        this.busIsFull = busIsFull;
    }

    public boolean isTakeOf() {
        return takeOf;
    }

    public void setTakeOf(boolean takeOf) {
        this.takeOf = takeOf;
    }

    public boolean isBoarding() {
        return boarding;
    }

    public void setBoarding(boolean boarding) {
        this.boarding = boarding;
    }

    public static int getBusCapacity() {
        return BUS_CAPACITY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bus bus = (Bus) o;

        if (busOnStop != bus.busOnStop) return false;
        if (busIsFull != bus.busIsFull) return false;
        if (takeOf != bus.takeOf) return false;
        if (boarding != bus.boarding) return false;
        if (busRoute != null ? !busRoute.equals(bus.busRoute) : bus.busRoute != null) return false;
        if (passengerList != null ? !passengerList.equals(bus.passengerList) : bus.passengerList != null) return false;
        if (lock != null ? !lock.equals(bus.lock) : bus.lock != null) return false;
        if (passengerCanTakingOff != null ? !passengerCanTakingOff.equals(bus.passengerCanTakingOff) : bus.passengerCanTakingOff != null)
            return false;
        return passengerCanBoarding != null ? passengerCanBoarding.equals(bus.passengerCanBoarding) : bus.passengerCanBoarding == null;
    }

    @Override
    public int hashCode() {
        int result = busRoute != null ? busRoute.hashCode() : 0;
        result = 31 * result + (passengerList != null ? passengerList.hashCode() : 0);
        result = 31 * result + (busOnStop ? 1 : 0);
        result = 31 * result + (lock != null ? lock.hashCode() : 0);
        result = 31 * result + (passengerCanTakingOff != null ? passengerCanTakingOff.hashCode() : 0);
        result = 31 * result + (passengerCanBoarding != null ? passengerCanBoarding.hashCode() : 0);
        result = 31 * result + (busIsFull ? 1 : 0);
        result = 31 * result + (takeOf ? 1 : 0);
        result = 31 * result + (boarding ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bus{" +
                "busRoute=" + busRoute +
                ", busOnStop=" + busOnStop +
                '}';
    }
}


