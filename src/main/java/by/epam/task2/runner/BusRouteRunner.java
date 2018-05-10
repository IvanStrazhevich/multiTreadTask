package by.epam.task2.runner;

import by.epam.task2.entity.thread.Bus;
import by.epam.task2.entity.BusRoute;
import by.epam.task2.entity.BusStop;

import java.util.ArrayDeque;

public class BusRouteRunner {
    public static void main(String[] args) {

        for (int i = 0; i < 1; i++) {
            BusRoute busRoute = new BusRoute();
            busRoute.setRouteNumber(i);
            busRoute.setBusStops(new ArrayDeque<>());
            busRoute.getBusStops().add(new BusStop("One " + i));
            busRoute.getBusStops().add(new BusStop("Two " + i));
            busRoute.getBusStops().add(new BusStop("Three " + i));
            busRoute.getBusStops().add(new BusStop("Four " + i));

            for (int j = 0; j < 3; j++) {
                new Bus(busRoute).start();
            }
        }

    }
}


