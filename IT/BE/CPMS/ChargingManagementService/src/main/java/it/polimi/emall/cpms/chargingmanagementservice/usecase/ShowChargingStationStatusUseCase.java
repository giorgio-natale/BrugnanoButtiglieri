package it.polimi.emall.cpms.chargingmanagementservice.usecase;

import it.polimi.emall.cpms.chargingmanagementservice.generated.http.client.cpms_mockingservice.model.SocketTypeClientDto;
import it.polimi.emall.cpms.chargingmanagementservice.generated.http.server.model.*;
import it.polimi.emall.cpms.chargingmanagementservice.mapper.SocketForDashboardMapper;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.Booking;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingManager;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingStatusEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.booking.BookingTypeEnum;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatus;
import it.polimi.emall.cpms.chargingmanagementservice.model.socketstatus.SocketCurrentStatusManager;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ShowChargingStationStatusUseCase {

    private final SocketCurrentStatusManager socketCurrentStatusManager;
    private final BookingManager bookingManager;

    public ShowChargingStationStatusUseCase(
            SocketCurrentStatusManager socketCurrentStatusManager,
            BookingManager bookingManager
    ) {
        this.socketCurrentStatusManager = socketCurrentStatusManager;
        this.bookingManager = bookingManager;
    }

    public SocketStatusDto getSocketStatus(Long socketId){
        SocketCurrentStatus socketCurrentStatus = socketCurrentStatusManager.getEntityByKey(socketId);
        return SocketForDashboardMapper.buildSocketStatusDto(socketCurrentStatus);
    }

    public ChargingStationStatusDto getChargingStationStatus(Long chargingStationId){

        OffsetDateTime now = OffsetDateTime.now();
        Map<Long, Booking> bookings =
                bookingManager.getBookingsByChargingStation(chargingStationId)
                        .stream()
                        .filter(booking -> booking.getBookingStatus().equals(BookingStatusEnum.BookingStatusPlanned) || booking.getBookingStatus().equals(BookingStatusEnum.BookingStatusInProgress))
                        .filter(booking -> booking.getTimeFrame().contains(now))
                        .collect(Collectors.toMap(
                                Booking::getSocketId,
                                booking -> booking
                        ));

        Set<SocketCurrentStatus> socketCurrentStatuses = socketCurrentStatusManager
                .getAllSocketStatusesOfChargingStation(chargingStationId);

        Map<SocketTypeClientDto, SocketTypeAvailabilityOverviewDto> overviewInformation =
                new HashMap<>(Map.of(
                        SocketTypeClientDto.SLOW, new SocketTypeAvailabilityOverviewDto(0, 0, Integer.MAX_VALUE),
                        SocketTypeClientDto.FAST, new SocketTypeAvailabilityOverviewDto(0, 0, Integer.MAX_VALUE),
                        SocketTypeClientDto.RAPID, new SocketTypeAvailabilityOverviewDto(0, 0, Integer.MAX_VALUE)
                ));

        socketCurrentStatuses.forEach(socketCurrentStatus -> {
            Optional<Booking> associatedBooking = Optional.ofNullable(bookings.get(socketCurrentStatus.getId()));
            var oldAvailability = overviewInformation.get(socketCurrentStatus.getSocketType());
            oldAvailability.setTotalSocketNumber(oldAvailability.getTotalSocketNumber() + 1);
            associatedBooking.ifPresentOrElse(
                    booking -> {
                        if(booking.getBookingType().equals(BookingTypeEnum.ON_THE_FLY)){
                            oldAvailability.setNearestExpectedMinutesLeft(
                                    Math.min(
                                            oldAvailability.getNearestExpectedMinutesLeft(),
                                            socketCurrentStatus.getProgressInformation() == null ? Integer.MAX_VALUE : socketCurrentStatus.getProgressInformation().expectedMinutesLeft()
                                    )
                            );
                        }else{
                            oldAvailability.setNearestExpectedMinutesLeft(
                                    Math.min(
                                            oldAvailability.getNearestExpectedMinutesLeft(),
                                            (int) Duration.between(now, booking.getTimeFrame().getEndInstant()).toMinutes()
                                    )
                            );
                        }
                    },
                    () -> {
                        oldAvailability.setNearestExpectedMinutesLeft(0);
                        oldAvailability.setAvailableSocketNumber(oldAvailability.getAvailableSocketNumber() + 1);
                    }
            );
        });

        Map<Long, List<SocketCurrentStatus>> socketStatusListByChargingPointId =
                socketCurrentStatuses.stream()
                        .sorted(Comparator.comparingLong(SocketCurrentStatus::getId))
                        .collect(Collectors.groupingBy(
                                SocketCurrentStatus::getChargingPointId,
                                Collectors.toList()
                        ));

        List<ChargingPointDto> chargingPointDtos =
                socketStatusListByChargingPointId.entrySet()
                        .stream()
                        .sorted(Comparator.comparingLong(Map.Entry::getKey))
                        .map(entry -> {
                            List<SocketCurrentStatus> socketCurrentStatusList = entry.getValue();
                            return new ChargingPointDto(
                                    entry.getKey(),
                                    socketCurrentStatusList.get(0).getChargingPointCode(),
                                    ChargingPointModeDto.fromValue(socketCurrentStatusList.get(0).getChargingPointMode().getValue()),
                                    socketCurrentStatusList
                                            .stream()
                                            .map(SocketForDashboardMapper::buildSocketForDashboardDto)
                                            .collect(Collectors.toList())
                            );
                        }).toList();
        AvailabilityOverviewDto availabilityOverviewDto = new AvailabilityOverviewDto(
                overviewInformation.get(SocketTypeClientDto.SLOW),
                overviewInformation.get(SocketTypeClientDto.FAST),
                overviewInformation.get(SocketTypeClientDto.RAPID)
        );

        return new ChargingStationStatusDto(availabilityOverviewDto, chargingPointDtos);
    }
}
