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

@Controller
@RequestMapping("/cars")
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

    @GetMapping
    @ResponseBody
    public List<CarDto> findAll(){
        return carService.findAllCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCatById(@PathVariable Long id){
        return new ResponseEntity<>(carService.getCarById(id), HttpStatus.OK);
    }

    @GetMapping("free")
    @ResponseBody
    public List<CarDto> findFreeCars(){
        return carService.findFreeCars();
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCarStatus(@PathVariable Long id, @RequestBody CarDto carDto){
        return new ResponseEntity<>(carService.updateCarStatus(id,carDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable Long id){
        carService.deleteCar(id);
    }
}
