package pl.pollub.hirols.managers.enums;

public enum ResourceType {
    STONE, WOOD, METAL, GOLD;

    public static ResourceType fromString(String resource) {
        if (resource.equals("stone"))
            return ResourceType.STONE;
        else if (resource.equals("metal"))
            return ResourceType.METAL;
        else if (resource.equals("wood"))
            return ResourceType.WOOD;
        else if (resource.equals("gold"))
            return ResourceType.GOLD;

        throw new IllegalArgumentException("Wrong resource type!");
    }
    public static ResourceType getRandomResource() {
        return values()[(int) (Math.random() * values().length)];
    }
}
