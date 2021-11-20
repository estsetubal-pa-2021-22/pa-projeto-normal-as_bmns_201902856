package pt.pa.model;

public class Route {
    private Hub firstHub, secondHub;
    private double distance;
    private boolean recalculateDistance;

    public Route(Hub firstHub, Hub secondHub, double distance) {
        this.firstHub = firstHub;
        this.secondHub = secondHub;
        this.distance = Math.abs(distance);
        this.recalculateDistance = false;
    }

    public Route(Hub firstHub, Hub secondHub) {
        this(firstHub, secondHub, firstHub.distanceTo(secondHub));
    }

    public Hub getFirstHub() {
        return firstHub;
    }

    public void setFirstHub(Hub firstHub) {
        this.firstHub = firstHub;
        distance = firstHub.distanceTo(this.secondHub);
    }

    public Hub getSecondHub() {
        return secondHub;
    }

    public void setSecondHub(Hub secondHub) {
        this.secondHub = secondHub;
        distance = firstHub.distanceTo(this.secondHub);
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Route {\n");
        sb.append("  firstHub=").append(firstHub).append("\n");
        sb.append("  secondHub=").append(secondHub).append("\n");
        sb.append("  distance=").append(distance).append("\n");
        sb.append('}');
        return sb.toString();
    }
}
