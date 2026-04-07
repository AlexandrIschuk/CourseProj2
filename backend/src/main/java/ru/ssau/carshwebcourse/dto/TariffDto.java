package ru.ssau.carshwebcourse.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.ssau.carshwebcourse.entity.TariffType;

@Getter
@Setter
public class TariffDto {
    private Long tariffId;
    private String name;
    private Long price;
    private TariffType tariffType;
}
