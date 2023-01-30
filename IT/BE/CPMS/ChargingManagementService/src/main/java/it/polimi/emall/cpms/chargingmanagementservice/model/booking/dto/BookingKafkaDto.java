package it.polimi.emall.cpms.chargingmanagementservice.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingStatusEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingTypeEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.TimeFrame;

public class BookingKafkaDto {
    @JsonProperty("bookingId")
    public final Long bookingId;

    @JsonProperty("bookingCode")
    public final String bookingCode;

    @JsonProperty("customerId")
    public final Long customerId;

    @JsonProperty("chargingStationId")
    public final Long chargingStationId;

    @JsonProperty("chargingPointId")
    public final Long chargingPointId;

    @JsonProperty("socketId")
    public final Long socketId;

    @JsonProperty("timeFrame")
    public final TimeFrame timeFrame;

    @JsonProperty("bookingType")
    public final BookingTypeEnum bookingType;

    @JsonProperty("status")
    public final BookingStatusEnum status;

    public BookingKafkaDto(
            Long bookingId, String bookingCode, Long customerId,
            Long chargingStationId, Long chargingPointId, Long socketId,
            TimeFrame timeFrame, BookingTypeEnum bookingType, BookingStatusEnum status
    ) {
        this.bookingId = bookingId;
        this.bookingCode = bookingCode;
        this.customerId = customerId;
        this.chargingStationId = chargingStationId;
        this.chargingPointId = chargingPointId;
        this.socketId = socketId;
        this.timeFrame = timeFrame;
        this.bookingType = bookingType;
        this.status = status;
    }
}
