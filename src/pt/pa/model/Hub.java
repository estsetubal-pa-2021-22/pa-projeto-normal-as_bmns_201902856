package pt.pa.model;

public class Hub implements Comparable<Hub>{
    private String name;
    private int population;
    private int guiX, guiY;
    private final boolean SMALL_STRING = true;

    public Hub(String name, int population, int guiX, int guiY) {
        this.name = name;
        this.population = population;
        this.guiX = guiX;
        this.guiY = guiY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getGuiX() {
        return guiX;
    }

    public void setGuiX(int guiX) {
        this.guiX = guiX;
    }

    public int getGuiY() {
        return guiY;
    }

    public void setGuiY(int guiY) {
        this.guiY = guiY;
    }

    public void setGuiPos(int guiX, int guiY) {
        setGuiX(guiX);
        setGuiY(guiY);
    }

    public double distanceTo(Hub other) {
        return Math.hypot(other.guiX - guiX, other.guiY - guiY);
    }

    /**
     * Returns a formatted String representation of the Hub object.
     * Example for a city with the following values for its attributes:
     * - name: "Generic City"
     * - population: 123456
     * - guiX: 123
     * - guiY: 234
     *
     * Hub {name='Generic City', population=123456, position=(123, 234)}
     *
     * @return The formatted String representation of the Hub object.
     */
    @Override
    public String toString() {
        if(!SMALL_STRING) {
            final StringBuilder sb = new StringBuilder("Hub {");
            sb.append("name='").append(name).append('\'');
            sb.append(", population=").append(population);
            sb.append(", position=(").append(guiX);
            sb.append(", ").append(guiY);
            sb.append(")}");
            return sb.toString();
        }
        return name;
    }

    @Override
    public int compareTo(Hub obj) {
        return this.name.compareToIgnoreCase(obj.getName());
    }
}
