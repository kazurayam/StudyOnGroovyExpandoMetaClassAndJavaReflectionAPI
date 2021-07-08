package my

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import my.ExpandoGlobalVariable as EGV
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

class ExpandoGlobalVariableTest {

    @Test
    void test_initial_state() {
        assert EGV.keySetOfStaticGlobalVariables().size() >= 1
        assert EGV.keySetOfStaticGlobalVariables().contains("CONFIG")
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 0
    }

    @Test
    void test_clear_should_not_make_it_empty() {
        EGV.clear()
        assert EGV.keySetOfStaticGlobalVariables().size() >= 1
        assert EGV.keySetOfStaticGlobalVariables().contains("CONFIG")
    }

    @Test
    void test_addGlobalVariable() {
        monitor("test_addGlobalVariable before 1st clear")
        EGV.clear()
        monitor("test_addGlobalVariable after 1st clear")
        assert EGV.keySetOfStaticGlobalVariables().size() == 1, "before adding"
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 0, "before adding"
        EGV.addGlobalVariable("additive1", "foo")
        EGV.addGlobalVariable("additive2", "bar")
        monitor("test_addGlobalVariable after adding")
        assert EGV.keySetOfStaticGlobalVariables().size() == 1, "after adding"
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 2, "after adding"
        EGV.clear()
        monitor("test_addGlobalVariable after 2nd clear")
        assert EGV.keySetOfStaticGlobalVariables().size() == 1, "after clear"
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 0, "after clear"
    }

    @Test
    void test_keySetOfAdditionalGlobalVariables() {
        EGV.clear()
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 0
        EGV.addGlobalVariable("additive1", "foo")
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 1
        EGV.clear()
        assert EGV.keySetOfAdditionalGlobalVariables().size() == 0
    }

    void monitor(String title) {
        println "-------------------${title}-------------------"
        println "static: " + EGV.keySetOfStaticGlobalVariables()
        println "additional: " + EGV.keySetOfAdditionalGlobalVariables()
        println "static+additional: " + EGV.keySetOfGlobalVariables()

    }
}
