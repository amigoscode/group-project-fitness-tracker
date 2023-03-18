package com.project.trackfit.trainer;

public record CreateTrainerRequest(
        String email,

        String password,


        String firstName,

        String lastName,

        String phoneNumber

) {
}
