package pl.kl.companycarfleetmanagementsystem.trip;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "Departure date cannot be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    @NotNull(message = "Departure date cannot be blank")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    @NotNull(message = "Meter status cannot be blank")
    @Min(0)
    private Integer departureMeterStatus;
    @NotNull(message = "Meter status cannot be blank")
    @Min(0)
    private Integer returnMeterStatus;
    private String comments;
//    @NotNull(message = "Car id cannot be blank")
//    private Long carId;
}
