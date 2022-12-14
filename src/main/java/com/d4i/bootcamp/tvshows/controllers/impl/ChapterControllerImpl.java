package com.d4i.bootcamp.tvshows.controllers.impl;

import com.d4i.bootcamp.tvshows.json.ChapterRest;
import com.d4i.bootcamp.tvshows.controllers.ChapterController;
import com.d4i.bootcamp.tvshows.exceptions.D4iBootcampException;
import com.d4i.bootcamp.tvshows.responses.D4iBootcampResponse;
import com.d4i.bootcamp.tvshows.services.ChapterService;
import com.d4i.bootcamp.tvshows.utils.constants.CommonConstants;
import com.d4i.bootcamp.tvshows.utils.constants.RestConstants;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(RestConstants.APPLICATION_NAME + RestConstants.API_VERSION_1 + RestConstants.RESOURCE_CHAPTER)
public class ChapterControllerImpl implements ChapterController {

	@Autowired
	private ChapterService chapterService;

	@Override
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public D4iBootcampResponse<List<ChapterRest>> getChaptersByTvShowIdAndSeasonNumber(
			@PathVariable Long tvShowId, @PathVariable short seasonNumber)
			throws D4iBootcampException {

		return new D4iBootcampResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK),
				CommonConstants.OK,
				chapterService.getChaptersByTvShowIdAndSeasonNumber(tvShowId, seasonNumber));
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@GetMapping(value = RestConstants.RESOURCE_NUMBER, produces = MediaType.APPLICATION_JSON_VALUE)
	public D4iBootcampResponse<ChapterRest> getChapterByTvShowIdAndSeasonNumberAndChapterNumber(
			@PathVariable Long tvShowId, @PathVariable short seasonNumber,
			@PathVariable short number) throws D4iBootcampException {

		return new D4iBootcampResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK),
				CommonConstants.OK,
				chapterService.getChapterByTvShowIdAndSeasonNumberAndChapterNumber(tvShowId,
						seasonNumber, number));
	}

	@Override
	@ResponseStatus(HttpStatus.OK)
	@PatchMapping(value = RestConstants.RESOURCE_ID, produces = MediaType.APPLICATION_JSON_VALUE)
	public D4iBootcampResponse<ChapterRest> patchChapterName(@PathVariable Long tvShowId,
			@PathVariable short seasonNumber, @PathVariable Long id,
			@ApiParam(value = RestConstants.PARAMETER_TV_CHAPTER, required = true) @RequestBody @Valid ChapterRest chapterRest)
			throws D4iBootcampException {

		return new D4iBootcampResponse<>(CommonConstants.SUCCESS, String.valueOf(HttpStatus.OK),
				CommonConstants.OK,
				chapterService.patchChapterName(tvShowId, seasonNumber, id, chapterRest));
	}
}
