package com.openclassrooms.testing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * cette classe implémente une interface permettant de manipuler la classe de test, juste après sa création.
 */
public class LoggingExtension implements TestInstancePostProcessor {
    
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        Logger logger = LogManager.getLogger(testInstance.getClass());
        testInstance
                .getClass()
                .getMethod("setLogger", Logger.class)
                .invoke(testInstance, logger);
    }
}
