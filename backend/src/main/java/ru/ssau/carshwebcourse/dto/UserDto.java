package ru.ssau.carshwebcourse.dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import ru.ssau.carshwebcourse.entity.Role;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@JsonIgnoreProperties(value = {"password", "userId"}, allowSetters = true)
public class UserDto {
    private Long userId;
    @Email(message = "Введите корректный Email!")
    private String email;
    private String password;
    @Pattern(regexp = "\\d{10}", message = "Неверный формат номера. Телефон должен быть в формате +7XXXXXXXXXX")
    private String phoneNumber;
    private String firstname;
    private String lastname;
    @Pattern(regexp = "\\d{2}-\\d{2}-\\d{6}",message = "Номер ВУ должен быть в формате XX-XX-XXXXXX")
    private String drvLicenseNumber;
    private LocalDateTime registerDate;
    private Set<Role> role;
}
