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
    private static final int BUS_STOP_PASSENGER_CAPACITY = 20;
    private static final int MAX_TIME_ON_STOP = 1000;
    private static final int WAITING_TIME_BEFORE_STOP = 5;
    private static final ReentrantLock lock = new ReentrantLock();
    private static Logger logger = LogManager.getLogger();
    private Random random = new Random();
    private String name;
    private int busStopOrderedId;
    private ArrayDeque<Bus> busesQueue = new ArrayDeque<>(BUS_STOP_BUS_CAPACITY);
    private Condition busStopIsFull = lock.newCondition();
    private Condition passengerCanTakingOff = lock.newCondition();
    private Condition passengerCanBoarding = lock.newCondition();
    private ArrayDeque<Passenger> passengersOnBusStop = new ArrayDeque<>(BUS_STOP_PASSENGER_CAPACITY);
    private boolean takeOf;
    private boolean boarding;

    public BusStop(String name) {
        this.name = name;
    }

    public BusStop(String name, int busStopOrderedId) {
        this.name = name;
        this.busStopOrderedId = busStopOrderedId;
    }

    public void passengersBoarding() {
        lock.lock();
        Bus bus = busesQueue.peek();
        try {
            for (Passenger passenger : passengersOnBusStop
                    ) {
                while (takeOf) {
                    passengerCanBoarding.await();
                }
                boarding = true;
                if (passenger.getBusRouteNeeded().equals(bus.getBusRoute())
                        && !passenger.getTakeOff().equals(this)
                        && passenger.isOnRide()) {
                    passengersOnBusStop.remove(passenger);
                    bus.boardOnBus(passenger);
                    logger.info("passenger " + passenger.getName()
                            + " is boarded on bus " + bus.getName()
                            + " destination is " + passenger.getTakeOff().getName());
                }
                boarding = false;
                passengerCanTakingOff.signal();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void passengersTakeOff() {
        lock.lock();
        Bus bus = busesQueue.peek();
        try {

            for (Passenger passenger : bus.getPassengerArrayDeque()) {
                while (boarding) {
                    passengerCanTakingOff.await();
                }
                takeOf = true;
                passengerCanTakingOff.signal();
                if (passenger.getTakeOff() == (this)) {
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
                } else if (this == (bus.getBusRoute().getBusStops().getLast())) {
                    bus.takeOffBus(passenger);
                    passengersOnBusStop.add(passenger);
                    logger.info("passenger " + passenger.getName() + " take of on last stop");
                    passenger.setOnRide(false);
                    passenger.setBoardFrom(this);
                }
                takeOf = false;
                passengerCanBoarding.signal();
            }

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
