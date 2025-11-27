package com.appdevg6.smartcite.service.impl;

import com.appdevg6.smartcite.entity.GrammarCheck;
import com.appdevg6.smartcite.repository.GrammarCheckRepository;
import com.appdevg6.smartcite.service.GrammarCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GrammarCheckServiceImpl implements GrammarCheckService {

    @Autowired
    private GrammarCheckRepository grammarCheckRepository;

    @Override
    public GrammarCheck createGrammarCheck(GrammarCheck grammarCheck) {
        return grammarCheckRepository.save(grammarCheck);
    }

    @Override
    public List<GrammarCheck> getAllGrammarChecks() {
        return grammarCheckRepository.findAll();
    }

    @Override
    public GrammarCheck getGrammarCheckById(Long id) {
        return grammarCheckRepository.findById(id).orElse(null);
    }

    @Override
    public GrammarCheck updateGrammarCheck(Long id, GrammarCheck updated) {
        GrammarCheck existing = grammarCheckRepository.findById(id).orElse(null);
        if (existing != null) {
            existing.setInputText(updated.getInputText());
            existing.setHighlightedPhrases(updated.getHighlightedPhrases());
            return grammarCheckRepository.save(existing);
        }
        return null;
    }

    @Override
    public void deleteGrammarCheck(Long id) {
        grammarCheckRepository.deleteById(id);
    }
}
