package fr.ul.miage.resto.dao.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class BaseServiceTest extends AbstractServiceTest {
    @Test
    void testSaveWithError() {
        String obj = RandomStringUtils.randomAlphabetic(10);

        boolean response = baseService.save(obj);

        Assertions.assertFalse(response);
    }

    @Test
    void testUpdateWithError() {
        String obj = RandomStringUtils.randomAlphabetic(10);

        boolean response = baseService.update(obj);

        Assertions.assertFalse(response);
    }
}
