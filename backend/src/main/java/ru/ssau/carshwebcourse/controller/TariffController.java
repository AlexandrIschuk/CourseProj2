package ru.ssau.carshwebcourse.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.service.TariffService;

import java.util.List;

@Controller
@RequestMapping("/tariffs")
public class TariffController {
    private final TariffService tariffService;

    public TariffController(TariffService tariffService){
        this.tariffService = tariffService;
    }

    @PostMapping
    public ResponseEntity<TariffDto> newTariff(@RequestBody TariffDto tariffDto){
        return new ResponseEntity<>(tariffService.newTariff(tariffDto), HttpStatus.CREATED);
    }
    @GetMapping
    @ResponseBody
    public List<TariffDto> findAll(){
        return tariffService.findAllTariffs();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TariffDto> getTariffById(@PathVariable Long id){
        return new ResponseEntity<>(tariffService.getTariffById(id), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTariff(@PathVariable Long id){
        tariffService.deleteTariff(id);
    }
}
