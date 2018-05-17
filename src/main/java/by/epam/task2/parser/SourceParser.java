package by.epam.task2.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceParser<T> implements SourceParsable<ArrayList<String>> {
    static Logger logger = LogManager.getLogger();
    private static final String ROUTE_PATTERN = "(\\d+\\.)\\s((\\w+\\s?)+?(\\.\\s)?)+";


    @Override
    public ArrayList<String> createRouteDataListFromList(ArrayList<String> routeDataList) {
        ArrayList<String> routeList = new ArrayList<>();
        Pattern pattern = Pattern.compile(ROUTE_PATTERN);
        for (String routeData : routeDataList
                ) {
            Matcher matcher = pattern.matcher(routeData);
            while (matcher.find()) {
                String route = matcher.group();
                routeList.add(route);
            }
        }
        logger.info(routeList + " Parser to route description result");
        return routeList;
    }
}

