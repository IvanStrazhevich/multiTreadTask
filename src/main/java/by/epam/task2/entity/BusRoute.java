package by.epam.task2.entity;

import java.util.ArrayDeque;
import java.util.Queue;

import by.epam.task2.entity.thread.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BusRoute {
    private static Logger logger = LogManager.getLogger();
    private int routeNumber;
    private Queue<BusStop> busStops = new ArrayDeque<>();

    public void moveOnRoute(Bus bus) {
        for (BusStop busStop : busStops) {

            logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " "
                    + bus.getName() + " is moving to bus stop " + busStop.getName());
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " "
                    + bus.getName() + " is arriving to stop " + busStop.getName());
            busStop.arriveOnBusStop(bus);
            busStop.leaveFromBusStop(bus);

        }
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public Queue<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(Queue<BusStop> busStops) {
        this.busStops = busStops;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusRoute busRoute = (BusRoute) o;

        if (routeNumber != busRoute.routeNumber) return false;
        return busStops != null ? busStops.equals(busRoute.busStops) : busRoute.busStops == null;
    }

    @Override
    public int hashCode() {
        int result = routeNumber;
        result = 31 * result + (busStops != null ? busStops.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BusRoute {" +
                " routeNumber = " + routeNumber +
                ", busStops = " + busStops +
                " }";
    }
}
