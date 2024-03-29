package ogorek.wojciech.persistance.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ogorek.wojciech.persistance.exception.JsonAppException;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

public abstract class JsonConverter<T> {


    private final String jsonFilename;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Type type = ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    public JsonConverter(String jsonFilename) {this.jsonFilename = jsonFilename;}


    public void toJson(final T element){
        try(FileWriter filewriter = new FileWriter(jsonFilename)){
            if(element == null){
                throw new NullPointerException("ELEMENT IS NULL");
            }
            gson.toJson(element, filewriter);
        }catch(Exception e){
            throw new JsonAppException("to json exception " + e.getMessage());
        }
    }

    public Optional<T> fromJson(){
        try(FileReader fileReader = new FileReader(jsonFilename) ){
            return Optional.of(gson.fromJson(fileReader, type));
        }catch(Exception e){
            throw new JsonAppException("from json exception " + e.getMessage());
        }
    }
}
