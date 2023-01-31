package it.polimi.cpms.bookingservice.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.cpms.bookingservice.configuration.JacksonObjectMapperConfig;
import it.polimi.cpms.bookingservice.model.booking.Booking;
import it.polimi.cpms.bookingservice.model.booking.BookingStatus;
import it.polimi.cpms.bookingservice.model.booking.dto.BookingKafkaDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.*;

public class BookingMapper {
    static ObjectMapper objectMapper =  new JacksonObjectMapperConfig().jsonObjectMapper();

    public static BookingDto buildBookingDto(Booking booking){
        return new BookingDto(
                booking.getId(),
                booking.getBookingCode(),
                booking.getCustomerId(),
                booking.getChargingStationId(),
                booking.getChargingPointId(),
                booking.getSocketId(),
                booking.getSocketType(),
                CommonMapper.buildTimeFrameDto(booking.getTimeFrame()),
                booking.getBookingType()
        );
    }

    public static BookingKafkaDto buildBookingKafkaDto(Booking booking){
        return new BookingKafkaDto(
                booking.getId(),
                booking.getBookingCode(),
                booking.getCustomerId(),
                booking.getChargingStationId(),
                booking.getChargingPointId(),
                booking.getSocketId(),
                booking.getSocketType(),
                CommonMapper.buildTimeFrameDto(booking.getTimeFrame()),
                booking.getBookingType(),
                booking.getBookingStatus().getBookingStatus()
        );
    }

    public static BookingStatusDto buildBookingStatusDto(BookingStatus bookingStatus){
        switch (bookingStatus.getBookingStatus()){
            case BookingStatusPlanned -> {
                return new BookingStatusPlannedDto().bookingId(bookingStatus.getId());
            }
            case BookingStatusInProgress -> {
                return new BookingStatusInProgressDto()
                        .expectedMinutesLeft(bookingStatus.getProgressInformation()
                                .expectedMinutesLeft())
                        .bookingId(bookingStatus.getId());
            }
            case BookingStatusCancelled -> {
                return new BookingStatusCancelledDto().bookingId(bookingStatus.getId());
            }
            case BookingStatusCompleted -> {
                return new BookingStatusCompletedDto().bookingId(bookingStatus.getId());
            }
            case BookingStatusExpired -> {
                return new BookingStatusExpiredDto().bookingId(bookingStatus.getId());
            }
            default -> throw new IllegalArgumentException(String.format("Booking status %s not supported", bookingStatus.getBookingStatus()));
        }
    }
}
