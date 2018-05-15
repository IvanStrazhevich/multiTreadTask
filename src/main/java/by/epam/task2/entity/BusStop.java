package by.epam.task2.entity;

import by.epam.task2.entity.thread.Bus;
import by.epam.task2.entity.thread.Passenger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BusStop {
    private static final int BUS_STOP_BUS_CAPACITY = 2;
    private static final int BUS_STOP_PASSENGER_CAPACITY = 2;
    private static final int MAX_TIME_ON_STOP = 500;
    private static final int WAITING_TIME_BEFORE_STOP = 5;
    private static Logger logger = LogManager.getLogger();
    private Random random = new Random();
    private String name;
    private int busStopOrderedId;
    private ArrayDeque<Bus> busesQueue = new ArrayDeque<>(BUS_STOP_BUS_CAPACITY);
    private ReentrantLock lock = new ReentrantLock();
    private Condition busStopIsFull = lock.newCondition();
    private ArrayDeque<Passenger> passengersOnBusStop = new ArrayDeque<>(BUS_STOP_PASSENGER_CAPACITY);

    public BusStop(String name) {
        this.name = name;
    }

    public BusStop(String name, int busStopOrderedId) {
        this.name = name;
        this.busStopOrderedId = busStopOrderedId;
    }

    public void passengersBoarding() {
        Bus bus = busesQueue.peek();
        try {
            lock.lock();
            for (Passenger passenger : passengersOnBusStop
                    ) {
                if (passenger.getBusRouteNeeded().equals(bus.getBusRoute())
                        && !passenger.getTakeOff().equals(this)
                        && passenger.isOnRide()
                        && new Random().nextBoolean()) {
                    passengersOnBusStop.remove(passenger);
                    bus.boardOnBus(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " is boarded on bus " + bus.getName()
                            + " destination is " + passenger.getTakeOff().getName());
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void passengersTakeOff() {
        Bus bus = busesQueue.peek();
        try {
            lock.lock();
            for (Passenger passenger : bus.getPassengerArrayDeque()) {
                if (passenger.getTakeOff().equals(this)
                        && random.nextBoolean()) {
                    bus.takeOffBus(passenger);
                    passengersOnBusStop.add(passenger);
                    passenger.setOnRide(false);
                    logger.info("passenger " + passenger.getName()
                            + " on ride is " + passenger.isOnRide() + " is arrived to destination");
                    passengersOnBusStop.remove(passenger);
                } else if (random.nextBoolean()) {
                    bus.takeOffBus(passenger);
                    passengersOnBusStop.add(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " on ride is " + passenger.isOnRide() + " take of on mid stop");
                    passenger.setBoardFrom(this);
                } else if (this.equals(bus.getBusRoute().getBusStops().getLast())) {
                    bus.takeOffBus(passenger);
                    passengersOnBusStop.add(passenger);
                    logger.info("passenger " + passenger.getName() + " take of on last stop");
                    passenger.setOnRide(false);
                    passenger.setBoardFrom(this);
                }
            }
        } finally {
            lock.unlock();
        }
    }

    public void arriveOnBusStop(Bus bus) {
        try {
            lock.lock();
            while (!(busesQueue.size() < BUS_STOP_BUS_CAPACITY)) {
                busStopIsFull.await(WAITING_TIME_BEFORE_STOP,TimeUnit.MILLISECONDS);
            }
            busStopIsFull.signal();
            busesQueue.add(bus);
            bus.setBusOnStop(true);
            logger.info("bus number " + bus.getBusRoute().getRouteNumber()
                    + " " + bus.getName() + " is on bus stop " + bus.isBusOnStop() + " " + this.getName());
            TimeUnit.MILLISECONDS.sleep(random.nextInt(MAX_TIME_ON_STOP));
        } catch (InterruptedException e) {
            logger.error("Arrive on BusStop problem", e);
        } finally {
            lock.unlock();
        }
    }

    public void leaveFromBusStop(Bus bus) {
        try {
            lock.lock();
            logger.info("bus number " + bus.getBusRoute().getRouteNumber()
                    + " " + bus.getName() + " is leaving bus stop " + this.getName() + " " + bus.getName());
            logger.info("buses on stop before bus leaving " + " " + this.toString() + " " + busesQueue.size());
            busesQueue.poll();
            logger.info("buses on stop after bus leaving " + " " + this.toString() + " " + busesQueue.size());
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

    public void setLock(ReentrantLock lock) {
        this.lock = lock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusStop busStop = (BusStop) o;

        if (busStopOrderedId != busStop.busStopOrderedId) return false;
        if (name != null ? !name.equals(busStop.name) : busStop.name != null) return false;
        if (busesQueue != null ? !busesQueue.equals(busStop.busesQueue) : busStop.busesQueue != null) return false;
        if (lock != null ? !lock.equals(busStop.lock) : busStop.lock != null) return false;
        return passengersOnBusStop != null ? passengersOnBusStop.equals(busStop.passengersOnBusStop) : busStop.passengersOnBusStop == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + busStopOrderedId;
        result = 31 * result + (busesQueue != null ? busesQueue.hashCode() : 0);
        result = 31 * result + (lock != null ? lock.hashCode() : 0);
        result = 31 * result + (passengersOnBusStop != null ? passengersOnBusStop.hashCode() : 0);
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
