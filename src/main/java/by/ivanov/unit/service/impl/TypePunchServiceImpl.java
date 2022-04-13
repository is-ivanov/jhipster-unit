package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.TypePunch;
import by.ivanov.unit.repository.TypePunchRepository;
import by.ivanov.unit.service.TypePunchService;
import by.ivanov.unit.service.dto.TypePunchDTO;
import by.ivanov.unit.service.mapper.TypePunchMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link TypePunch}.
 */
@Service
@Transactional
public class TypePunchServiceImpl implements TypePunchService {

    private final Logger log = LoggerFactory.getLogger(TypePunchServiceImpl.class);

    private final TypePunchRepository typePunchRepository;

    private final TypePunchMapper typePunchMapper;

    public TypePunchServiceImpl(TypePunchRepository typePunchRepository, TypePunchMapper typePunchMapper) {
        this.typePunchRepository = typePunchRepository;
        this.typePunchMapper = typePunchMapper;
    }

    @Override
    public TypePunchDTO save(TypePunchDTO typePunchDTO) {
        log.debug("Request to save TypePunch : {}", typePunchDTO);
        TypePunch typePunch = typePunchMapper.toEntity(typePunchDTO);
        typePunch = typePunchRepository.save(typePunch);
        return typePunchMapper.toDto(typePunch);
    }

    @Override
    public TypePunchDTO update(TypePunchDTO typePunchDTO) {
        log.debug("Request to save TypePunch : {}", typePunchDTO);
        TypePunch typePunch = typePunchMapper.toEntity(typePunchDTO);
        typePunch = typePunchRepository.save(typePunch);
        return typePunchMapper.toDto(typePunch);
    }

    @Override
    public Optional<TypePunchDTO> partialUpdate(TypePunchDTO typePunchDTO) {
        log.debug("Request to partially update TypePunch : {}", typePunchDTO);

        return typePunchRepository
            .findById(typePunchDTO.getId())
            .map(existingTypePunch -> {
                typePunchMapper.partialUpdate(existingTypePunch, typePunchDTO);

                return existingTypePunch;
            })
            .map(typePunchRepository::save)
            .map(typePunchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TypePunchDTO> findAll() {
        log.debug("Request to get all TypePunches");
        return typePunchRepository.findAll().stream().map(typePunchMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TypePunchDTO> findOne(Long id) {
        log.debug("Request to get TypePunch : {}", id);
        return typePunchRepository.findById(id).map(typePunchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete TypePunch : {}", id);
        typePunchRepository.deleteById(id);
    }
}
