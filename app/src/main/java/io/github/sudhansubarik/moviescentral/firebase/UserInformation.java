package io.github.sudhansubarik.moviescentral.firebase;

class UserInformation {

    public String name, email, mobile;

    UserInformation() {
    }

    UserInformation(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }
}
