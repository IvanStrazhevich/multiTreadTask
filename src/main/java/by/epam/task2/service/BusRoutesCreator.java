package by.epam.task2.service;

import by.epam.task2.entity.BusRoute;
import by.epam.task2.exception.ExtendedException;
import by.epam.task2.factory.BusRouteFactory;
import by.epam.task2.factory.RouteFactory;
import by.epam.task2.parser.SourceParsable;
import by.epam.task2.parser.SourceParser;
import by.epam.task2.reader.SourceReadable;
import by.epam.task2.reader.SourceReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class BusRoutesCreator {
    private static Logger logger = LogManager.getLogger();
    private ArrayList<BusRoute> routes = new ArrayList<>();

    public ArrayList<BusRoute> serviceRoutes() {
        SourceReadable<ArrayList<String>> sourceReader = new SourceReader<>();
        SourceParsable<ArrayList<String>> sourceParser = new SourceParser<>();
        RouteFactory<ArrayList<BusRoute>> routeFactory = BusRouteFactory.getInstance();
        try {
            routes = routeFactory.createRoutes(sourceParser.createRouteDataListFromList
                    (sourceReader.readSource("data/busRoutes.txt")));
        } catch (ExtendedException e) {
            logger.log(Level.ERROR, e.getStackTrace());
        }
        return routes;
    }
}
