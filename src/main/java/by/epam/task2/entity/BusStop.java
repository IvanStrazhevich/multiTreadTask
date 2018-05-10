package by.epam.task2.entity;

import by.epam.task2.entity.thread.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class BusStop {
    private static Logger logger = LogManager.getLogger();
    private String name;
    private Queue<Bus> busesQueue = new ArrayDeque<>(2);
    private ReentrantLock lock = new ReentrantLock();

    public BusStop(String name) {
        this.name = name;
    }

    public void arriveOnBusStop(Bus bus) {
        try {
            while (!lock.tryLock()) {
                TimeUnit.MILLISECONDS.sleep(1);
            }
            busesQueue.add(bus);
            logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " " + bus.getName() + " is on busstop " + this.getName());
            TimeUnit.MILLISECONDS.sleep(new Random().nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
    public void leaveFromBusStop(Bus bus) {
        try {
            while (!lock.tryLock()) {
                TimeUnit.MILLISECONDS.sleep(1);
            }
            logger.info("buses on stop " +" "+ this.getName()+" "+ busesQueue.size());
            logger.info("bus number " + bus.getBusRoute().getRouteNumber() + " " + bus.getName() + " is leaving busstop " + this.getName() + " " + bus.getName());
            busesQueue.poll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

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

    public void setBusesQueue(Queue<Bus> busesQueue) {
        this.busesQueue = busesQueue;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BusStop busStop = (BusStop) o;
        if (name != null ? !name.equals(busStop.name) : busStop.name != null) return false;
        return busesQueue != null ? busesQueue.equals(busStop.busesQueue) : busStop.busesQueue == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (busesQueue != null ? busesQueue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "BusStop {" +
                " name = '" + name + '\'' +
                ", busesQueue = " + busesQueue +
                " }";
    }
}
