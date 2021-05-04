package fr.ul.miage.projetResto.dao.service;

import fr.ul.miage.projetResto.dao.repository.*;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceTest {
    EasyRandom easyRandom = new EasyRandom();

    @Mock
    BillCollection billCollection;

    @Mock
    BookingCollection bookingCollection;

    @Mock
    DishCollection dishCollection;

    @Mock
    CategoryCollection categoryCollection;

    @Mock
    OrderCollection orderCollection;

    @Mock
    PerformanceCollection performanceCollection;

    @Mock
    ProductCollection productCollection;

    @Mock
    TableCollection tableCollection;

    @Mock
    UserCollection userCollection;

    @Spy
    @InjectMocks
    BaseService baseService;
}
