package fr.ul.miage.projetResto.dao.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ServiceTest extends AbstractServiceTest {
    @Test
    public void testSaveWithError() {
        String obj = RandomStringUtils.randomAlphabetic(10);

        boolean response = service.save(obj);

        Assertions.assertFalse(response);
    }
}
