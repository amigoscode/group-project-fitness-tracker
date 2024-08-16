package com.project.trackfit.customer;

import com.project.trackfit.user.Role;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


public class CustomerUpdateRequest {

        @Min(value = 18, message = "Age must be at least 18")
        @Max(value = 100, message = "Age must be no more than 100")
        private Integer age;

        private String address;

        private Role role;

        public CustomerUpdateRequest() {
        }

        public CustomerUpdateRequest(Integer age, String address, Role role) {
                this.age = age;
                this.address = address;
                this.role = role;
        }

        public Integer getAge() {
                return age;
        }

        public void setAge(Integer age) {
                this.age = age;
        }

        public String getAddress() {
                return address;
        }

        public void setAddress(String address) {
                this.address = address;
        }

        public Role getRole() {
                return role;
        }

        public void setRole(Role role) {
                this.role = role;
        }
}
