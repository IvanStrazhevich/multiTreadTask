package by.epam.task2.entity;

import java.util.ArrayDeque;

import by.epam.task2.entity.thread.Bus;
import by.epam.task2.entity.thread.Passenger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class BusRoute {
    private static Logger logger = LogManager.getLogger();
    private static final int MAX_TIME_BETWIN_STOPS = 1000;
    private int routeNumber;
    private ArrayDeque<BusStop> busStops = new ArrayDeque<>();

    public BusRoute() {

    }

    public void moveOnRoute(Bus bus) {
        for (BusStop busStop : busStops) {
            logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " "
                    + bus.getName() + " is moving to bus stop " + busStop.getName());
            try {
                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(MAX_TIME_BETWIN_STOPS));
            } catch (InterruptedException e) {
                logger.error(e);
            }
            logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " "
                    + bus.getName() + " is arriving to stop " + busStop.getName());

            busStop.arriveOnBusStop(bus);
            if (busStop.equals(bus.getBusRoute().getBusStops().getLast())) {
                logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " "
                        + bus.getName() + " is finished it's ride ");
                busStop.passengersTakeOff();
                logger.info("bus number " + bus.getBusRoute().getRouteNumber()
                        + " " + bus.getName()
                        + " has left " + bus.getPassengerArrayDeque().size() + " passengers on board");
                busStop.leaveFromBusStop(bus);
            } else {
                busStop.passengersTakeOff();
                busStop.passengersBoarding();
                busStop.leaveFromBusStop(bus);
            }
        }
    }

    public void passengerToStartStop(Passenger passenger) {
        for (BusStop busStop : busStops
                ) {
            if (passenger.getBoardFrom() != passenger.getTakeOff()
                    && passenger.getBoardFrom().equals(busStop)) {
                passenger.setOnRide(true);
                busStop.getPassengersOnBusStop().add(passenger);

            }
        }
    }

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public ArrayDeque<BusStop> getBusStops() {
        return busStops;
    }

    public void setBusStops(ArrayDeque<BusStop> busStops) {
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
        return getClass().getName() + "{" +
                " routeNumber = " + routeNumber +
                ", busStops = " + busStops +
                " }";
    }
}
