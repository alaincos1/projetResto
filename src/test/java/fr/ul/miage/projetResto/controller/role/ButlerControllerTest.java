package fr.ul.miage.projetResto.controller.role;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import fr.ul.miage.projetResto.Launcher;
import fr.ul.miage.projetResto.appinfo.Service;
import fr.ul.miage.projetResto.constants.MealType;
import fr.ul.miage.projetResto.constants.TableState;
import fr.ul.miage.projetResto.model.entity.TableEntity;
import fr.ul.miage.projetResto.model.entity.UserEntity;

import fr.ul.miage.projetResto.constants.OrderState;
import fr.ul.miage.projetResto.dao.service.BaseService;
import fr.ul.miage.projetResto.model.entity.BookingEntity;
import fr.ul.miage.projetResto.model.entity.OrderEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ButlerController")
public class ButlerControllerTest {
	@Mock
	Service service;

	@Mock
	Launcher launcher;

//	@Mock 
//	Service service;

	@InjectMocks
	ButlerController butlerController = new ButlerController();

	@Test
	void checkReturnMealTypeWithDate() {
		String dateBooking = "2021/04/29";
		String dateToday = "2021/04/29";

		MockedStatic mocked = mockStatic(Launcher.class);
		MockedStatic serviceMock = mockStatic(Service.class);
		
		// Mocking
		mocked.when(Launcher::getService).thenReturn(service);
        when(Launcher.getService().getDate()).thenReturn(dateToday);
        when(Launcher.getService().getMealType()).thenReturn(MealType.Déjeuner);

		MealType toTested = butlerController.choiceMealTypeWithDate(dateBooking);

		assertEquals(toTested, MealType.Dîner);
	}

	@Test
	void choiceMealTypeTestDejeuner() {
		MealType test = ButlerController.choiceMealType(0);
		assertEquals(MealType.Déjeuner, test);
	}
	
	@Test
	void choiceMealTypeTestDiner() {
		MealType test = ButlerController.choiceMealType(1);
		assertEquals(MealType.Dîner, test);
	}

	@Test
	void isTableIdCorrectTestReturnTrue() {
		boolean test = ButlerController.isTableIdCorrect("1", TableState.Free);
		assertTrue(test);
	}
	
//	
//	@Test
//	private String choiceTable(TableState state) {
//	}
//
//	@Test
//	private String choiceTableServer(UserEntity user) {
//	}
//
//	@Test
//	private boolean isTableIdCorrectServer(String tableId, UserEntity user) {
//	}
//
//	@Test
//	private String choiceReservation(List<TableEntity> tables, Integer reservation) {
//	}

}
