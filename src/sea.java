import java.util.HashMap;
import java.util.Map;

public class Sea {
    public static void main(String args[]) {
    }
}

class Island {
    String name;
    int population;
    Map<Island, Double> routes; // tracks travel time from one island to another
    Map<String, Integer> resources;  // tracks resources and their quantities

    public Island(String name, int population) {
        this.name = name;
        this.population = population;
        this.routes = new HashMap<>();
        this.resources = new HashMap<>();
    }
}