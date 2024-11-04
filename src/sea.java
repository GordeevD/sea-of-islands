import java.util.HashMap;
import java.util.Map;

public class Sea {
    public static void main(String args[]) {
    }
}

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

    public Map<String, Integer> getResources() {
        return resources;
    }
}