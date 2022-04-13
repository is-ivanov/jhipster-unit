package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.PunchList;
import by.ivanov.unit.repository.PunchListRepository;
import by.ivanov.unit.service.PunchListService;
import by.ivanov.unit.service.dto.PunchListDTO;
import by.ivanov.unit.service.mapper.PunchListMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PunchList}.
 */
@Service
@Transactional
public class PunchListServiceImpl implements PunchListService {

    private final Logger log = LoggerFactory.getLogger(PunchListServiceImpl.class);

    private final PunchListRepository punchListRepository;

    private final PunchListMapper punchListMapper;

    public PunchListServiceImpl(PunchListRepository punchListRepository, PunchListMapper punchListMapper) {
        this.punchListRepository = punchListRepository;
        this.punchListMapper = punchListMapper;
    }

    @Override
    public PunchListDTO save(PunchListDTO punchListDTO) {
        log.debug("Request to save PunchList : {}", punchListDTO);
        PunchList punchList = punchListMapper.toEntity(punchListDTO);
        punchList = punchListRepository.save(punchList);
        return punchListMapper.toDto(punchList);
    }

    @Override
    public PunchListDTO update(PunchListDTO punchListDTO) {
        log.debug("Request to save PunchList : {}", punchListDTO);
        PunchList punchList = punchListMapper.toEntity(punchListDTO);
        punchList = punchListRepository.save(punchList);
        return punchListMapper.toDto(punchList);
    }

    @Override
    public Optional<PunchListDTO> partialUpdate(PunchListDTO punchListDTO) {
        log.debug("Request to partially update PunchList : {}", punchListDTO);

        return punchListRepository
            .findById(punchListDTO.getId())
            .map(existingPunchList -> {
                punchListMapper.partialUpdate(existingPunchList, punchListDTO);

                return existingPunchList;
            })
            .map(punchListRepository::save)
            .map(punchListMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PunchListDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PunchLists");
        return punchListRepository.findAll(pageable).map(punchListMapper::toDto);
    }

    public Page<PunchListDTO> findAllWithEagerRelationships(Pageable pageable) {
        return punchListRepository.findAllWithEagerRelationships(pageable).map(punchListMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PunchListDTO> findOne(Long id) {
        log.debug("Request to get PunchList : {}", id);
        return punchListRepository.findOneWithEagerRelationships(id).map(punchListMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PunchList : {}", id);
        punchListRepository.deleteById(id);
    }
}
