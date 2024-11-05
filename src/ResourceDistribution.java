import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
/**
 * Dijkstra's Algorithm for Resource Distribution
*/
public class ResourceDistribution {
    public void distributeResource(Sea sea, Island sourceIsland, String resourceName) {
        // Initialize distances and previous nodes in the shortest paths
        Map<String, Double> distances = new HashMap<>();
        Map<String, String> previousNodes = new HashMap<>();

        // Priority queue for node selection based on distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>();
        pq.add(new NodeDistance(sourceIsland.getName(), 0.0, 0.0));

        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            String currentNode = current.islandName;

            if (distances.getOrDefault(currentNode, Double.POSITIVE_INFINITY) <= current.distance) {
                continue; // Already processed with shorter distance
            }

            distances.put(currentNode, current.distance);

            // Explore neighbors
            Map<Island, Double> neighbors = sea.getIsland(currentNode).getRoutes();
            for (Map.Entry<Island, Double> entry : neighbors.entrySet()) {
                Island neighborIsland = entry.getKey();
                Double distance = entry.getValue();
                double newDistance = current.distance + distance;

                // Check capacity constraint and update if a shorter path is found
                if (newDistance < distances.getOrDefault(neighborIsland.getName(), Double.POSITIVE_INFINITY)) {
                    distances.put(neighborIsland.getName(), newDistance);
                    previousNodes.put(neighborIsland.getName(), currentNode);
                    pq.add(new NodeDistance(neighborIsland.getName(), newDistance, distance));
                }
            }
        }

        // Allocate resources based on shortest paths and capacities
        double availableResource = sourceIsland.getResources().get(resourceName);
        for (Map.Entry<String, Double> entry : distances.entrySet()) {
            String islandName = entry.getKey();
            if (islandName.equals(sourceIsland.getName())) continue;

            Island targetIsland = sea.getIsland(islandName);
            double pathCapacity = getPathCapacity(sea, previousNodes, islandName, resourceName);
            double amountToAllocate = Math.min(availableResource, pathCapacity);

            if (amountToAllocate > 0) {
                availableResource -= amountToAllocate;
                targetIsland.addResources(resourceName, (int)amountToAllocate);
                System.out.println("Allocated " + amountToAllocate + " " + resourceName + " to " + islandName);
            }
        }
    }
    /**
     * Calculates the maximum capacity of a path from source to target.
     *
     * @param graph      The graph representing island connections.
     * @param previous   A map tracking the previous node in the shortest path.
     * @param source     The starting island.
     * @param target     The destination island.
     * @return           The maximum capacity along this path.
     */
    public double getPathCapacity(Sea graph, Map<String, String> previous, String source, String target) {
        double pathCapacity = Double.POSITIVE_INFINITY; // Initialize with max possible value

        String currentIsland = target;
        while (!currentIsland.equals(source)) {
            String nextIsland = previous.get(currentIsland);
            if (nextIsland == null) break; // No path found, or error in tracking

            Double neighborCapacity = graph.getIsland(nextIsland).getRoutes().get(currentIsland);
            // graph.getNeighbors(nextIsland).get(currentIsland);
            if (neighborCapacity != null) {
                pathCapacity = Math.min(pathCapacity, neighborCapacity);
            }

            currentIsland = nextIsland;
        }

        return pathCapacity == Double.POSITIVE_INFINITY ? 0 : pathCapacity; // Return calculated capacity or 0 if no path
    }

    public class NodeDistance implements Comparable<NodeDistance> {
        public String islandName; // Name of the Island (Vertex)
        public double distance;    // Distance from the Source (for Dijkstra's)
        public double capacity;    // Maximum Capacity along this Path

        public NodeDistance(String islandName, double distance, double capacity) {
            this.islandName = islandName;
            this.distance = distance;
            this.capacity = capacity;
        }

        @Override
        public int compareTo(NodeDistance other) {
            // Prioritize based on Distance (for Dijkstra's), then by Capacity (higher is better)
            if (this.distance != other.distance) {
                return Double.compare(this.distance, other.distance);
            } else {
                return Double.compare(other.capacity, this.capacity); // Higher capacity comes first
            }
        }

        @Override
        public String toString() {
            return "NodeDistance{" +
                    "islandName='" + islandName + '\'' +
                    ", distance=" + distance +
                    ", capacity=" + capacity +
                    '}';
        }
    }
    public static void main(String[] args) {
        Sea sea = new Sea();
        Island island1 = new Island("Island1", 100);
        Island island2 = new Island("Island2", 200);
        Island island3 = new Island("Island3", 300);
        Island island4 = new Island("Island4", 400);
        Island island5 = new Island("Island5", 500);
        Island island6 = new Island("Island6", 600);
        Island island7 = new Island("Island7", 700);
        Island island8 = new Island("Island8", 800);
        Island island9 = new Island("Island9", 900);
        Island island10 = new Island("Island10", 1000);
        sea.addIsland(island1);
        sea.addIsland(island2);
        sea.addIsland(island3);
        sea.addIsland(island4);
        sea.addIsland(island5);
        sea.addIsland(island6);
        sea.addIsland(island7);
        sea.addIsland(island8);
        sea.addIsland(island9);
        sea.addIsland(island10);
        island1.addRoute(island2, 10);
        island1.addRoute(island3, 20);
        island1.addRoute(island4, 30);
        island1.addRoute(island5, 40);
        island1.addRoute(island6, 50);
        island1.addRoute(island7, 60);
        island1.addRoute(island8, 70);
        island1.addRoute(island9, 80);
        island1.addRoute(island10, 90);
        island2.addRoute(island1, 10);
        island2.addRoute(island3, 10);
        island2.addRoute(island4, 20);
        island2.addRoute(island5, 30);
        island2.addRoute(island6, 40);
        island2.addRoute(island7, 50);
        island2.addRoute(island8, 60);
        island2.addRoute(island9, 70);
        island2.addRoute(island10, 80);
        island3.addRoute(island1, 20);
        island3.addRoute(island2, 10);
        island3.addRoute(island4, 10);

        island1.addResources("Resource1", 200);

        ResourceDistribution distributor = new ResourceDistribution();
        distributor.distributeResource(sea, island1, "Resource1");
    }
}