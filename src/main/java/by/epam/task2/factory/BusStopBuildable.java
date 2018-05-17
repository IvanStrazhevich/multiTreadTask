package by.epam.task2.factory;

import by.epam.task2.entity.BusStop;
import by.epam.task2.exception.ExtendedException;

public interface BusStopBuildable {
    BusStop createBusStop(String dataSource, int busStopOrderId) throws ExtendedException;
}
