package it.polimi.emall.emsp.bookingmanagementservice.model.booking;

import it.polimi.emall.emsp.bookingmanagementservice.utils.Identifiable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class BookingStatus implements Identifiable<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_booking_status")
    private Long id;

    @PrimaryKeyJoinColumn(name = "booking")
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    private Booking booking;
    private BookingStatusEnum bookingStatus;
    @Convert(converter = ProgressInformationConverter.class)
    private ProgressInformation progressInformation;

    public BookingStatus(Booking booking){
        this.booking = booking;
        this.bookingStatus = BookingStatusEnum.BookingStatusPlanned;
        this.progressInformation = null;
    }

    void changeStatus(BookingStatusEnum desiredStatus){
        if(!bookingStatus.getParents().contains(desiredStatus))
            throw new IllegalArgumentException(String.format(
                    "Cannot put a booking in %s if it was %s",
                    desiredStatus,
                    bookingStatus)
            );
        bookingStatus = desiredStatus;
    }
    void changeProgressInformation(ProgressInformation progressInformation){
        if(bookingStatus != BookingStatusEnum.BookingStatusInProgress)
            throw new IllegalArgumentException("Cannot update progress information if the booking is not in progress");
        this.progressInformation = progressInformation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BookingStatus that = (BookingStatus) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(BookingStatus.class);
    }
}
