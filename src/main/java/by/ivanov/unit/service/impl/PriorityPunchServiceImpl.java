package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.PriorityPunch;
import by.ivanov.unit.repository.PriorityPunchRepository;
import by.ivanov.unit.service.PriorityPunchService;
import by.ivanov.unit.service.dto.PriorityPunchDTO;
import by.ivanov.unit.service.mapper.PriorityPunchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PriorityPunch}.
 */
@Service
@Transactional
public class PriorityPunchServiceImpl implements PriorityPunchService {

    private final Logger log = LoggerFactory.getLogger(PriorityPunchServiceImpl.class);

    private final PriorityPunchRepository priorityPunchRepository;

    private final PriorityPunchMapper priorityPunchMapper;

    public PriorityPunchServiceImpl(PriorityPunchRepository priorityPunchRepository, PriorityPunchMapper priorityPunchMapper) {
        this.priorityPunchRepository = priorityPunchRepository;
        this.priorityPunchMapper = priorityPunchMapper;
    }

    @Override
    public PriorityPunchDTO save(PriorityPunchDTO priorityPunchDTO) {
        log.debug("Request to save PriorityPunch : {}", priorityPunchDTO);
        PriorityPunch priorityPunch = priorityPunchMapper.toEntity(priorityPunchDTO);
        priorityPunch = priorityPunchRepository.save(priorityPunch);
        return priorityPunchMapper.toDto(priorityPunch);
    }

    @Override
    public Optional<PriorityPunchDTO> partialUpdate(PriorityPunchDTO priorityPunchDTO) {
        log.debug("Request to partially update PriorityPunch : {}", priorityPunchDTO);

        return priorityPunchRepository
            .findById(priorityPunchDTO.getId())
            .map(existingPriorityPunch -> {
                priorityPunchMapper.partialUpdate(existingPriorityPunch, priorityPunchDTO);

                return existingPriorityPunch;
            })
            .map(priorityPunchRepository::save)
            .map(priorityPunchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PriorityPunchDTO> findAll() {
        log.debug("Request to get all PriorityPunches");
        return priorityPunchRepository.findAll().stream().map(priorityPunchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriorityPunchDTO> findOne(Long id) {
        log.debug("Request to get PriorityPunch : {}", id);
        return priorityPunchRepository.findById(id).map(priorityPunchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PriorityPunch : {}", id);
        priorityPunchRepository.deleteById(id);
    }
}
