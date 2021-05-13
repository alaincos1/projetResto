package fr.ul.miage.resto.dao.service;

import fr.ul.miage.resto.model.entity.BookingEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class BaseService_BookingTest extends AbstractServiceTest {
    @Test
    void testSaveBooking() {
        BookingEntity bookingEntity = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.save(any())).thenReturn(true);

        boolean response = baseService.save(bookingEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testUpdateBooking() {
        BookingEntity bookingEntity = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.update(any())).thenReturn(true);

        boolean response = baseService.update(bookingEntity);

        Assertions.assertTrue(response);
    }

    @Test
    void testGetById() {
        BookingEntity expected = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.getBookingById(anyString())).thenReturn(expected);

        BookingEntity actual = baseService.getBookingById(expected.get_id());

        assertEqual(actual, expected);
    }

    @Test
    void testGetByIdWithError() {
        BookingEntity expected = easyRandom.nextObject(BookingEntity.class);

        when(bookingCollection.getBookingById(anyString())).thenReturn(null);

        BookingEntity actual = baseService.getBookingById(expected.get_id());

        Assertions.assertNull(actual);
    }

    @Test
    void testGetAllBooking() {
        List<BookingEntity> expected = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            expected.add(easyRandom.nextObject(BookingEntity.class));
        }

        when(baseService.getAllBooking()).thenReturn(expected);

        List<BookingEntity> actual = baseService.getAllBooking();

        assertEqual(actual, expected);
    }

    private void assertEqual(BookingEntity actual, BookingEntity expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.get_id(), expected.get_id());
        Assertions.assertEquals(actual.getReservationName(), expected.getReservationName());
        Assertions.assertEquals(actual.getIdTable(), expected.getIdTable());
        Assertions.assertEquals(actual.getMealType(), expected.getMealType());
        Assertions.assertEquals(actual.getDate(), expected.getDate());
    }

    private void assertEqual(List<BookingEntity> actual, List<BookingEntity> expected) {
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(actual.size(), expected.size());

        for (int i = 0; i < actual.size(); i++) {
            assertEqual(actual.get(i), expected.get(i));
        }
    }
}
