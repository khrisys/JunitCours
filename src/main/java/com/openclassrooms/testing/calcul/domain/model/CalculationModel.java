package com.openclassrooms.testing.calcul.domain.model;

import org.mockito.invocation.InvocationOnMock;

/**
 * A model to represent a two argument integer calculation which needs to be
 * performed.
 */
public class CalculationModel {
    
    private static final String SEPARATOR = " ";
    private Integer leftArgument;
    private Integer rightArgument;
    private CalculationType type;
    private Integer solution;
    
    // creation de l'attr correspondant à la solution formatée
    private String formattedSolution;
    
    
    public CalculationModel(CalculationType calculationType, int leftArgument, int rightArgument) {
        type = calculationType;
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
    }
    
    /**
     * Convenience Constructor used in test
     */
    public CalculationModel(CalculationType calculationType, int leftArgument, int rightArgument, Integer solution) {
        type = calculationType;
        this.leftArgument = leftArgument;
        this.rightArgument = rightArgument;
        this.solution = solution;
    }
    
    /**
     * Builds a Calculation from a string such as 2 + 2
     *
     * @param calculation in written form
     * @return model representing the calculatoin
     */
    public static CalculationModel fromText(String calculation) {
        final String[] parts = calculation.split(SEPARATOR);
        final int leftArgument = Integer.parseInt(parts[0]);
        final int rightArgument = Integer.parseInt(parts[2]);
        final CalculationType calculationType = CalculationType.fromSymbol(parts[1]);
        
        return new CalculationModel(calculationType, leftArgument, rightArgument);
    }
    
    /**
     * Classe construisant un mock qui renvoie un résultat en fonction du type d'opération. Si l'opération est une addition, on
     * renvoie le modèle de calcul avec la réponse 4, et on donne un résultat différent pour chaque type d'opération
     *
     * La classe   InvocationOnMock  permet de connaître les arguments de l'appel au mock. Ici, on souhaite connaître le type
     * d'opération pour donner une réponse différente en fonction. De plus, on réutilise la classe en entrée  CalculationModel
     * pour la renvoyer complétée de la réponse.
     *
     * @param invocation
     * @return
     */
    public CalculationModel answer(InvocationOnMock invocation) {
        final CalculationModel model = invocation.getArgument(0, CalculationModel.class);
        switch (model.getType()) {
            case ADDITION:
                model.setSolution(4);
                break;
            case SUBTRACTION:
                model.setSolution(1);
                break;
            case MULTIPLICATION:
                model.setSolution(48);
                break;
            case DIVISION:
                model.setSolution(3);
                break;
            default:
        }
        return model;
    }
    
    public CalculationType getType() {
        return type;
    }
    
    public void setType(CalculationType type) {
        this.type = type;
    }
    
    public Integer getLeftArgument() {
        return leftArgument;
    }
    
    public void setLeftArgument(Integer leftArgument) {
        this.leftArgument = leftArgument;
    }
    
    public Integer getRightArgument() {
        return rightArgument;
    }
    
    public void setRightArgument(Integer rightArgument) {
        this.rightArgument = rightArgument;
    }
    
    public Integer getSolution() {
        return solution;
    }
    
    public void setSolution(Integer solution) {
        this.solution = solution;
    }
    
    public String getFormattedSolution() {
        return formattedSolution;
    }
    
    public void setFormattedSolution(String pFormattedSolution) {
        formattedSolution = pFormattedSolution;
    }
}
