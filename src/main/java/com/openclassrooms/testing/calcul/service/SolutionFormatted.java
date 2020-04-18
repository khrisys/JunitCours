package com.openclassrooms.testing.calcul.service;

public interface SolutionFormatted {
    
    /**
     * On doit fournir à l'interface une methode de formatage si on veut que l'objet soit formaté
     * @param pResponse
     * @return
     */
    String format(Integer pResponse);
}
