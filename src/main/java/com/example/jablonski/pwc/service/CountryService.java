package com.example.jablonski.pwc.service;

import com.example.jablonski.pwc.exception.CountryNotFoundException;
import com.example.jablonski.pwc.exception.NoPathFoundException;
import com.example.jablonski.pwc.model.Country;
import com.example.jablonski.pwc.model.CountryNode;
import com.example.jablonski.pwc.model.RouteResponse;
import com.example.jablonski.pwc.model.Routing;
import com.example.jablonski.pwc.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CountryService {

    private final HashMap<Routing, RouteResponse> ROUTING_CACHE = new HashMap<>();

    public RouteResponse getPath(String origin, String destination) {
        Routing key = new Routing(origin, destination);
        Optional<RouteResponse> cached = Optional.ofNullable(ROUTING_CACHE.get(key));
        if (cached.isPresent()) {
            log.info("Returning from cache");
            return cached.get();
        }
        CountryNode path = findPath(origin, destination)
                .orElseThrow(() -> new NoPathFoundException(origin, destination));

        List<String> routing = new LinkedList<>();
        routing.add(path.getCode());
        CountryNode currentStart = path.getParentFromStart();
        while (currentStart != null) {
            routing.add(0, currentStart.getCode());
            currentStart = currentStart.getParentFromStart();
        }
        CountryNode currentEnd = path.getParentFromEnd();
        while (currentEnd != null) {
            routing.add(currentEnd.getCode());
            currentEnd = currentEnd.getParentFromEnd();
        }
        ROUTING_CACHE.put(key, new RouteResponse(routing));
        return new RouteResponse(routing);
    }

    private Optional<CountryNode> findPath(String origin, String destination) {
        Map<String, CountryNode> graphNodes = createGraphNodes();
        CountryNode start = Optional.ofNullable(graphNodes.get(origin))
                .orElseThrow(() -> new CountryNotFoundException(origin));
        CountryNode end = Optional.ofNullable(graphNodes.get(destination))
                .orElseThrow(() -> new CountryNotFoundException(destination));
        log.info("Looking for a routing between origin={}, destination={}", origin, destination);
        Queue<CountryNode> startQueue = new ArrayDeque<>();
        Queue<CountryNode> endQueue = new ArrayDeque<>();
        startQueue.add(start);
        endQueue.add(end);
        Set<String> visitedFromStart = new HashSet<>();
        Set<String> visitedFromEnd = new HashSet<>();
        visitedFromStart.add(start.getCode());
        visitedFromEnd.add(end.getCode());

        while (!startQueue.isEmpty() && !endQueue.isEmpty()) {
            int startQueueSize = startQueue.size();
            int endQueueSize = endQueue.size();

            for (int i = 0; i < startQueueSize; i++) {
                CountryNode currentStart = startQueue.remove();
                for (CountryNode border : currentStart.getBorders()) {
                    if (visitedFromEnd.contains(border.getCode())) {
                        border.setParentFromStart(currentStart);
                        return Optional.of(border);
                    }
                    if (!visitedFromStart.contains(border.getCode())) {
                        startQueue.add(border);
                        visitedFromStart.add(border.getCode());
                        border.setParentFromStart(currentStart);
                    }
                }
            }

            for (int i = 0; i < endQueueSize; i++) {
                CountryNode currentEnd = endQueue.remove();
                for (CountryNode border : currentEnd.getBorders()) {
                    if (visitedFromStart.contains(border.getCode())) {
                        border.setParentFromEnd(currentEnd);
                        return Optional.of(border);
                    }
                    if (!visitedFromEnd.contains(border.getCode())) {
                        endQueue.add(border);
                        visitedFromEnd.add(border.getCode());
                        border.setParentFromEnd(currentEnd);
                    }
                }
            }
        }
        log.info("No routing found for origin={}, destination={}", origin, destination);
        return Optional.empty();
    }

    private Map<String, CountryNode> createGraphNodes() {
        List<Country> countries = FileUtil.geCountries();
        Map<String, CountryNode> countryNodeMap = countries.stream()
                .map(country -> CountryNode.of(country.name().common(), country.cca3()))
                .collect(Collectors.toMap(CountryNode::getCode, Function.identity()));

        countries.forEach(country -> addBorders(countryNodeMap, country));

        return countryNodeMap;
    }

    private static void addBorders(Map<String, CountryNode> countryNodeMap, Country country) {
        CountryNode countryNode = countryNodeMap.get(country.cca3());
        country.borders()
                .forEach(border -> countryNode.addBorder(countryNodeMap.get(border)));
    }
}
