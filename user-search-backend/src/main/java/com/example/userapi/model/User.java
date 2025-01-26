package com.example.userapi.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String maidenName;
    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String username;
    private String password;
    private String birthDate;
    private String image;
    private String bloodGroup;
    private Double height;
    private Double weight;
    private String eyeColor;

    // Explicit declaration of setters and getters is not required , as lombok generates them during the runtime
    @Embedded
    private Hair hair;

    @Embedded
    private Address address;

    @Embedded
    private Bank bank;

    @Embedded
    private Company company;

    private String ein;
    private String ssn;
    private String userAgent;
    private String role;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Hair {
        private String color;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Address {
        private String address;
        private String city;
        private String state;
        private String stateCode;
        private String postalCode;

        @Embedded
        private Coordinates coordinates;

        private String country;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Coordinates {
        private Double lat;
        private Double lng;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Bank {
        private String cardExpire;
        private String cardNumber;
        private String cardType;
        private String currency;
        private String iban;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Embeddable
    public static class Company {
        private String department;
        private String name;
        private String title;
    }
}
