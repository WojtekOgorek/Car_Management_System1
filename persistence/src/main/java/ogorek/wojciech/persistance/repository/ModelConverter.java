package ogorek.wojciech.persistance.repository;

import java.util.List;

public class ModelConverter extends JsonConverter<List<String>> {
    public ModelConverter(String jsonFilename){
        super(jsonFilename);
    }
}
