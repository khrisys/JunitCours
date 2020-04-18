package com.openclassrooms.testing.calcul.service;

public class SolutionFormattedImpl implements SolutionFormatted {
    
    @Override
    public String format(Integer pResponse) {
        return String.format("%d", pResponse);
    }
}
