package com.jobscape.entitymanager.model;


public enum ApplicationStatus {

    WISHLIST("À postuler"),
    APPLIED("CV envoyé"),
    FOLLOWED_UP("Relancé"),
    FIRST_CONTACT("Premier contact"),
    HR_INTERVIEW("Entretien RH"),
    MANAGER_INTERVIEW("Entretien Manager"),
    TECHNICAL_TEST("Test technique"),
    OFFER_RECEIVED("Offre reçue"),
    REJECTED("Rejeté");

    private final String label;

    ApplicationStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
