package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.Block;
import by.ivanov.unit.repository.BlockRepository;
import by.ivanov.unit.service.BlockService;
import by.ivanov.unit.service.dto.BlockDTO;
import by.ivanov.unit.service.mapper.BlockMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Block}.
 */
@Service
@Transactional
public class BlockServiceImpl implements BlockService {

    private final Logger log = LoggerFactory.getLogger(BlockServiceImpl.class);

    private final BlockRepository blockRepository;

    private final BlockMapper blockMapper;

    public BlockServiceImpl(BlockRepository blockRepository, BlockMapper blockMapper) {
        this.blockRepository = blockRepository;
        this.blockMapper = blockMapper;
    }

    @Override
    public BlockDTO save(BlockDTO blockDTO) {
        log.debug("Request to save Block : {}", blockDTO);
        Block block = blockMapper.toEntity(blockDTO);
        block = blockRepository.save(block);
        return blockMapper.toDto(block);
    }

    @Override
    public BlockDTO update(BlockDTO blockDTO) {
        log.debug("Request to save Block : {}", blockDTO);
        Block block = blockMapper.toEntity(blockDTO);
        block = blockRepository.save(block);
        return blockMapper.toDto(block);
    }

    @Override
    public Optional<BlockDTO> partialUpdate(BlockDTO blockDTO) {
        log.debug("Request to partially update Block : {}", blockDTO);

        return blockRepository
            .findById(blockDTO.getId())
            .map(existingBlock -> {
                blockMapper.partialUpdate(existingBlock, blockDTO);

                return existingBlock;
            })
            .map(blockRepository::save)
            .map(blockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlockDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Blocks");
        return blockRepository.findAll(pageable).map(blockMapper::toDto);
    }

    public Page<BlockDTO> findAllWithEagerRelationships(Pageable pageable) {
        return blockRepository.findAllWithEagerRelationships(pageable).map(blockMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BlockDTO> findOne(Long id) {
        log.debug("Request to get Block : {}", id);
        return blockRepository.findOneWithEagerRelationships(id).map(blockMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Block : {}", id);
        blockRepository.deleteById(id);
    }
}
