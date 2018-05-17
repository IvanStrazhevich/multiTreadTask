package by.epam.task2.runner;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.entity.Passenger;

import java.util.ArrayList;


public class PassengerLauncher {
    private static final PassengerLauncher instance = new PassengerLauncher(new ArrayList<BusRoute>());
    private ArrayList<BusRoute> routes;
    private static final int NUMBER_OF_PASSENGER = 20;

    private PassengerLauncher(ArrayList<BusRoute> routes) {
        this.routes = routes;
    }

    public static final PassengerLauncher getInstance(ArrayList<BusRoute> routes) {
        instance.setRoutes(routes);
        return instance;
    }

    public void launchPassengers() {

        for (int i = 0; i < NUMBER_OF_PASSENGER; i++) {
            for (BusRoute route : routes
                    ) {
                new Passenger(route).start();
            }
        }
    }

    public ArrayList<BusRoute> getRoutes() {
        return routes;
    }

    public void setRoutes(ArrayList<BusRoute> routes) {
        this.routes = routes;
    }
}
