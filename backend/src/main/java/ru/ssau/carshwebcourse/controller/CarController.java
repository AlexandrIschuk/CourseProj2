package ru.ssau.carshwebcourse.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.service.CarService;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
@Validated
public class CarController {
    private final CarService carService;
    public CarController(CarService carService){
        this.carService = carService;
    }
    @PostMapping
    public ResponseEntity<CarDto> addCar(@Valid @RequestBody CarDto carDto){
        return new ResponseEntity<>(carService.addCar(carDto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<CarDto> findAll(){
        return carService.findAllCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCatById(@PathVariable Long id){
        return new ResponseEntity<>(carService.getCarById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @RequestBody CarDto car){
        return new ResponseEntity<>(carService.updateCar(id, car), HttpStatus.OK);
    }

    @GetMapping()
    @ResponseBody
    public List<CarDto> findFreeCars(){
        return carService.findFreeCars();
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
    }
}
