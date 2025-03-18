package com.example.userapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Embedded;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("maiden_name")
    private String maidenName;

    private Integer age;
    private String gender;
    private String email;
    private String phone;
    private String username;
    private String password;

    @Column("birth_date")
    private String birthDate;

    private String image;

    @Column("blood_group")
    private String bloodGroup;

    private Double height;
    private Double weight;

    @Column("eye_color")
    private String eyeColor;

    @Embedded.Empty
    private Hair hair;

    @Embedded.Empty
    private Address address;

    @Embedded.Empty
    private Bank bank;

    @Embedded.Empty
    private Company company;

    private String ein;
    private String ssn;

    @Column("user_agent")
    private String userAgent;

    private String role;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Hair {
        private String color;
        private String type;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Address {
        private String address;
        private String city;
        private String state;

        @Column("state_code")
        private String stateCode;

        @Column("postal_code")
        private String postalCode;

        @Embedded.Empty
        private Coordinates coordinates;

        private String country;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Coordinates {
        private Double lat;
        private Double lng;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Bank {
        @Column("card_expire")
        private String cardExpire;

        @Column("card_number")
        private String cardNumber;

        @Column("card_type")
        private String cardType;

        private String currency;
        private String iban;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Company {
        private String department;
        private String name;
        private String title;
    }
}
