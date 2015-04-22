package core.parser;

import com.google.gson.*;
import core.item.Item;
import core.item.ItemFactory;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by viktor on 22.04.15.
 */
public class ItemJsonParser {

    /**
     * Parse input string and try to extract array of Item objects from the JSON.
     *
     * @param s
     * @return
     * @throws ItemJsonParserException
     */
    public ArrayList<Item> parseArray(String s) throws ItemJsonParserException {
        ArrayList<Item> c = new ArrayList<Item>();
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(s);
        if (!elem.isJsonArray()) {
            throw new ItemJsonParserException("JSON array expected.");
        }
        JsonArray list = elem.getAsJsonArray();
        for (Iterator<JsonElement> i = list.iterator(); i.hasNext(); ) {
            JsonElement oelem = i.next();
            if (!oelem.isJsonObject()) {
                throw new ItemJsonParserException("JSON array of core.item.Item expected.");
            }
            JsonObject o = oelem.getAsJsonObject();
            Item item = gson.fromJson(o, ItemFactory.factory(o.get("type").getAsString()));
            c.add(item);
        }

        return c;
    }

    /**
     * Parse input string and try to extract Item object from the JSON.
     *
     * @param s
     * @return
     * @throws ItemJsonParserException
     */
    public Item parseOne(String s) throws ItemJsonParserException {
        Gson gson = new Gson();
        JsonParser parser = new JsonParser();
        JsonElement elem = parser.parse(s);
        if (!elem.isJsonObject()) {
            throw new ItemJsonParserException("JSON object expected.");
        }
        JsonObject o = elem.getAsJsonObject();
        Item item = gson.fromJson(o, ItemFactory.factory(o.get("type").getAsString()));
        return item;
    }
}
