package com.appdevg6.smartcite.service;

import com.appdevg6.smartcite.entity.GrammarCheck;
import java.util.List;

public interface GrammarCheckService {

    GrammarCheck createGrammarCheck(GrammarCheck grammarCheck);

    List<GrammarCheck> getAllGrammarChecks();

    GrammarCheck getGrammarCheckById(Long id);

    GrammarCheck updateGrammarCheck(Long id, GrammarCheck updated);

    void deleteGrammarCheck(Long id);
}
