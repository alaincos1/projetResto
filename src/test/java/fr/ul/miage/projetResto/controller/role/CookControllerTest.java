package fr.ul.miage.projetResto.controller.role;

import fr.ul.miage.projetResto.constants.Role;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import fr.ul.miage.projetResto.view.role.CookView;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@DisplayName("CookController")
@ExtendWith(MockitoExtension.class)
public class CookControllerTest {

    @Mock
    CookView cookView;

    @Mock
    BaseService baseService;

    @Spy
    @InjectMocks
    CookController cookController;

	@Test
    @DisplayName("Aucune commande à afficher")
    void checkViewOrdersListsNone() {
        List<OrderEntity> orders = new ArrayList<>();
        when(baseService.getNotPreparedOrders()).thenReturn(orders);
        doNothing().when(cookController).launch(any(Role.class));
        cookController.viewOrdersList();
		verify(cookView, times(1)).displayNoOrderToPrepare();
    }
	
}
