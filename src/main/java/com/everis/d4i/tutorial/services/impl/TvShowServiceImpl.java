package com.everis.d4i.tutorial.services.impl;

import com.everis.d4i.tutorial.entities.Actor;
import com.everis.d4i.tutorial.entities.Award;
import com.everis.d4i.tutorial.entities.Chapter;
import com.everis.d4i.tutorial.entities.TvShow;
import com.everis.d4i.tutorial.exceptions.InternalServerErrorException;
import com.everis.d4i.tutorial.exceptions.NetflixException;
import com.everis.d4i.tutorial.exceptions.NotFoundException;
import com.everis.d4i.tutorial.json.TvShowRest;
import com.everis.d4i.tutorial.repositories.AwardRepository;
import com.everis.d4i.tutorial.repositories.ChapterRepository;
import com.everis.d4i.tutorial.repositories.TvShowRepository;
import com.everis.d4i.tutorial.services.TvShowService;
import com.everis.d4i.tutorial.utils.constants.ExceptionConstants;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class TvShowServiceImpl implements TvShowService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TvShowServiceImpl.class);

	private final ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private AwardRepository awardRepository;

	@Autowired
	private ChapterRepository chapterRepository;

	@Autowired
	private TvShowRepository tvShowRepository;

	@Override
	public List<TvShowRest> getTvShowsByCategory(Long categoryId) throws NetflixException {

		return tvShowRepository.findByCategoryId(categoryId).stream()
							   .map(tvShow -> modelMapper.map(tvShow, TvShowRest.class))
							   .collect(Collectors.toList());

	}

	@Override
	public TvShowRest getTvShowById(Long id) throws NetflixException {

		try {
			return modelMapper.map(tvShowRepository.getOne(id), TvShowRest.class);
		} catch (EntityNotFoundException entityNotFoundException) {
			throw new NotFoundException(entityNotFoundException.getMessage());
		}

	}

	@Override
	public TvShowRest patchTvShowName(Long tvShowId, TvShowRest tvShowRest)
			throws NetflixException {

		TvShow tvShow;
		try {
			tvShow = tvShowRepository.getOne(tvShowId);
		} catch (EntityNotFoundException entityNotFoundException) {
			throw new NotFoundException(entityNotFoundException.getMessage());
		}

		tvShow.setName(tvShowRest.getName());

		try {
			tvShow = tvShowRepository.save(tvShow);
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.INTERNAL_SERVER_ERROR, e);
			throw new InternalServerErrorException(ExceptionConstants.INTERNAL_SERVER_ERROR);
		}

		return modelMapper.map(tvShow, TvShowRest.class);
	}

	@Override
	public TvShowRest deleteById(Long id) throws NetflixException {
		List<Chapter> chapters = chapterRepository.findBySeasonTvShowId(id);
		for (Chapter chapter : chapters) {
			for (Actor actor : chapter.getActors()) {
				actor.getChapters().remove(chapter);
			}
			chapter.getActors().clear();
		}

		List<Award> awards = awardRepository.getAwardsByTvShowId(id);
		for (Award award : awards) {
			award.getTvShows().removeIf(tvShow -> tvShow.getId().equals(id));
		}
		tvShowRepository.getOne(id).getAwards().clear();

		try {
			tvShowRepository.deleteById(id);
		} catch (EmptyResultDataAccessException emptyResultDataAccessException) {
			throw new NotFoundException(ExceptionConstants.MESSAGE_INEXISTENT_TV_SHOW);
		} catch (Exception e) {
			LOGGER.error(ExceptionConstants.INTERNAL_SERVER_ERROR, e);
			throw new InternalServerErrorException(ExceptionConstants.INTERNAL_SERVER_ERROR);
		}

		return modelMapper.map(new TvShow(), TvShowRest.class);
	}
}
