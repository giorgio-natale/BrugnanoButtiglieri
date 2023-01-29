package it.polimi.emall.emsp.bookingmanagementservice.mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.emall.emsp.bookingmanagementservice.configuration.JacksonObjectMapperConfig;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.BookingRequestClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.SocketTypeClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.client.cpms_bookingservice.model.TimeframeClientDto;
import it.polimi.emall.emsp.bookingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.Booking;
import it.polimi.emall.emsp.bookingmanagementservice.model.booking.BookingStatus;

public class BookingDtoMapper {
    static ObjectMapper objectMapper =  new JacksonObjectMapperConfig().jsonObjectMapper();
    public static BookingDto buildBookingDto(BookingClientDto bookingClientDto){
        return objectMapper.convertValue(bookingClientDto, BookingDto.class);
    }

    public static BookingDto buildBookingDto(Booking booking){

        TimeframeDto timeFrameDto = new TimeframeDto(booking.getTimeFrame().getStartInstant())
                .endInstant(booking.getTimeFrame().getEndInstant());
        return new BookingDto(
                booking.getId(),
                booking.getBookingCode(),
                booking.getCustomerId(),
                booking.getChargingStationId(),
                booking.getChargingPointId(),
                booking.getSocketId(),
                booking.getSocketType(),
                timeFrameDto,
                booking.getBookingType()
        );
    }

    public static BookingRequestClientDto buildBookingRequestClientDto(BookingRequestDto bookingRequestDto){
        if(bookingRequestDto instanceof BookingInAdvanceDto bookingInAdvanceDto){
            TimeframeClientDto timeframeClientDto = objectMapper.convertValue(bookingInAdvanceDto.getTimeframe(), TimeframeClientDto.class);
            return new BookingRequestClientDto()
                    .bookingType(bookingInAdvanceDto.getBookingType())
                    .chargingStationId(bookingInAdvanceDto.getChargingStationId())
                    .customerId(bookingInAdvanceDto.getCustomerId())
                    .socketType(SocketTypeClientDto.fromValue(bookingInAdvanceDto.getSocketType().getValue()))
                    .timeframe(timeframeClientDto);
        }else if(bookingRequestDto instanceof BookingOnTheFlyDto bookingOnTheFlyDto){
            return new BookingRequestClientDto()
                    .bookingType(bookingOnTheFlyDto.getBookingType())
                    .chargingStationId(bookingOnTheFlyDto.getChargingStationId())
                    .customerId(bookingOnTheFlyDto.getCustomerId())
                    .chargingPointId(bookingOnTheFlyDto.getChargingPointId())
                    .socketId(bookingOnTheFlyDto.getSocketId());
        }else{
            throw new IllegalArgumentException(String.format(
                    "Booking type %s is not supported",
                    bookingRequestDto.getBookingType()
            ));
        }
    }

    public static BookingStatusDto buildBookingStatusDto(BookingStatus bookingStatus){
        switch (bookingStatus.getBookingStatus()){
            case BookingStatusPlanned -> {
                return new BookingStatusPlannedDto();
            }
            case BookingStatusInProgress -> {
                return new BookingStatusInProgressDto()
                                .expectedMinutesLeft(bookingStatus.getProgressInformation()
                                .expectedMinutesLeft());
            }
            case BookingStatusCancelled -> {
                return new BookingStatusCancelledDto();
            }
            case BookingStatusCompleted -> {
                return new BookingStatusCompletedDto();
            }
            case BookingStatusExpired -> {
                return new BookingStatusExpiredDto();
            }
            default -> throw new IllegalArgumentException(String.format("Booking status %s not supported", bookingStatus.getBookingStatus()));
        }
    }
}
