package core.item;

public class ItemFactory {
    public static Class<Item> factory(String type) {
        Class item;
        if (type.equals("link")) {
            item = Link.class;
        } else if (type.equals("book")) {
            item = Book.class;
        } else if (type.equals("keyphrase")) {
            item = Keyphrase.class;
        } else if (type.equals("video")) {
            item = Video.class;
        } else {
            throw new IllegalArgumentException("Unknown item type " + type);
        }

        return item;
    }
}
