package by.epam.task2.factory;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.exception.ExtendedException;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BusRouteFactory<T> implements RouteFactory<BusRoute> {
    private static Logger logger = LogManager.getLogger();
    private static BusRouteFactory<BusRoute> instance;

    public static BusRouteFactory<BusRoute> getInstance() {
        if (null == instance) {
            instance = new BusRouteFactory<>();
        }
        return instance;
    }

    @Override
    public ArrayList<BusRoute> createRoutes(ArrayList<String> routesList) {

        ArrayList<BusRoute> routes = new ArrayList<>();
        try {
            routes = RouteBuilder.getInstance().createRoutes(routesList);
        } catch (ExtendedException e) {
            logger.log(Level.ERROR, "WrongSource", e);
        }
        logger.info(routes);
        return routes;
    }
}
