package com.appdevg6.smartcite.service.impl;

import com.appdevg6.smartcite.entity.GrammarCheck;
import com.appdevg6.smartcite.repository.GrammarCheckRepository;
import com.appdevg6.smartcite.service.GrammarCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.TreeMap;
import java.util.Map;

@Service
public class GrammarCheckServiceImpl implements GrammarCheckService {

    @Autowired
    private GrammarCheckRepository grammarCheckRepository;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public GrammarCheck createGrammarCheck(GrammarCheck grammarCheck) {
        // Generate corrected text using GrammarBot API
        String corrected = applyGrammarCorrections(grammarCheck.getInputText());
        grammarCheck.setCorrectedText(corrected);
        return grammarCheckRepository.save(grammarCheck);
    }
    
    private String applyGrammarCorrections(String text) {
        if (text == null || text.isEmpty()) return text;
        
        try {
            // Call GrammarBot API
            String url = "https://api.grammarbot.io/v2/check?text=" + 
                java.net.URLEncoder.encode(text, "UTF-8") + "&language=en-US";
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            // Parse response and apply corrections
            JsonNode root = objectMapper.readTree(response.getBody());
            JsonNode matches = root.path("matches");
            
            if (!matches.isArray()) {
                System.out.println("No matches found in GrammarBot response");
                return text;
            }
            
            // Sort matches by offset in reverse order to maintain correct positions
            TreeMap<Integer, JsonNode> sortedMatches = new TreeMap<>();
            for (JsonNode match : matches) {
                int offset = match.path("offset").asInt();
                sortedMatches.put(offset, match);
            }
            
            String correctedText = text;
            
            // Apply corrections from end to start to avoid offset issues
            for (Map.Entry<Integer, JsonNode> entry : sortedMatches.descendingMap().entrySet()) {
                JsonNode match = entry.getValue();
                int offset = match.path("offset").asInt();
                int length = match.path("length").asInt();
                JsonNode replacements = match.path("replacements");
                
                if (replacements.isArray() && replacements.size() > 0) {
                    String replacement = replacements.get(0).path("value").asText();
                    
                    if (offset >= 0 && offset + length <= correctedText.length()) {
                        String before = correctedText.substring(0, offset);
                        String after = correctedText.substring(offset + length);
                        correctedText = before + replacement + after;
                        System.out.println("Applied correction: '" + correctedText.substring(offset, offset + replacement.length()) + "' at offset " + offset);
                    }
                }
            }
            
            return correctedText;
            
        } catch (Exception e) {
            // If API fails, return original text with fallback corrections
            System.err.println("GrammarBot API error: " + e.getMessage());
            e.printStackTrace();
            return applyBasicCorrections(text);
        }
    }
    
    private String applyBasicCorrections(String text) {
        String corrected = text;
        // Fallback: basic corrections
        corrected = corrected.replaceAll("\\bdont\\b", "don't");
        corrected = corrected.replaceAll("\\bcant\\b", "can't");
        corrected = corrected.replaceAll("\\bisnt\\b", "isn't");
        corrected = corrected.replaceAll("\\barent\\b", "aren't");
        
        // Capitalize first letter
        if (!corrected.isEmpty()) {
            corrected = corrected.substring(0, 1).toUpperCase() + corrected.substring(1);
        }
        return corrected;
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
