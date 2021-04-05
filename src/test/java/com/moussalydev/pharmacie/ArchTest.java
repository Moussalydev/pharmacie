package com.moussalydev.pharmacie;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.moussalydev.pharmacie");

        noClasses()
            .that()
            .resideInAnyPackage("com.moussalydev.pharmacie.service..")
            .or()
            .resideInAnyPackage("com.moussalydev.pharmacie.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.moussalydev.pharmacie.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
