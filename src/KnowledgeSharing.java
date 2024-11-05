import java.util.*;

public class KnowledgeSharing {
    private Sea sea;

    public KnowledgeSharing(Sea sea) {
        this.sea = sea;
    }

    // use dijkstra's algorithm to find the shortest path for knowledge sharing
    public List<Island> findShortestPath(String startIslandName, String endIslandName) {
        // retrieve starting and ending island objects
        Island startIsland = sea.getIsland(startIslandName);
        Island endIsland = sea.getIsland(endIslandName);

        // if either starting or ending island does not exist, return empty path
        if (startIsland == null || endIsland == null) {
            return Collections.emptyList();
        }

        // priority queue for exploring islands based on distance or population
        PriorityQueue<Island> priorityQueue = new PriorityQueue<>(Comparator.comparingDouble(this::getDistance));
        Map<Island, Double> distances = new HashMap<>(); // stores min distances to each island
        Map<Island, Island> previous = new HashMap<>(); // stores previous island paths

        // initialize distances to infinity for all islands
        for (Island island : sea.islands.values()) {
            distances.put(island, Double.MAX_VALUE);
        }
        distances.put(startIsland, 0.0); // distance to start island is 0
        priorityQueue.add(startIsland); // add start island to priority queue

        // loop until all reachable islands are processed
        while (!priorityQueue.isEmpty()) {
            Island current = priorityQueue.poll(); // get island with smallest distance in queue

            // if island is reached, construct and return shortest path
            if (current.equals(endIsland)) {
                return constructPath(previous, endIsland); // found shortest path
            }

            // explore each neighbor of current island
            for (Map.Entry<Island, Double> entry : current.getRoutes().entrySet()) {
                Island neighbor = entry.getKey();
                double travelTime = entry.getValue();
                double newDist = distances.get(current) + travelTime; // calculate new potential distance to neighbor

                // update distance if shorter path to neighbor has been found
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current); // record path
                    priorityQueue.add(neighbor); // add neighbor to queue
                }
            }
        }

        return Collections.emptyList(); // return empty if no path found (island not reachable)
    }

    // get distance for priority queue comparison
    private double getDistance(Island island) {
        return island.getPopulation(); // use population as a weight
    }

    //construct path from start to end island
    private List<Island> constructPath(Map<Island, Island> previous, Island endIsland) {
        List<Island> path = new ArrayList<>();
        for (Island at = endIsland; at != null; at = previous.get(at)) {
            path.add(at); // add islands to path in reverse order
        }
        Collections.reverse(path); // reverse to get correct order from start to end
        return path;
    }
    
    public static void main(String[] args) {
        Sea sea = new Sea();

        // initialize islands and populations
        sea.addIsland("Hawaii", 1400000);
        sea.addIsland("Tahiti", 285900);
        sea.addIsland("Easter Island", 7750);
        sea.addIsland("New Zealand", 5338500);
        sea.addIsland("Samoa", 218000);

        // initialize routes and travel times
        sea.addRoute("Hawaii", "Tahiti", 5.9);
        sea.addRoute("Tahiti", "Easter Island", 31.58);
        sea.addRoute("Hawaii", "New Zealand", 10.0);
        sea.addRoute("Easter Island", "Samoa", 20.0);
        
        // create KnowledgeSharing instance to calculate shortest paths
        KnowledgeSharing ks = new KnowledgeSharing(sea);

        // test: find and print shortest path from Hawaii to Easter Island
        List<Island> path = ks.findShortestPath("Hawaii", "Easter Island");
        System.out.println("Shortest path from Hawaii to Easter Island:");
        for (Island island : path) {
            System.out.println(island.getName());
        }
    }
}
