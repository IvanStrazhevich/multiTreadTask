package by.epam.task2.runner;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.service.BusLauncher;
import by.epam.task2.service.BusRoutesCreator;
import by.epam.task2.service.PassengerLauncher;

import java.util.ArrayList;

public class BusRouteRunner {
    public static void main(String[] args) {
        BusRoutesCreator busRoutesCreator = new BusRoutesCreator();
        ArrayList<BusRoute> routes = busRoutesCreator.serviseRoutes();
        BusLauncher busLauncher = BusLauncher.getInstance(routes);
        PassengerLauncher passengerLauncher = PassengerLauncher.getInstance(routes);
        passengerLauncher.launchPassengers();
        busLauncher.launchBuses();
    }
}




