package pl.kl.companycarfleetmanagementsystem.trip;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kl.companycarfleetmanagementsystem.car.Car;
import pl.kl.companycarfleetmanagementsystem.car.CarRepository;
import pl.kl.companycarfleetmanagementsystem.car.CarService;
import pl.kl.companycarfleetmanagementsystem.validator.MeterStatusValidator;
import pl.kl.companycarfleetmanagementsystem.validator.TripDateValidator;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TripService {

    private final CarRepository carRepository;
    private final CarService carService;
    private final TripRepository tripRepository;

    public Trip createTrip(CreateTripRequest request) {

        final LocalDate systemStartDate = LocalDate.of(2000, 1, 1);
        final Integer systemStartMeterStatus = 0;

        final Car car = carService.fetchCarById(request.getCarId());

        final LocalDate lastReturnDate = car.getTrips().stream()
                .map(Trip::getReturnDate)
                .max(LocalDate::compareTo)
                .orElse(systemStartDate);

        final Integer lastReturnMeterStatus = car.getTrips().stream()
                .map(Trip::getReturnMeterStatus)
                .max(Integer::compareTo)
                .orElse(systemStartMeterStatus);

        TripDateValidator.validateTripDateOnTripCreate(request.getDepartureDate(), request.getReturnDate(), lastReturnDate);
        MeterStatusValidator.validateMeterStatusOnTripCreate(request.getDepartureMeterStatus(), request.getReturnMeterStatus(), lastReturnMeterStatus);

        final Trip trip = Trip.builder()
                .departureDate(request.getDepartureDate())
                .returnDate(request.getReturnDate())
                .departureMeterStatus(request.getDepartureMeterStatus())
                .returnMeterStatus(request.getReturnMeterStatus())
                .comments(request.getComments())
                .car(car)
                .build();

        return tripRepository.save(trip);
    }

    public List<Trip> fetchAllTrips() {

        return tripRepository.findAll();
    }

    public Trip editTrip(UpdateTripRequest request) {
        final Trip trip = tripRepository.findById(request.getId())
                .orElseThrow(() -> new NoSuchElementException("Trip with id: " + request.getId() + "not found"));

        final Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new NoSuchElementException("Car with id: " + request.getCarId() + "not found"));

        TripDateValidator.validateTripDateOnTripEdit(request.getDepartureDate(), request.getReturnDate());
        MeterStatusValidator.validateMeterStatusOnTripEdit(request.getDepartureMeterStatus(), request.getReturnMeterStatus());

        trip.setDepartureDate(request.getDepartureDate());
        trip.setReturnDate(request.getReturnDate());
        trip.setDepartureMeterStatus(request.getDepartureMeterStatus());
        trip.setReturnMeterStatus(request.getReturnMeterStatus());
        trip.setComments(request.getComments());
        trip.setCar(car);

        return tripRepository.save(trip);
    }

    public Trip fetchTripById(Long id) {

        return tripRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Trip with id: " + id + " not found"));
    }
}
