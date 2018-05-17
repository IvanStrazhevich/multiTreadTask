package by.epam.task2.entity;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BusStop {
    private static final int BUS_STOP_BUS_CAPACITY = 3;
    private static final int BUS_STOP_PASSENGER_CAPACITY = 100;
    private static final int MAX_TIME_ON_STOP = 500;
    private static final int WAITING_TIME_BEFORE_STOP = 5;
    private final ReentrantLock lock = new ReentrantLock();
    private static Logger logger = LogManager.getLogger();
    private Condition busStopIsFull = lock.newCondition();
    private Random random = new Random();
    private String name;
    private int busStopOrderedId;
    private ArrayDeque<Bus> busesQueue = new ArrayDeque<>(BUS_STOP_BUS_CAPACITY);
    private ArrayDeque<Passenger> passengersOnBusStop = new ArrayDeque<>(BUS_STOP_PASSENGER_CAPACITY);
    private Condition passengerCanTakingOff = lock.newCondition();
    private Condition passengerCanBoarding = lock.newCondition();
    private boolean takeOf;
    private boolean boarding;

    public BusStop(String name, int busStopOrderedId) {
        this.name = name;
        this.busStopOrderedId = busStopOrderedId;
    }

    public void passengersTakeOff() {
        lock.lock();
        Bus bus = busesQueue.peek();
        ArrayDeque<Passenger> passengersOnBus = bus.getPassengerArrayDeque();
        ArrayList<Passenger> toAddOnStop = new ArrayList<>();
        ArrayList<Passenger> toRemoveFromBus = new ArrayList<>();
        try {
            while (boarding) {
                passengerCanTakingOff.await();
            }
            for (Passenger passenger : passengersOnBus) {
                // if destination bus stop
                if (passenger.getTakeOff().equals(this)) {
                    toRemoveFromBus.add(passenger);
                    passenger.setOnRide(false);
                    toAddOnStop.add(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " on ride is " + passenger.isOnRide() + " is arrived to destination");
                    // if passenger can decide to take of
                } else if (random.nextBoolean()) {
                    toRemoveFromBus.add(passenger);
                    toAddOnStop.add(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " on ride is " + passenger.isOnRide() + " take of on mid stop");
                    // if last stop on route
                } else if (this.equals(bus.getBusRoute().getBusStops().getLast())) {
                    toRemoveFromBus.add(passenger);
                    passenger.setOnRide(false);
                    toAddOnStop.add(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " on ride is " + passenger.isOnRide() + " take of on last stop");
                }
            }
            bus.takeOffBus(toRemoveFromBus);
            passengersOnBusStop.addAll(toAddOnStop);
            takeOf = false;
            passengerCanBoarding.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void passengersBoarding() {
        lock.lock();
        Bus bus = busesQueue.peek();
        ArrayList<Passenger> toAddOnBus = new ArrayList<>();
        try {
            while (takeOf) {
                passengerCanBoarding.await();
            }
            for (Passenger passenger : passengersOnBusStop
                    ) {
                if (passenger.getBusRouteNeeded().equals(bus.getBusRoute())
                        && !passenger.getTakeOff().equals(this)
                        && passenger.isOnRide()) {
                    toAddOnBus.add(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " boarding on " + bus.getBusRoute().getRouteNumber() + " " + bus.getName()
                            + " destination " + passenger.getTakeOff().getName());
                } else if (random.nextBoolean()
                        && passenger.isOnRide()
                        && passenger.getBusRouteNeeded().equals(bus.getBusRoute())) {
                    toAddOnBus.add(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " boarding on " + bus.getBusRoute().getRouteNumber() + " " + bus.getName()
                            + " destination " + passenger.getTakeOff().getName());
                }
            }
            passengersOnBusStop.removeAll(toAddOnBus);
            int busCanTake = Bus.getBusCapacity() - bus.getPassengerArrayDeque().size();
            int passengersToAddOnBus = toAddOnBus.size();
            if (busCanTake < passengersToAddOnBus) {
                logger.log(Level.INFO, (passengersToAddOnBus - busCanTake) + " bus is overloaded with");
                bus.setBusIsFull(true);
                List<Passenger> trimmedToAddOnBus = toAddOnBus.subList(0, busCanTake - 1);
                logger.log(Level.INFO, trimmedToAddOnBus.size());
                List<Passenger> backToStop = toAddOnBus.subList(busCanTake, passengersToAddOnBus);
                logger.log(Level.INFO, backToStop.size());
                bus.boardOnBus(trimmedToAddOnBus);
                passengersOnBusStop.addAll(backToStop);
            } else {
                bus.boardOnBus(toAddOnBus);
            }
            boarding = false;
            passengerCanTakingOff.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


    public void arriveOnBusStop(Bus bus) {
        try {
            lock.lock();
            while (!(busesQueue.size() < BUS_STOP_BUS_CAPACITY)) {
                busStopIsFull.await(WAITING_TIME_BEFORE_STOP, TimeUnit.MILLISECONDS);
            }
            busStopIsFull.signal();
            busesQueue.add(bus);
            bus.setBusOnStop(true);
            logger.info("bus number " + bus.getBusRoute().getRouteNumber()
                    + " " + bus.getName() + " is on bus stop " + bus.isBusOnStop() + " " + this.getName());
        } catch (InterruptedException e) {
            logger.error("Arrive on BusStop problem", e);
        } finally {
            lock.unlock();
        }
        try {
            TimeUnit.MILLISECONDS.sleep(random.nextInt(MAX_TIME_ON_STOP));
        } catch (InterruptedException e) {
            logger.error("Arrive on BusStop problem", e);
        }
    }

    public void leaveFromBusStop(Bus bus) {
        try {
            lock.lock();
            logger.info("bus number " + bus.getBusRoute().getRouteNumber()
                    + " " + bus.getName() + " is leaving bus stop " + this.getName() + " " + bus.getName());
            logger.info("buses on stop before bus leaving " + " " + this.toString() + " " + busesQueue.size());
            busesQueue.poll();
            bus.setBusOnStop(false);
        } finally {
            lock.unlock();
        }
    }

    public int getBusStopOrderedId() {
        return busStopOrderedId;
    }

    public void setBusStopOrderedId(int busStopOrderedId) {
        this.busStopOrderedId = busStopOrderedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Queue<Bus> getBusesQueue() {
        return busesQueue;
    }

    public void setBusesQueue(ArrayDeque<Bus> busesQueue) {
        this.busesQueue = busesQueue;
    }

    public ArrayDeque<Passenger> getPassengersOnBusStop() {
        return passengersOnBusStop;
    }

    public void setPassengersOnBusStop(ArrayDeque<Passenger> passengersOnBusStop) {
        this.passengersOnBusStop = passengersOnBusStop;
    }

    public ReentrantLock getLock() {
        return lock;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusStop busStop = (BusStop) o;

        if (busStopOrderedId != busStop.busStopOrderedId) return false;
        if (takeOf != busStop.takeOf) return false;
        if (boarding != busStop.boarding) return false;
        if (lock != null ? !lock.equals(busStop.lock) : busStop.lock != null) return false;
        if (random != null ? !random.equals(busStop.random) : busStop.random != null) return false;
        if (name != null ? !name.equals(busStop.name) : busStop.name != null) return false;
        if (busesQueue != null ? !busesQueue.equals(busStop.busesQueue) : busStop.busesQueue != null) return false;
        if (busStopIsFull != null ? !busStopIsFull.equals(busStop.busStopIsFull) : busStop.busStopIsFull != null)
            return false;
        if (passengerCanTakingOff != null ? !passengerCanTakingOff.equals(busStop.passengerCanTakingOff) : busStop.passengerCanTakingOff != null)
            return false;
        if (passengerCanBoarding != null ? !passengerCanBoarding.equals(busStop.passengerCanBoarding) : busStop.passengerCanBoarding != null)
            return false;
        return passengersOnBusStop != null ? passengersOnBusStop.equals(busStop.passengersOnBusStop) : busStop.passengersOnBusStop == null;
    }

    @Override
    public int hashCode() {
        int result = lock != null ? lock.hashCode() : 0;
        result = 31 * result + (random != null ? random.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + busStopOrderedId;
        result = 31 * result + (busesQueue != null ? busesQueue.hashCode() : 0);
        result = 31 * result + (busStopIsFull != null ? busStopIsFull.hashCode() : 0);
        result = 31 * result + (passengerCanTakingOff != null ? passengerCanTakingOff.hashCode() : 0);
        result = 31 * result + (passengerCanBoarding != null ? passengerCanBoarding.hashCode() : 0);
        result = 31 * result + (passengersOnBusStop != null ? passengersOnBusStop.hashCode() : 0);
        result = 31 * result + (takeOf ? 1 : 0);
        result = 31 * result + (boarding ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BusStop{ " +
                "name = '" + name + '\'' +
                ", busStopOrderedId = " + busStopOrderedId +
                '}';
    }
}
