package it.polimi.emall.emsp.bookingmanagementservice.model.booking;

import org.apache.commons.collections.set.UnmodifiableSet;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum BookingStatusEnum {
    BookingStatusPlanned(Set.of()),
    BookingStatusInProgress(Set.of(BookingStatusPlanned)),
    BookingStatusCompleted(Set.of(BookingStatusInProgress)),
    BookingStatusExpired(Set.of(BookingStatusPlanned)),
    BookingStatusCancelled(Set.of(BookingStatusPlanned))
;

    private final Set<BookingStatusEnum> parents;
    BookingStatusEnum(Set<BookingStatusEnum> parents){
        this.parents = Collections.unmodifiableSet(parents);
    }
    public Set<BookingStatusEnum> getParents(){
        return parents;
    }
}
