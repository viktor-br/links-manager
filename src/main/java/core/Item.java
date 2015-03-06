package core;

import org.springframework.data.annotation.Id;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by viktor on 28.02.15.
 */
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
}
