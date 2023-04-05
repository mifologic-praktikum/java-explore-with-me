package ru.practicum.ewm.compilation.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.Compilation;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.CompilationRepository;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.event.Event;
import ru.practicum.ewm.event.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository compilationRepository;
    EventRepository eventRepository;

    public CompilationServiceImpl(CompilationRepository compilationRepository, EventRepository eventRepository) {
        this.compilationRepository = compilationRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = new ArrayList<>();
        if (!newCompilationDto.getEvents().isEmpty()) {
            events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
        }
        Compilation compilation = CompilationMapper.toNewCompilation(newCompilationDto);
        compilation.setEvents(events);
        Compilation compilationForDto = compilationRepository.save(compilation);
        return CompilationMapper.toCompilationDto(compilationForDto);
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id = " + compId + " not found")
        );
        compilationRepository.deleteById(compId);

    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationDto updateCompilationDto) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id = " + compId + " not found")
        );
        if (!updateCompilationDto.getEvents().isEmpty() && updateCompilationDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllByIdIn(updateCompilationDto.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationDto.getTitle() != null && !updateCompilationDto.getTitle().isBlank()) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }

        return CompilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public List<CompilationDto> getCompilationsList(Boolean pinned, int from, int size) {
        List<CompilationDto> compilationDtos = new ArrayList<>();

        Pageable pageable = PageRequest.of((from / size), size);
        List<Compilation> compilations = compilationRepository.findAllByPinned(pinned, pageable);
        for (Compilation compilation : compilations) {
            compilationDtos.add(CompilationMapper.toCompilationDto(compilation));
        }
        return compilationDtos;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation with id = " + compId + " not found")
        );
        return CompilationMapper.toCompilationDto(compilation);
    }
}
