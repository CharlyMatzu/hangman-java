
package mensajeria;

import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;


class MessageParser {
    
    private final JSONParser parser;
    private JSONObject json;

    public MessageParser() {
        parser = new JSONParser();
    }
    
    public String jsonToString(JSONObject json){
        return json.toJSONString();
    }
    
    public JSONObject stringToJson(String msj) throws ParseException{
        json = (JSONObject) parser.parse(msj);
        return json;
    }
    
}
