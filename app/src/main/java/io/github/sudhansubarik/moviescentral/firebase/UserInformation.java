package io.github.sudhansubarik.moviescentral.firebase;

public class UserInformation {

    public String name, email, mobile;

    UserInformation() {
    }

    public UserInformation(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }
}
