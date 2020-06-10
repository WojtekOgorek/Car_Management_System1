package ogorek.wojciech.service;

import lombok.experimental.UtilityClass;
import ogorek.wojciech.persistance.exception.AppException;

import ogorek.wojciech.persistance.model.enums.Color;
import ogorek.wojciech.persistance.repository.ComponentConverter;
import ogorek.wojciech.persistance.repository.ModelConverter;
import ogorek.wojciech.persistance.validator.impl.ComponentValidator;
import ogorek.wojciech.persistance.validator.impl.ModelValidator;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


public class RandomDataService {

    private Random rnd = new Random();
    private final int NUMBER_OF_COMPONENTS = 9;
    private final String MODELS = "./resources/data/models.json";
    private final String COMPONENTS = "./resources/data/components.json";
    private Set<String> modelsFromJsonFile = getModelsFromJson(MODELS);
    private Set<String> componentsFromJsonFile = getComponentsFromJson(COMPONENTS);


    public double randomMileage() {
        var rndNum = 1 + rnd.nextInt(10000);
        return rndNum;

    }

    public BigDecimal randomPrice() {
        var rndNum = 5000 + rnd.nextInt(2000);
        return new BigDecimal(String.valueOf(rndNum));

    }

    public Color randomColor() {
        Color[] colors = Color.values();
        Random rnd = new Random();
        Color rndColor = colors[rnd.nextInt(colors.length)];
        return rndColor;
    }

    public Set<String> getModelsFromJson(String jsonFilename) {

        var modelsValidator = new ModelValidator();
        var counter = new AtomicInteger(1);

        return new ModelConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new AppException("Random models data service - cannot read data from json file"))
                .stream()
                .filter(model -> {
                    var errors = modelsValidator.validate(model);
                    if (modelsValidator.hasErrors()) {
                        System.out.println("\n-------------- validation errors for random model nr." + counter.getAndIncrement() + ". " + model + "--------------");
                    }
                    return !modelsValidator.hasErrors();
                }).collect(Collectors.toSet());

    }

    public Set<String> getComponentsFromJson(String jsonFilename) {

        var componentValidator = new ComponentValidator();
        var counter = new AtomicInteger(1);

        return new ComponentConverter(jsonFilename)
                .fromJson()
                .orElseThrow(() -> new AppException("Random component data service - cannot read data from json file"))
                .stream()
                .filter(component -> {
                    var errors = componentValidator.validate(component);
                    if (componentValidator.hasErrors()) {
                        System.out.println("\n-------------- validation errors for random component nr." + counter.getAndIncrement() + ". " + component + "--------------");
                    }
                    return !componentValidator.hasErrors();
                }).collect(Collectors.toSet());
    }

    public String getRandomModelFromCollection() {
        return modelsFromJsonFile
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected.stream()
                            .findFirst()
                            .get();
                }));
    }

    public Set<String> getRandomComponentsFromCollection(){
        var randomNumberOfComponents = rnd.nextInt(NUMBER_OF_COMPONENTS);
        return componentsFromJsonFile
                .stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected
                            .stream()
                            .limit(randomNumberOfComponents)
                            .collect(Collectors.toSet());
                }));
    }
}
