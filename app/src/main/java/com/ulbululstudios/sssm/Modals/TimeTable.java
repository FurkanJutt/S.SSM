package com.ulbululstudios.sssm.Modals;

public class TimeTable {

    private String timeFrom, timeTo, subject, room, teacher;

    public TimeTable() {
        timeFrom = "00:00";
        timeTo = "00:00";
        subject = "subject";
        room = "room";
        teacher = "teacher";
    }

    public TimeTable(String timeFrom, String timeTo, String subject, String room, String teacher) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.subject = subject;
        this.room = room;
        this.teacher = teacher;
    }

    public String getTimeFrom() {
        return timeFrom;
    }
    public String getTimeTo() {
        return timeTo;
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
