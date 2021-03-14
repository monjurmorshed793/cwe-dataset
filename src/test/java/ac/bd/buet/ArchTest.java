package ac.bd.buet;

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
            .importPackages("ac.bd.buet");

        noClasses()
            .that()
            .resideInAnyPackage("ac.bd.buet.service..")
            .or()
            .resideInAnyPackage("ac.bd.buet.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..ac.bd.buet.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
