package ru.rsoi.rentservice;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.rsoi.rentservice.domain.Rent;
import ru.rsoi.rentservice.model.RentInfo;
import ru.rsoi.rentservice.model.RentResponse;
import ru.rsoi.rentservice.repository.RentRepository;
import ru.rsoi.rentservice.service.RentService;
import ru.rsoi.rentservice.service.RentServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RentServiceTests {

	private static final Integer ID = 19;
	private static final Integer ID2 = 2;
	private static final UUID CINEMAUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
	private static final UUID MOVIEUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440002");
	private static final UUID MOVIEUUID2 = UUID.fromString("123e4567-e89b-12d3-a456-426655440003");
	private static final Integer DURATION = 5;
	private static final Integer DURATION1 = 6;
	private static final Integer PRICE = 5;
	private static final Integer PRICE1 = 6;

	@Test
	void addRentTest() {
		final RentInfo rentInfo = new RentInfo(ID, CINEMAUUID, MOVIEUUID, DURATION, PRICE, false);
		final RentResponse rentResponse = new RentResponse(null, DURATION, PRICE, false);
		final Rent rent = new Rent()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setMovieUid(MOVIEUUID)
				.setDuration(DURATION)
				.setPrice(PRICE)
				.setConfirmed(false);

		RentRepository rentRepository = mock(RentRepository.class);
		RentService movieService = new RentServiceImpl(rentRepository);
		when(rentRepository.save(rent)).thenReturn(rent);
		assertEquals(movieService.addRent(rentInfo), rentResponse);
	}

	@Test
	void updateRentTest() {
		RentInfo rentInfo = new RentInfo(ID, CINEMAUUID, MOVIEUUID, DURATION, PRICE, false);
		Rent rent = new Rent()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setMovieUid(MOVIEUUID)
				.setDuration(DURATION)
				.setPrice(PRICE)
				.setConfirmed(false);

		RentRepository rentRepository = mock(RentRepository.class);
		RentService movieService = new RentServiceImpl(rentRepository);
		when(rentRepository.findById(ID)).thenReturn(Optional.of(rent));
		when(rentRepository.save(rent)).thenReturn(rent);
		assertEquals(movieService.updateRent(ID), rentInfo);
	}

	@Test
	void updateDurationTest() {
		RentInfo rentInfo = new RentInfo(ID, CINEMAUUID, MOVIEUUID, DURATION1, PRICE1, false);
		Rent rent = new Rent()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setMovieUid(MOVIEUUID)
				.setDuration(DURATION)
				.setPrice(PRICE)
				.setConfirmed(false);

		RentRepository rentRepository = mock(RentRepository.class);
		RentService movieService = new RentServiceImpl(rentRepository);
		when(rentRepository.findById(ID)).thenReturn(Optional.of(rent));
		when(rentRepository.save(rent)).thenReturn(rent);
		assertEquals(movieService.updateDuration(ID, DURATION1), rentInfo);
	}

	@Test
	void deleteRentTest() {
		RentInfo rentInfo = new RentInfo(ID, CINEMAUUID, MOVIEUUID, DURATION, PRICE, false);
		Rent rent = new Rent()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setMovieUid(MOVIEUUID)
				.setDuration(DURATION)
				.setPrice(PRICE)
				.setConfirmed(false);

		RentRepository rentRepository = mock(RentRepository.class);
		RentService movieService = new RentServiceImpl(rentRepository);
		when(rentRepository.findById(ID)).thenReturn(Optional.of(rent));
		assertEquals(movieService.deleteRent(ID), rentInfo);
	}
}
