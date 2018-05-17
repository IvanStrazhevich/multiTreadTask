package by.epam.task2.factory;

import by.epam.task2.exception.ExtendedException;
import by.epam.task2.entity.BusRoute;

import java.util.ArrayList;

public interface RouteBuildable {
    ArrayList<BusRoute> createRoutes(ArrayList<String> routesData) throws ExtendedException;
}
