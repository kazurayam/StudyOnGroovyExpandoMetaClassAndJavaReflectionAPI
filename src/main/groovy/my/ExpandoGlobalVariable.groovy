package my

import static java.lang.reflect.Modifier.isPublic
import static java.lang.reflect.Modifier.isStatic
import static java.lang.reflect.Modifier.isTransient

import java.lang.reflect.Field
import java.util.stream.Collectors

import internal.GlobalVariable

class ExpandoGlobalVariable {

    private final static Map<String, Object> additionalProperties = Collections.synchronizedMap([:])

    private ExpandoGlobalVariable() {}

    static SortedSet<String> keySetOfStaticGlobalVariables() {
        // getDeclaredFields() returns both of static fields and additional fields
        Set<Field> fields = GlobalVariable.class.getDeclaredFields() as Set<Field>
        Set<String> result = fields.stream()
                .filter { Field f ->
                    isPublic(f.modifiers) && isStatic(f.modifiers) && ! isTransient(f.modifiers)
                }
                .map { Field f -> f.getName() }
                .collect(Collectors.toSet())
        SortedSet<String> sorted = new TreeSet()
        sorted.addAll(result)
        return sorted
    }

    static SortedSet<String> keySetOfAdditionalGlobalVariables() {
        SortedSet<String> sorted = new TreeSet<String>()
        sorted.addAll(additionalProperties.keySet())
        return sorted
    }

    static SortedSet<String> keySetOfGlobalVariables() {
        SortedSet<String> sorted = new TreeSet()
        sorted.addAll(keySetOfStaticGlobalVariables())
        sorted.addAll(keySetOfAdditionalGlobalVariables())
        return sorted
    }

    static int addGlobalVariable(String name, Object value) {
        //validateVariableName(name)

        // obtain the ExpandoMetaClass of the internal.GlobalVariable class
        MetaClass mc = GlobalVariable.metaClass

        // register the Getter method for the name
        String getterName = getGetterName(name)
        mc.'static'."${getterName}" = { -> return additionalProperties[name] }

        // register the Setter method for the name
        String setterName = getSetterName(name)
        mc.'static'."${setterName}" = { newValue ->
            additionalProperties[name] = newValue
        }

        // store the value into the storage
        additionalProperties.put(name, value)

        return 1
    }

    static String getGetterName(String name) {
        return 'get' + getAccessorName(name)
    }

    static String getSetterName(String name) {
        return 'set' + getAccessorName(name)
    }

    static String getAccessorName(String name) {
        return ((CharSequence)name).capitalize()
    }

    static void clear() {
        additionalProperties.clear()
    }
}
