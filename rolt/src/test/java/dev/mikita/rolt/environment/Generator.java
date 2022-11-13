package dev.mikita.rolt.environment;

import dev.mikita.rolt.entity.*;

import java.util.Random;

public class Generator {
    private static final Random RAND = new Random();

    public static int randomInt() {
        return RAND.nextInt();
    }

    public static int randomInt(int max) {
        return RAND.nextInt(max);
    }

    public static int randomInt(int min, int max) {
        assert min >= 0;
        assert min < max;

        int result;
        do {
            result = randomInt(max);
        } while (result < min);
        return result;
    }

    public static boolean randomBoolean() {
        return RAND.nextBoolean();
    }

    public static City generateCity() {
        final City city = new City();
        city.setName("Random City Name " + randomInt());

        return city;
    }

    public static Property generateProperty() {
        final Property property = new Property();

        property.setAvailable(true);
        property.setDescription("randomDescription" + randomInt());
        property.setType(PropertyType.APARTMENT);
        property.setSquare((double) randomInt(15, 100));
        property.setStreet("randomStreet" + randomInt());
        property.setPostalCode(String.valueOf(randomInt()));

        return property;
    }

    public static ConsumerDetails generateConsumerDetails() {
        final ConsumerDetails details = new ConsumerDetails();
        details.setFirstName("FirstName" + randomInt());
        details.setLastName("LastName" + randomInt());
        details.setPhone(String.valueOf(randomInt()));
        details.setGender(ConsumerGender.MALE);

        return details;
    }

    public static Landlord generateLandlord() {
        final Landlord landlord = new Landlord();
        final ConsumerDetails details = generateConsumerDetails();

        landlord.setEmail("randomemail" + randomInt() + "@gmail.com");
        landlord.setPassword("randompassword" + randomInt());
        landlord.setDetails(details);

        return landlord;
    }

    public static Tenant generateTenant() {
        final Tenant tenant = new Tenant();
        final ConsumerDetails details = generateConsumerDetails();

        tenant.setEmail("randomemail" + randomInt() + "@gmail.com");
        tenant.setPassword("randompassword" + randomInt());
        tenant.setDetails(details);

        return tenant;
    }
}