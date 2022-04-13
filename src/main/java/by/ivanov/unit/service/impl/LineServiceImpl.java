package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.Line;
import by.ivanov.unit.repository.LineRepository;
import by.ivanov.unit.service.LineService;
import by.ivanov.unit.service.dto.LineDTO;
import by.ivanov.unit.service.mapper.LineMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Line}.
 */
@Service
@Transactional
public class LineServiceImpl implements LineService {

    private final Logger log = LoggerFactory.getLogger(LineServiceImpl.class);

    private final LineRepository lineRepository;

    private final LineMapper lineMapper;

    public LineServiceImpl(LineRepository lineRepository, LineMapper lineMapper) {
        this.lineRepository = lineRepository;
        this.lineMapper = lineMapper;
    }

    @Override
    public LineDTO save(LineDTO lineDTO) {
        log.debug("Request to save Line : {}", lineDTO);
        Line line = lineMapper.toEntity(lineDTO);
        line = lineRepository.save(line);
        return lineMapper.toDto(line);
    }

    @Override
    public Optional<LineDTO> partialUpdate(LineDTO lineDTO) {
        log.debug("Request to partially update Line : {}", lineDTO);

        return lineRepository
            .findById(lineDTO.getId())
            .map(existingLine -> {
                lineMapper.partialUpdate(existingLine, lineDTO);

                return existingLine;
            })
            .map(lineRepository::save)
            .map(lineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Lines");
        return lineRepository.findAll(pageable).map(lineMapper::toDto);
    }

    public Page<LineDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lineRepository.findAllWithEagerRelationships(pageable).map(lineMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<LineDTO> findOne(Long id) {
        log.debug("Request to get Line : {}", id);
        return lineRepository.findOneWithEagerRelationships(id).map(lineMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Line : {}", id);
        lineRepository.deleteById(id);
    }
}
