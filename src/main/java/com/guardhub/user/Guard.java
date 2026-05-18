package com.guardhub.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.guardhub.shift.registration.ShiftRegistration;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("GUARD")
public class Guard extends User {

    @OneToMany(mappedBy = "guard", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ShiftRegistration> shiftRegistrations = new ArrayList<>();

    public Guard() {
        super();
        setUserType("GUARD");
    }

    public Guard(String name, String password, String email, String phoneNumber) {
        super(name, password, email, phoneNumber);
        setUserType("GUARD");
    }

    public Guard(Long id, String name, String password, String email, String phoneNumber) {
        super(id, name, password, email, phoneNumber);
        setUserType("GUARD");
    }

    public List<ShiftRegistration> getShiftRegistrations() {
        return shiftRegistrations;
    }

    public void setShiftRegistrations(List<ShiftRegistration> shiftRegistrations) {
        this.shiftRegistrations = shiftRegistrations;
    }

    public void addShiftRegistration(ShiftRegistration shiftRegistration) {
        if (shiftRegistrations == null) {
            shiftRegistrations = new ArrayList<>();
        }
        shiftRegistrations.add(shiftRegistration);
        shiftRegistration.setGuard(this);
    }

    public void removeShiftRegistration(ShiftRegistration shiftRegistration) {
        if (shiftRegistrations != null) {
            shiftRegistrations.remove(shiftRegistration);
            shiftRegistration.setGuard(null);
        }
    }

    public boolean hasRegistrations() {
        return shiftRegistrations != null && !shiftRegistrations.isEmpty();
    }

    public int getRegistrationCount() {
        return shiftRegistrations != null ? shiftRegistrations.size() : 0;
    }

    @Override
    public String toString() {
        return "Guard{" +
                "id=" + getUserId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", registrationCount=" + getRegistrationCount() +
                '}';
    }
}
