package ru.ssau.carshwebcourse.mapping;

import org.springframework.stereotype.Component;
import ru.ssau.carshwebcourse.dto.TariffDto;
import ru.ssau.carshwebcourse.entity.Tariff;

@Component
public class TariffMappingUtils {
    public Tariff mapTariffToEntity(TariffDto tariffDto){
        Tariff entity = new Tariff();
        entity.setTariffId(tariffDto.getTariffId());
        entity.setName(tariffDto.getName());
        entity.setPrice(tariffDto.getPrice());
        entity.setTariffType(tariffDto.getTariffType());
        return entity;
    }

    public TariffDto mapTariffToDto(Tariff entity){
        TariffDto tariffDto = new TariffDto();
        tariffDto.setTariffId(entity.getTariffId());
        tariffDto.setName(entity.getName());
        tariffDto.setPrice(entity.getPrice());
        tariffDto.setTariffType(entity.getTariffType());
        return tariffDto;
    }
}
