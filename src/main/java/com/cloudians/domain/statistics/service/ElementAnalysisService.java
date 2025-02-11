package com.cloudians.domain.statistics.service;

import com.cloudians.domain.personaldiary.entity.PersonalDiary;
import com.cloudians.domain.personaldiary.entity.analysis.FiveElement;
import com.cloudians.domain.personaldiary.entity.analysis.PersonalDiaryAnalysis;
import com.cloudians.domain.personaldiary.repository.FiveElementCharacterRepository;
import com.cloudians.domain.personaldiary.repository.FiveElementRepository;
import com.cloudians.domain.personaldiary.repository.PersonalDiaryAnalysisRepository;
import com.cloudians.domain.personaldiary.service.PersonalDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;

@Service
@RequiredArgsConstructor
@Transactional
public class ElementAnalysisService {

    private final PersonalDiaryService personalDiaryService;
    private final PersonalDiaryAnalysisRepository personalDiaryAnalysisRepository;
    private final FiveElementRepository fiveElementRepository;

    protected List<String> getCharactersList(FiveElement element) {
        return personalDiaryService.getElementCharacters(element);
    }

    protected FiveElement getElementCharacter(List<Map.Entry<String, Long>> elementList, String type) {
        Map.Entry<String, Long> elementEntry;

        switch (type) {
            case "MAX":
                elementEntry = elementList.stream()
                        .max(comparingByValue())
                        .orElseThrow(() -> new RuntimeException("List is empty"));
                break;
            case "MIN":
                elementEntry = elementList.stream()
                        .min(comparingByValue())
                        .orElseThrow(() -> new RuntimeException("List is empty"));
                break;
            default:
                throw new IllegalArgumentException("Invalid type: " + type);
        }

        String elementStr = elementEntry.getKey().toString();
        FiveElement element = fiveElementRepository.findByName(elementStr).get();
        return element;
    }

    protected String formatTop3Elements(Map<String, Long> frequency) {
        return frequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(","));
    }

    protected List<String> getElementList(List<PersonalDiary> diaryList) {
        List<String> elementList = new ArrayList<>();
        for (PersonalDiary diary : diaryList) {
            PersonalDiaryAnalysis analysis = personalDiaryAnalysisRepository.findByPersonalDiaryId(diary.getId()).get();
            elementList.add(analysis.getFiveElement().getName());
        }
        return elementList;
    }


    protected List<Map.Entry<String, Long>> getMostFrequentElement(List<String> elementList) {

        Map<String, Long> frequencyMap = elementList.stream()
                .collect(Collectors.groupingBy(e -> e, Collectors.counting()));

        Optional<Map.Entry<String, Long>> mostFrequent = frequencyMap.entrySet().stream()
                .max(comparingByValue());

        if (frequencyMap.size() < 3) {
            List<Map.Entry<String, Long>> list = frequencyMap.entrySet().stream()
                    .sorted(comparingByValue(Comparator.reverseOrder()))
                    .limit(frequencyMap.size())
                    .collect(Collectors.toList());
            return list;
        }

        List<Map.Entry<String, Long>> list = frequencyMap.entrySet().stream()
                .sorted(comparingByValue(Comparator.reverseOrder()))
                .limit(3)
                .collect(Collectors.toList());
        return list;
    }


}
