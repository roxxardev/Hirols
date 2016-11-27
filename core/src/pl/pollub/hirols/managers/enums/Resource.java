package pl.pollub.hirols.managers.enums;

/**
 * Created by kamil on 15.03.2016.
 */
public enum Resource {
    STONE, WOOD, METAL, GOLD;

    public static Resource fromString(String resource) {
        if (resource.equals("stone"))
            return Resource.STONE;
        else if (resource.equals("steel"))
            return Resource.METAL;
        else if (resource.equals("wood"))
            return Resource.WOOD;
        else if (resource.equals("gold"))
            return Resource.GOLD;

        throw new IllegalArgumentException("Wrong resource type!");
    }
    public static Resource getRandomResource() {
        return values()[(int) (Math.random() * values().length)];
    }
}
