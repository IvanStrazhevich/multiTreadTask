package by.epam.task2.factory;

import by.epam.task2.exception.ExtendedException;
import by.epam.task2.entity.BusStop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class BusStopBuilder implements BusStopBuildable {
    private static Logger logger = LogManager.getLogger();

    private BusStopBuilder() {
    }

    private static BusStopBuilder instance;

    public static BusStopBuilder getInstance() {
        if (null == instance) {
            instance = new BusStopBuilder();
        }
        return instance;
    }

    public BusStop createBusStop(String dataSource, int busStopOrderId) throws ExtendedException {
        if (dataSource != null) {
            BusStop busStop = new BusStop(dataSource,busStopOrderId);
            logger.debug(busStop.toString());
            return busStop;
        } else {
            throw new ExtendedException("Incorrect data");
        }
    }
}
