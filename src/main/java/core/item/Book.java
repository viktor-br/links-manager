package core.item;

public class Book extends Item {
    private String title;
    private String author;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void populate(Book item) {
        super.populate(item);
        if (item.getTitle() != null) {
            this.setTitle(item.getTitle());
        }
        if (item.getAuthor() != null) {
            this.setAuthor(item.getAuthor());
        }
    }
}
