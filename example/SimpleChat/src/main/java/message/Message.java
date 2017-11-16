
package message;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.json.JsonObject;
import javax.json.Json;
import javax.json.JsonReader;

/**
 * A message is an object containing information that the user wants to send though the socket.
 * A message has a header (message type) that is used internally to match message to message type. A header is used to categorise messages,
 * it also has list of properties set by the user.
 * If the user wants to send its own JSON object, message supports json.
 *
 * The format of a message sent through the socket will be a json object structured like this:
 *
 * {
 *      "h": the header,
 *      "prop":{ an object representing the properties set by the user},
 *      "json":{ if the user has specified a json object it will be contained here}
 * }
 *
 * The properties has been added for users who don't want or know how to use JSON.
 *
 * @author Nicolas Dilley
 */
public class Message {

    private HashMap<String,String> properties;
    private String header;
    private JsonObject json = null;

    /**
     * This is the constructor to be used by users of the this library to create a new message.
     */
    public Message()
    {
        properties = new HashMap<>();
    }

    /**
     * Used internally to parse a string into a message.
     *
     * @param message the json formatted string that contains the formatted message.
     */
    public Message(String message)
    {
        this();
        extract(message);
    }

    /**
     * fill the field of the object with the values set in the string provided.
     *  The string provided has to be formatted in this way
     *  {
     *      "h":"header",
     *      "prop":{properties},
     *      "json":{Json object}
     *  }
     * @param messageToExtract a json formatted string representing a message.
     */
    private void extract(String messageToExtract)
    {
        try (JsonReader reader = Json.createReader(new StringReader(messageToExtract))) {
            JsonObject object = reader.readObject();
            reader.close();

            header = object.getString("h");
            json = object.getJsonObject("json");
            JsonObject propertiesObject = object.getJsonObject("prop");

            Set<String> propertiesKeys = propertiesObject.keySet();

            for (Iterator<String> it = propertiesKeys.iterator(); it.hasNext(); ) {
                String key = it.next();
                this.properties.put(key,propertiesObject.getString(key));
            }
        }
    }

    public void addProperty(String name,String value)
    {
        properties.put(name,value);
    }

    /**
     * Returns the property in the message that matches the key.
     *
     * @param key the key of the value to be retrieved.
     * @return String the value matched by the key.
     */
    public String getProperty(String key)
    {
        return properties.get(key);
    }
    /**
     * Format the message into a json object.
     * The format of a message will be in this form
     * {
     *      h:message header
     *      prop: the properties of the message
     *      json: the json object of the message
     * }
     * @return String A json formatted string of the message.
     */
    public String getMessageInJson()
    {
        // include header in the message and the properties
        StringBuilder message = new StringBuilder();
        message.append("{\"h\":\"");
        message.append(header);
        message.append("\"");
        message.append(formatProperties());

        if(json != null) // only add json if needed (Reduce bandwith usage)
        {
            message.append(",\"json\":");
            message.append(json);
        }
        message.append("}\n");

        return message.toString();
    }

    /**
     * Returns the json object in the message
     * @return JsonObject the json property of the message
     */
    public JsonObject getJson()
    {
        return json;
    }

    /**
     * A setter of the header of the message. The header is place in the "h" property
     * in a message.
     *
     * @param header the new header to be set in the message
     */
    public void setHeader(String header)
    {
        this.header = header;
    }

    /**
     * Format the properties into a json formatted string.
     * @return  json formatted string of the properties
     */
    private String formatProperties()
    {
        if(properties.size() == 0)
        {
            return "";
        }

        StringBuilder toReturn = new StringBuilder();

        toReturn.append(",\"prop\":{");
        boolean isFirst = true;

        for(String key : properties.keySet())
        {
            // check if the value is the first one
            // if it is not then add a starting , to seperate from previous value
            if(isFirst)
            {
                isFirst = false;
            }
            else
            {
                toReturn.append(",");
            }
            toReturn.append("\"" + key + "\"" + ":\"" + properties.get(key) + "\"");
        }

        toReturn.append("}");


        return toReturn.toString();
    }

    /**
     * returns the header property of the message ("h").
     *
     * @return String the header property of the message.
     */
    public String getHeader()
    {
        return header;
    }

    /**
     * Setter for the json field
     * The Json field is used to incorporate a json object into the message.
     *
     * @param json JsonObject
     */
    public void setJson(JsonObject json)
    {
        this.json = json;
    }
}
