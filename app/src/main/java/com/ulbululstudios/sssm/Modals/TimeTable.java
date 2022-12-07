package com.ulbululstudios.sssm.Modals;

public class TimeTable {

    private String time, subject, room, teacher;

    public TimeTable() {
        time = "00:00 - 00:00";
        subject = "subject";
        room = "room";
        teacher = "teacher";
    }

    public TimeTable(String time, String subject, String room, String teacher) {
        this.time = time;
        this.subject = subject;
        this.room = room;
        this.teacher = teacher;
    }

    public String getTime() {
        return time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
}
