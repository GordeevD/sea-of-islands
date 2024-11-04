import java.util.HashMap;
import java.util.Map;

// manages sea of islands & routes between islands
public class Sea {
    Map<String, Island> islands;

    public Sea() {
        islands = new HashMap<>();
    }

    // add new island to sea of islands
    public void addIsland(String name, int population) {
        islands.put(name, new Island(name, population));
    }

    // add route betwen two islands with travel time
    public void addRoute(String island1, String island2, double travelTime) {
        Island fromIsland = islands.get(island1);
        Island toIsland = islands.get(island2);
        if (fromIsland != null && toIsland != null) {
            fromIsland.addRoute(toIsland, travelTime);
        }
    }

    public Island getIsland(String name) {
        return islands.get(name);
    }

    public static void main(String args[]) {
        Sea sea = new Sea();

    }
}

// represents each island with name, population, resources, and routes to other islands
class Island {
    private String name;
    private int population;
    private Map<Island, Double> routes; // tracks travel time from one island to another
    private Map<String, Integer> resources;  // tracks resources and their quantities

    public Island(String name, int population) {
        this.name = name;
        this.population = population;
        this.routes = new HashMap<>();
        this.resources = new HashMap<>();
    }

    // add route to island with specific travel time
    public void addRoute(Island destination, double travelTime) {
        routes.put(destination, travelTime);
    }

    public void addResources(String resource, int quantity) {
        // if resource exists, update its quantity
        if (resources.containsKey(resource)) {
            resources.put(resource, resources.get(resource) + quantity);
        } else {
            // if resource doesn't exist, add it with the given quantity
            resources.put(resource, quantity);
        }
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public Map<Island, Double> getRoutes() {
        return routes;
    }

    public Map<String, Integer> getResources() {
        return resources;
    }
}