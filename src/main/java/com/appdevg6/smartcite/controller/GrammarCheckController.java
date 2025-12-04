package com.appdevg6.smartcite.controller;

import com.appdevg6.smartcite.entity.GrammarCheck;
import com.appdevg6.smartcite.service.GrammarCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/grammarchecks")
public class GrammarCheckController {

    @Autowired
    private GrammarCheckService grammarCheckService;

    @PostMapping
    public GrammarCheck createGrammarCheck(@RequestBody GrammarCheck grammarCheck) {
        return grammarCheckService.createGrammarCheck(grammarCheck);
    }

    @GetMapping
    public List<GrammarCheck> getAllGrammarChecks() {
        return grammarCheckService.getAllGrammarChecks();
    }

    @GetMapping("/{id}")
    public GrammarCheck getGrammarCheckById(@PathVariable Long id) {
        return grammarCheckService.getGrammarCheckById(id);
    }

    @PutMapping("/{id}")
    public GrammarCheck updateGrammarCheck(@PathVariable Long id, @RequestBody GrammarCheck grammarCheck) {
        return grammarCheckService.updateGrammarCheck(id, grammarCheck);
    }

    @DeleteMapping("/{id}")
    public void deleteGrammarCheck(@PathVariable Long id) {
        grammarCheckService.deleteGrammarCheck(id);
    }
}
