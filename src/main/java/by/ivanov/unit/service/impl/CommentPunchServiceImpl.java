package by.ivanov.unit.service.impl;

import by.ivanov.unit.domain.CommentPunch;
import by.ivanov.unit.repository.CommentPunchRepository;
import by.ivanov.unit.service.CommentPunchService;
import by.ivanov.unit.service.dto.CommentPunchDTO;
import by.ivanov.unit.service.mapper.CommentPunchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CommentPunch}.
 */
@Service
@Transactional
public class CommentPunchServiceImpl implements CommentPunchService {

    private final Logger log = LoggerFactory.getLogger(CommentPunchServiceImpl.class);

    private final CommentPunchRepository commentPunchRepository;

    private final CommentPunchMapper commentPunchMapper;

    public CommentPunchServiceImpl(CommentPunchRepository commentPunchRepository, CommentPunchMapper commentPunchMapper) {
        this.commentPunchRepository = commentPunchRepository;
        this.commentPunchMapper = commentPunchMapper;
    }

    @Override
    public CommentPunchDTO save(CommentPunchDTO commentPunchDTO) {
        log.debug("Request to save CommentPunch : {}", commentPunchDTO);
        CommentPunch commentPunch = commentPunchMapper.toEntity(commentPunchDTO);
        commentPunch = commentPunchRepository.save(commentPunch);
        return commentPunchMapper.toDto(commentPunch);
    }

    @Override
    public Optional<CommentPunchDTO> partialUpdate(CommentPunchDTO commentPunchDTO) {
        log.debug("Request to partially update CommentPunch : {}", commentPunchDTO);

        return commentPunchRepository
            .findById(commentPunchDTO.getId())
            .map(existingCommentPunch -> {
                commentPunchMapper.partialUpdate(existingCommentPunch, commentPunchDTO);

                return existingCommentPunch;
            })
            .map(commentPunchRepository::save)
            .map(commentPunchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CommentPunchDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CommentPunches");
        return commentPunchRepository.findAll(pageable).map(commentPunchMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CommentPunchDTO> findOne(Long id) {
        log.debug("Request to get CommentPunch : {}", id);
        return commentPunchRepository.findById(id).map(commentPunchMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CommentPunch : {}", id);
        commentPunchRepository.deleteById(id);
    }
}
