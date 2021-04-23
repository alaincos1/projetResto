package fr.ul.miage.projetResto.model.jackson;

import fr.ul.miage.projetResto.dao.repository.Mapper;
import fr.ul.miage.projetResto.model.jackson.serializer.EnumSerializer;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractJacksonTest {
    public EnumSerializer enumSerializer = new EnumSerializer();
    public EasyRandom easyRandom = new EasyRandom();
    public Mapper mapper;
}
