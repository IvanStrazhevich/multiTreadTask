package by.epam.task2.entity.thread;

import by.epam.task2.entity.BusRoute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;

public class Bus extends Thread {
    private static Logger logger = LogManager.getLogger();
    private BusRoute busRoute;
    private ArrayDeque<Passenger> passengerList = new ArrayDeque<>(20);
    private boolean busOnStop;

    public Bus(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    @Override
    public void run() {
        busRoute.moveOnRoute(Bus.this);
    }

    public void boardOnBus(Passenger passenger) {
            passengerList.add(passenger);
    }

    public void takeOffBus(Passenger passenger) {
            passengerList.remove(passenger);
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
}


