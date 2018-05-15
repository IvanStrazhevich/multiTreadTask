package by.epam.task2.factory;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.exception.ExtendedException;
import by.epam.task2.parser.SourceParsable;
import by.epam.task2.parser.SourceParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayDeque;
import java.util.ArrayList;


public class RouteBuilder implements RouteBuildable {
    static Logger logger = LogManager.getLogger();
    private BusStopBuildable busStopBuilder;
    private SourceParsable sourceParser;

    private RouteBuilder(BusStopBuildable busStopBuilder, SourceParsable sourceParser) {
        this.busStopBuilder = busStopBuilder;
        this.sourceParser = sourceParser;
    }

    private static RouteBuilder instance;

    public static RouteBuilder getInstance() {
        if (null == instance) {
            instance = new RouteBuilder(BusStopBuilder.getInstance(), new SourceParser());
        }
        return instance;
    }

    @Override
    public BusRoute createRoute(String dataSource) throws ExtendedException {
        if (dataSource != null) {
            ArrayList<String> routDataList = sourceParser.createRouteDataListFromList(dataSource);
            logger.debug(routDataList + " Data for route");
            BusRoute route = new BusRoute();
            route.setBusStops(new ArrayDeque<>());
            for (int i = 0; i < routDataList.size() - 1; i++) {
                if (i != 0 && route.getBusStops() != null) {
                    route.getBusStops().add(busStopBuilder.createBusStop(routDataList.get(i)));
                } else {
                    route.setRouteNumber(Integer.parseInt(routDataList.get(i)));
                }
            }
            logger.debug(route.toString());
            return route;
        } else {
            throw new ExtendedException(" Incorrect data");
        }
    }

    public void setBusStopBuildable(BusStopBuildable busStopBuilder) {
        this.busStopBuilder = busStopBuilder;
    }

    public void setSourceParser(SourceParsable sourceParser) {
        this.sourceParser = sourceParser;
    }
}
