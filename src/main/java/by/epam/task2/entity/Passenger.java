package by.epam.task2.entity;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Passenger extends Thread {
    private static Logger logger = LogManager.getLogger();
    private static final int APROX_RIDING_TIME = 10000;
    private BusRoute busRouteNeeded;
    private BusStop boardFrom;
    private BusStop takeOff;
    private boolean onRide;

    public Passenger(BusRoute busRouteNeeded) {
        this.busRouteNeeded = busRouteNeeded;
    }

    public void distributeStopsToPassengers() {
        for (BusStop busStop : busRouteNeeded.getBusStops()) {
            if (new Random().nextBoolean()) {
                this.boardFrom = busStop;
                break;
            } else {
                this.boardFrom = busRouteNeeded.getBusStops().peekFirst();
            }
        }
        for (BusStop busStop : busRouteNeeded.getBusStops()) {
            if (new Random().nextBoolean()) {
                this.takeOff = busStop;
                break;
            } else {
                this.takeOff = busRouteNeeded.getBusStops().peekLast();
            }
        }
        if (takeOff.getBusStopOrderedId() < boardFrom.getBusStopOrderedId()) {
            BusStop busStop = takeOff;
            takeOff = boardFrom;
            boardFrom = busStop;
        } else if (takeOff == boardFrom) {
            onRide = false;
            logger.info("passenger " + "" + this.getName() + " is not going");
        }

    }

    @Override
    public void run() {
        busRouteNeeded.passengerToStartStop(Passenger.this);
        if (takeOff != boardFrom) {
            onRide = true;
        }
        logger.info("passenger " + this.getName());
        logger.info("passenger " + this.getName()
                + " is on stop " + this.getBoardFrom().getName()
                + " goes to " + this.getTakeOff().getName());
        while (onRide) {
            try {
                TimeUnit.MILLISECONDS.sleep(APROX_RIDING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            logger.info("is on destination point " + this.getName());
        }
    }

    public BusRoute getBusRouteNeeded() {
        return busRouteNeeded;
    }

    public void setBusRouteNeeded(BusRoute busRouteNeeded) {
        this.busRouteNeeded = busRouteNeeded;
    }

    public BusStop getBoardFrom() {
        return boardFrom;
    }

    public void setBoardFrom(BusStop boardFrom) {
        this.boardFrom = boardFrom;
    }

    public BusStop getTakeOff() {
        return takeOff;
    }

    public void setTakeOff(BusStop takeOff) {
        this.takeOff = takeOff;
    }

    public boolean isOnRide() {
        return onRide;
    }

    public void setOnRide(boolean onRide) {
        this.onRide = onRide;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Passenger passenger = (Passenger) o;

        if (onRide != passenger.onRide) return false;
        if (busRouteNeeded != null ? !busRouteNeeded.equals(passenger.busRouteNeeded) : passenger.busRouteNeeded != null)
            return false;
        if (boardFrom != null ? !boardFrom.equals(passenger.boardFrom) : passenger.boardFrom != null) return false;
        return takeOff != null ? takeOff.equals(passenger.takeOff) : passenger.takeOff == null;
    }

    @Override
    public int hashCode() {
        int result = busRouteNeeded != null ? busRouteNeeded.hashCode() : 0;
        result = 31 * result + (boardFrom != null ? boardFrom.hashCode() : 0);
        result = 31 * result + (takeOff != null ? takeOff.hashCode() : 0);
        result = 31 * result + (onRide ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Passenger{ " +
                "busRouteNeeded = " + busRouteNeeded +
                ", takeOff = " + takeOff +
                ", boardFrom = " + boardFrom +
                ", onRide = " + onRide +
                '}';
    }
}
