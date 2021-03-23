package com.example.demo;

import java.lang.reflect.Field;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

// Entity is a JPA annotation to make this object ready for storage in a JPA-based data store

@Entity
public class Account {
    public @Id @GeneratedValue Long id = -1L;

    public @Embedded Name name = new Name();
    public @Embedded Address address = new Address();
    public @ElementCollection @OrderColumn Phone[] phone;
    public String email;

    // Account constrcutor is created when we need a new instance, but don't yet have its attributes
    public Account() {}

    // Constructor that instantiates our attributes
    public Account(String first, String middle, String last, String street, String city, String state, String zip, String email, Phone ... phone) {
        this.name.first = first;
        this.name.middle = middle;
        this.name.last = last;
        this.address.street = street;
        this.address.city = city;
        this.address.state = state;
        this.address.zip = zip;
        this.email = email;
        this.phone = phone;
    }

    public void update(Account o) {
        for(Field f : Account.class.getFields()) {
            try{
                Object val = f.get(o);
                if(val != null) f.set(this, val);
            }catch(IllegalAccessException ignored){
                //this ll never happen, everything is public
            }
        }
    }

    public PhoneResponse phone() {
        for(Phone p : phone) {
            if(p.type.equals("home")) {
                return new PhoneResponse(name, p.number);
            }
        }
        return null;
    }

    @Embeddable
    public static class Name {
        public String first, middle, last;
    }

    @Embeddable
    public static class Address {
        public String street, city, state, zip;
    }

    @Embeddable
    public static class Phone {
        public String number, type;

        public Phone() {}

        Phone(String number, String type) {
            this.number = number;
            this.type = type;
        }
    }
    
    public static class PhoneResponse {
        public Name name;
        public String phone;

        public PhoneResponse() {}

        PhoneResponse(Name name, String phone) {
            this.name = name;
            this.phone = phone;
        }
    }
}
