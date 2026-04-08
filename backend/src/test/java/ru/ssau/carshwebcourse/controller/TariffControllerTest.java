package ru.ssau.carshwebcourse.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import ru.ssau.carshwebcourse.dto.CarDto;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.entity.Car;
import ru.ssau.carshwebcourse.entity.CarStatus;
import ru.ssau.carshwebcourse.entity.Tariff;
import ru.ssau.carshwebcourse.entity.TariffType;
import ru.ssau.carshwebcourse.repository.TariffRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class TariffControllerTest {
    @Autowired
    private TariffController tariffController;
    @Autowired
    private TariffRepository tariffRepository;

    private Long tariffId;



    @BeforeEach
    public void before(){
        Tariff tariff = new Tariff();
        tariff.setName("Tariff 1");
        tariff.setTariffType(TariffType.KILOMETER);
        Tariff savedTariff = tariffRepository.save(tariff);
        tariffId = savedTariff.getTariffId();
    }

    @AfterEach
    public void after(){
        tariffRepository.deleteAllInBatch();
    }

    @Test
    void testFindAllTariffs(){
        List<TariffDto> tariffs = tariffController.findAll();
        assertEquals(1, tariffs.size());
        assertEquals(tariffId, tariffs.get(0).getTariffId());
    }

    @Test
    void testFindTariffById(){
        TariffDto newTariff = new TariffDto();
        newTariff.setName("Tariff 2");
        newTariff.setTariffType(TariffType.MINUTE);
        TariffDto saved = tariffController.newTariff(newTariff).getBody();
        TariffDto tariff = tariffController.getTariffById(saved.getTariffId()).getBody();
        assertEquals("Tariff 2", tariff.getName());
    }

}
