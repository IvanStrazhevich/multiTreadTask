package by.epam.task2.service;

import by.epam.task2.entity.Bus;
import by.epam.task2.entity.BusRoute;

import java.util.ArrayList;


public class BusLauncher {
    private static final BusLauncher instance = new BusLauncher(new ArrayList<>());
    private static final int NUMBER_OF_BUSES = 10;
    private ArrayList<BusRoute> routes;

    private BusLauncher(ArrayList<BusRoute> routes) {
        this.routes = routes;
    }

    public static final BusLauncher getInstance(ArrayList<BusRoute> routes) {
        instance.setRoutes(routes);
        return instance;
    }

    public void launchBuses() {

        for (int i = 0; i < NUMBER_OF_BUSES; i++) {
            for (BusRoute route : routes
                    ) {
                new Bus(route).start();
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
