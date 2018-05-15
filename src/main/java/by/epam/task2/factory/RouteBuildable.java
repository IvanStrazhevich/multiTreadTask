package by.epam.task2.factory;

import by.epam.task2.exception.ExtendedException;
import by.epam.task2.entity.BusRoute;

public interface RouteBuildable {
    BusRoute createRoute(String dataSource) throws ExtendedException;
}
