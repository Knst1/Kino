package ru.rsoi.cinemaservice;

import org.junit.jupiter.api.Test;
import ru.rsoi.cinemaservice.domain.Cinema;
import ru.rsoi.cinemaservice.model.CinemaInfo;
import ru.rsoi.cinemaservice.repository.CinemaRepository;
import ru.rsoi.cinemaservice.service.CinemaService;
import ru.rsoi.cinemaservice.service.CinemaServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CinemaServiceTests {

	private static final Integer ID = 19;
	private static final UUID CINEMAUUID = UUID.fromString("123e4567-e89b-12d3-a456-426655440000");
	private static final String NAME = "NAME";
	private static final String CHIEF = "CHIEF";
	private static final String OWNER = "OWNER";
	private static final String PHONE = "+79999999999";
	private static final Integer COUNT = 0;
	private static final String ADDRESS = "ADDRESS";

	@Test
	void getCinemaInfoTest() {
		final CinemaInfo cinemaInfo = new CinemaInfo(CINEMAUUID, NAME, CHIEF, OWNER, PHONE, COUNT, ADDRESS);
		final Cinema cinema = new Cinema()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setName(NAME)
				.setChief(CHIEF)
				.setOwner(OWNER)
				.setPhone(PHONE)
				.setCount(COUNT)
				.setAddress(ADDRESS);

		CinemaRepository cinemaRepository = mock(CinemaRepository.class);
		CinemaService cinemaService = new CinemaServiceImpl(cinemaRepository);
		when(cinemaRepository.findById(ID)).thenReturn(Optional.of(cinema));
		assertEquals(cinemaService.getCinemaInfo(ID), cinemaInfo);
	}

	@Test
	void getCinemaUuidTest() {
		final Cinema cinema = new Cinema()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setName(NAME)
				.setChief(CHIEF)
				.setOwner(OWNER)
				.setPhone(PHONE)
				.setCount(COUNT)
				.setAddress(ADDRESS);

		CinemaRepository cinemaRepository = mock(CinemaRepository.class);
		CinemaService cinemaService = new CinemaServiceImpl(cinemaRepository);
		when(cinemaRepository.findById(ID)).thenReturn(Optional.of(cinema));
		assertEquals(cinemaService.getCinemaUuid(ID), CINEMAUUID);
	}

	@Test
	void incCountTest() {
		final Cinema cinema = new Cinema()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setName(NAME)
				.setChief(CHIEF)
				.setOwner(OWNER)
				.setPhone(PHONE)
				.setCount(COUNT)
				.setAddress(ADDRESS);

		CinemaRepository cinemaRepository = mock(CinemaRepository.class);
		CinemaService cinemaService = new CinemaServiceImpl(cinemaRepository);
		when(cinemaRepository.findById(ID)).thenReturn(Optional.of(cinema));
		when(cinemaRepository.save(cinema)).thenReturn(cinema);
		assertEquals(cinemaService.incCount(ID), ID);
	}

	@Test
	void decCountTest() {
		final Cinema cinema = new Cinema()
				.setId(ID)
				.setCinemaUid(CINEMAUUID)
				.setName(NAME)
				.setChief(CHIEF)
				.setOwner(OWNER)
				.setPhone(PHONE)
				.setCount(COUNT)
				.setAddress(ADDRESS);

		CinemaRepository cinemaRepository = mock(CinemaRepository.class);
		CinemaService cinemaService = new CinemaServiceImpl(cinemaRepository);
		when(cinemaRepository.findByCinemaUid(CINEMAUUID)).thenReturn(Optional.of(cinema));
		when(cinemaRepository.save(cinema)).thenReturn(cinema);
		assertEquals(cinemaService.decCount(CINEMAUUID), ID);
	}
}
