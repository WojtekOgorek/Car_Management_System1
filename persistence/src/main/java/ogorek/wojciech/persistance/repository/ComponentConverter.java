package ogorek.wojciech.persistance.repository;

import java.util.List;

public class ComponentConverter extends JsonConverter<List<String>>{
    public ComponentConverter(String jsonFilename){
        super(jsonFilename);
    }
}
