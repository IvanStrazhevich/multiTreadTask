package by.epam.task2.runner;

import by.epam.task2.entity.Bus;
import by.epam.task2.entity.BusRoute;
import by.epam.task2.entity.BusStop;
import by.epam.task2.entity.Passenger;

import java.util.ArrayDeque;

public class BusRouteRunner {
    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            BusRoute busRoute = new BusRoute();
            busRoute.setRouteNumber(i);
            busRoute.setBusStops(new ArrayDeque<>());
            int j = 0;
            busRoute.getBusStops().add(new BusStop("One ", ++j));
            busRoute.getBusStops().add(new BusStop("Two ", ++j));
            busRoute.getBusStops().add(new BusStop("Three ", ++j));
            busRoute.getBusStops().add(new BusStop("Four ", ++j));
            busRoute.getBusStops().add(new BusStop("Five ", ++j));

            for (int k = 0; k < 10; k++) {
                Passenger passenger = new Passenger(busRoute);
                passenger.distributeStopsToPassengers();
                passenger.start();
            }

            for (j = 0; j < 5; j++) {
                new Bus(busRoute).start();
            }
        }
    }
}



