package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.PunchItem;
import by.ivanov.unit.repository.PunchItemRepository;
import by.ivanov.unit.service.PunchItemService;
import by.ivanov.unit.service.dto.PunchItemDTO;
import by.ivanov.unit.service.mapper.PunchItemMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PunchItem}.
 */
@Service
@Transactional
public class PunchItemServiceImpl implements PunchItemService {

    private final Logger log = LoggerFactory.getLogger(PunchItemServiceImpl.class);

    private final PunchItemRepository punchItemRepository;

    private final PunchItemMapper punchItemMapper;

    public PunchItemServiceImpl(PunchItemRepository punchItemRepository, PunchItemMapper punchItemMapper) {
        this.punchItemRepository = punchItemRepository;
        this.punchItemMapper = punchItemMapper;
    }

    @Override
    public PunchItemDTO save(PunchItemDTO punchItemDTO) {
        log.debug("Request to save PunchItem : {}", punchItemDTO);
        PunchItem punchItem = punchItemMapper.toEntity(punchItemDTO);
        punchItem = punchItemRepository.save(punchItem);
        return punchItemMapper.toDto(punchItem);
    }

    @Override
    public PunchItemDTO update(PunchItemDTO punchItemDTO) {
        log.debug("Request to save PunchItem : {}", punchItemDTO);
        PunchItem punchItem = punchItemMapper.toEntity(punchItemDTO);
        punchItem = punchItemRepository.save(punchItem);
        return punchItemMapper.toDto(punchItem);
    }

    @Override
    public Optional<PunchItemDTO> partialUpdate(PunchItemDTO punchItemDTO) {
        log.debug("Request to partially update PunchItem : {}", punchItemDTO);

        return punchItemRepository
            .findById(punchItemDTO.getId())
            .map(existingPunchItem -> {
                punchItemMapper.partialUpdate(existingPunchItem, punchItemDTO);

                return existingPunchItem;
            })
            .map(punchItemRepository::save)
            .map(punchItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PunchItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PunchItems");
        return punchItemRepository.findAll(pageable).map(punchItemMapper::toDto);
    }

    public Page<PunchItemDTO> findAllWithEagerRelationships(Pageable pageable) {
        return punchItemRepository.findAllWithEagerRelationships(pageable).map(punchItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PunchItemDTO> findOne(Long id) {
        log.debug("Request to get PunchItem : {}", id);
        return punchItemRepository.findOneWithEagerRelationships(id).map(punchItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PunchItem : {}", id);
        punchItemRepository.deleteById(id);
    }
}
