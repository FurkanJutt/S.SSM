package com.ulbululstudios.sssm.Modals;

public class Section {
    private String section;
    private String department;

    public Section() {
        section = "0A";
        department = "department";
    }

    public Section(String department, String section) {
        this.department = department;
        this.section = section;
    }

    public String getSection() {
        return section;
    }

    public String getDepartment() {
        return department;
    }
}
