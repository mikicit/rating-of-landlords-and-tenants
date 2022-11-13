package dev.mikita.rolt.entity;

import dev.mikita.rolt.environment.Generator;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class LandlordTest {
    @Test
    public void addPropertyWorksWhenAddingPropertyForFirstTime() {
        final Landlord owner = Generator.generateLandlord();
        final Property property = Generator.generateProperty();

        property.setId(Generator.randomInt());
        owner.addProperty(property);

        assertEquals(1, owner.getProperties().size());
    }

    @Test
    public void addPropertyWorksForLandlordWithExistingProperties() {
        final Landlord owner = Generator.generateLandlord();
        final Property propertyOne = Generator.generateProperty();

        propertyOne.setId(Generator.randomInt());
        owner.setProperties(new ArrayList<>(Collections.singletonList(propertyOne)));

        final Property propertyTwo = Generator.generateProperty();
        propertyTwo.setId(Generator.randomInt());

        owner.addProperty(propertyTwo);
        assertEquals(2, owner.getProperties().size());
    }

    @Test
    public void afterAddingPropertySameOwner() {
        final Landlord owner = Generator.generateLandlord();
        final Property property = Generator.generateProperty();

        property.setId(Generator.randomInt());
        owner.addProperty(property);

        assertNotNull(property.getOwner());
        assertEquals(owner, property.getOwner());
    }

    @Test
    public void afterAddingExistingPropertyQuantityDoesNotChange() {
        final Landlord owner = Generator.generateLandlord();
        final Property property = Generator.generateProperty();

        property.setId(Generator.randomInt());
        owner.addProperty(property);
        owner.addProperty(property);

        assertEquals(1, owner.getProperties().size());
    }

    @Test
    public void afterDeletingPropertyDoesNotExist() {
        final Landlord owner = Generator.generateLandlord();
        final Property property = Generator.generateProperty();

        property.setId(Generator.randomInt());
        owner.addProperty(property);
        owner.removeProperty(property);

        assertEquals(0, owner.getProperties().size());
    }
}
