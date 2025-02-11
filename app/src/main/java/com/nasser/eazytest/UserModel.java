package com.nasser.eazytest;

public class UserModel {
    private Name name;
    private Picture picture;
    private String email;
    private String phone;
    private Location location;

    public Name getName() {
        return name;
    }

    public Picture getPicture() {
        return picture;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public Location getLocation() {
        return location;
    }

    public class Name {
        private String first;
        private String last;

        public String getFullName() {
            return first + " " + last;
        }
    }

    public class Picture {
        private String large;

        public String getLarge() {
            return large;
        }
    }

    public class Location {
        private Street street;
        private String city;
        private String country;

        public String getFullAddress() {
            return street.number + " " + street.name + ", " + city + ", " + country;
        }

        public class Street {
            private int number;
            private String name;
        }
    }
}