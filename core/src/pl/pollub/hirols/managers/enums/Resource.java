package pl.pollub.hirols.managers.enums;

/**
 * Created by kamil on 15.03.2016.
 */
public enum Resource {
    Stone, Wood, Steel;

    public static Resource fromString(String resource) {
        if (resource.equals("stone"))
            return Resource.Stone;
        else if (resource.equals("steel"))
            return Resource.Steel;
        else if (resource.equals("wood"))
            return Resource.Wood;

        throw new IllegalArgumentException("Wrong resource type!");
    }
}
