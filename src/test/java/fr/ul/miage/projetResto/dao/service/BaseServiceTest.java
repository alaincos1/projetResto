package fr.ul.miage.projetResto.dao.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class BaseServiceTest extends AbstractServiceTest {
    @Test
    public void testSaveWithError() {
        String obj = RandomStringUtils.randomAlphabetic(10);

        boolean response = baseService.save(obj);

        Assertions.assertFalse(response);
    }

    @Test
    public void testUpdateWithError() {
        String obj = RandomStringUtils.randomAlphabetic(10);

        boolean response = baseService.update(obj);

        Assertions.assertFalse(response);
    }
}
