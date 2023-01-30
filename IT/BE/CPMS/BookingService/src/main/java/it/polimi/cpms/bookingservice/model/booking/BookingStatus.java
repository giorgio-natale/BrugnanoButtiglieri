package it.polimi.cpms.bookingservice.model.booking;

import it.polimi.cpms.bookingservice.utils.Identifiable;
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
    private Long id;
    @Enumerated(EnumType.STRING)
    private BookingStatusEnum bookingStatus;
    @Convert(converter = ProgressInformationConverter.class)
    private ProgressInformation progressInformation;

    public BookingStatus(Long id){
        this.id = id;
        this.bookingStatus = BookingStatusEnum.BookingStatusPlanned;
    }

    void changeStatus(BookingStatusEnum desiredStatus){
        if(desiredStatus != bookingStatus && !desiredStatus.getParents().contains(bookingStatus))
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
