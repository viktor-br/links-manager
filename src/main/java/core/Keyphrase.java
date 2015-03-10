package core;

public class Keyphrase extends Item {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public void populate(Item item) {
        super.populate(item);
        if (((Keyphrase) item).getText() != null) {
            this.setText(((Keyphrase) item).getText());
        }
    }
}
