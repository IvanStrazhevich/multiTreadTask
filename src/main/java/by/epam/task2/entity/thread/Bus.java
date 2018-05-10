package by.epam.task2.entity.thread;

import by.epam.task2.entity.BusRoute;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Bus extends Thread {
    private static Logger logger = LogManager.getLogger();
    private BusRoute busRoute;

    public Bus(BusRoute busRoute) {
        this.busRoute = busRoute;
    }

    @Override
    public void run() {
            busRoute.moveOnRoute(Bus.this);
    }

    public BusRoute getBusRoute() {
        return busRoute;
    }

    public void setBusRoute(BusRoute busRoute) {
        this.busRoute = busRoute;
    }
}


