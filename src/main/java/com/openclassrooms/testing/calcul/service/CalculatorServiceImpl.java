package com.openclassrooms.testing.calcul.service;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;

public class CalculatorServiceImpl implements CalculatorService {
    
    private final Calculator calculator;
    
    // pour renvoyer un format de nb de milleir formatté, creer un attr. Pourtant, ce n'est pas ici que l'on doit faire le
    // formattage de la donnée, puisque nous sommes dans le model (pas de getter et setter à faire ici). Qui a la
    // responsabilité de formatter la sollution du calcul? C'est calculatorServiceImpl. Il fat construire un objet de type
    // SolutionFormatter
    private SolutionFormatted formattedSolution;
    
    /**
     * Constructeur avec le calculateur , et le calculateur formaté
     *
     * @param calculator
     * @param pSolutionFormatted
     */
    public CalculatorServiceImpl(Calculator calculator, SolutionFormatted pSolutionFormatted) {
        this.calculator = calculator;
        this.formattedSolution = pSolutionFormatted;
    }
    
    @Override
    public CalculationModel calculate(CalculationModel calculationModel) {
        final CalculationType type = calculationModel.getType();
        
        Integer response = null;
        switch (type) {
            case ADDITION:
                response = calculator.add(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
                break;
            case SUBTRACTION:
                response = calculator.sub(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
                break;
            case MULTIPLICATION:
                response = calculator.multiply(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
                break;
            case DIVISION:
                // ajout de la transformation de l'exception ArithemthicException en IllegalArgumentException
                try {
                    response = calculator.divide(calculationModel.getLeftArgument(), calculationModel.getRightArgument());
                } catch (ArithmeticException pE) {
                    throw new IllegalArgumentException(pE);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unsupported calculations");
        }
        
        calculationModel.setSolution(response);
        // renvoi la reponse formattée pour les nbs grace à une methode créé dans l'interface SolutionFormatted nouvellement crééé
        calculationModel.setFormattedSolution(formattedSolution.format(response));
        return calculationModel;
    }
    
}
