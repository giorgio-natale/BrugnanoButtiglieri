package it.polimi.cpms.bookingservice.model.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.polimi.cpms.bookingservice.model.booking.BookingStatusEnum;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.BookingTypeDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.SocketTypeDto;
import it.polimi.emall.cpms.bookingservice.generated.http.server.model.TimeframeDto;

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

    @JsonProperty("socketType")
    public final SocketTypeDto socketType;

    @JsonProperty("timeframe")
    public final TimeframeDto timeframe;

    @JsonProperty("bookingType")
    public final BookingTypeDto bookingType;

    @JsonProperty("status")
    public final BookingStatusEnum status;

    public BookingKafkaDto(Long bookingId, String bookingCode, Long customerId, Long chargingStationId, Long chargingPointId, Long socketId, SocketTypeDto socketType, TimeframeDto timeframe, BookingTypeDto bookingType, BookingStatusEnum status) {
        this.bookingId = bookingId;
        this.bookingCode = bookingCode;
        this.customerId = customerId;
        this.chargingStationId = chargingStationId;
        this.chargingPointId = chargingPointId;
        this.socketId = socketId;
        this.socketType = socketType;
        this.timeframe = timeframe;
        this.bookingType = bookingType;
        this.status = status;
    }
}
