package core;

import org.springframework.data.annotation.Id;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public abstract class Item {
    @Id
    private UUID id;
    private UUID userId;
    private String description;
    private HashSet<String> tags;
    private URL url;

    public Item() {
        this.description = "";
        this.tags = new HashSet<String>();
    }

    /**
     *
     * @param desc
     */
    public synchronized void setDescription(String desc) {
        this.description = desc;
    }

    /**
     *
     * @return
     */
    public synchronized String getDescription() {
        return this.description;
    }

    /**
     *
     * @param tag
     */
    public synchronized void addTag(String tag) {
        this.tags.add(tag);
    }

    /**
     *
     * @return
     */
    public HashSet<String> getTags() {
        return this.tags;
    }

    public void setTags(HashSet<String> tags) {
        this.tags.clear();

        for(String t : tags) {
            this.addTag(t);
        }
    }

    /**
     *
     * @param tagName
     */
    public synchronized void removeTagByName(String tagName) {
        this.tags.remove(tagName);
     }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Return type. Used for JSON marshal to know item type.
     *
     * @return
     */
    public String getType() {
        return this.getClass().getSimpleName().toLowerCase();
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    /**
     * Populate item with values from other item.
     *
     * @param item
     */
    public void populate(Item item) {
        if (item.getUrl() != null) {
            this.setUrl(item.getUrl());
        }
        if (item.getDescription() != null) {
            this.setDescription(item.getDescription());
        }
        if (item.getTags() != null) {
            this.setTags(item.getTags());
        }
    }
}
