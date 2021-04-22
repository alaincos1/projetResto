package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.model.entity.BookingEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class BaseService_BookingTest extends AbstractServiceTest {
    @Test
    public void testSaveBooking() {
        BookingEntity bookingEntity = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(bookingEntity);

        Assertions.assertTrue(response);
    }

    @Test
    public void testGetById() {
        BookingEntity expected = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.getBookingById(anyString())).thenReturn(expected);

        BookingEntity actual = baseService.getBookingById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    public void testGetByIdWithError() {
        BookingEntity expected = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.getBookingById(anyString())).thenReturn(null);

        BookingEntity actual = baseService.getBookingById(expected.get_id());

        Assertions.assertNull(actual);
    }

    private void assertEqual(BookingEntity actual, BookingEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getReservationName(), expected.getReservationName());
        Assertions.assertEquals(actual.getIdTable(), expected.getIdTable());
        Assertions.assertEquals(actual.getMealType(), expected.getMealType());
    }
}
