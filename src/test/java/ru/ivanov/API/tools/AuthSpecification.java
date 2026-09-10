package ru.ivanov.API.tools;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class AuthSpecification {
    public static RequestSpecification getAuthSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setBasePath("/auth")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification postBookingSpec() {
        return new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setBasePath("/booking")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static RequestSpecification deleteBookingSpec(int bookingId, String authToken) {
        return new RequestSpecBuilder()
                .setBaseUri("https://restful-booker.herokuapp.com")
                .setBasePath("/booking/" + bookingId)
                .addHeader("Cookie", "token=" + authToken)
                .setContentType(ContentType.JSON)
                .build();
    }
}
