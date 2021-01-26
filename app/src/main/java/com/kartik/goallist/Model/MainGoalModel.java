package com.kartik.goallist.Model;

public class MainGoalModel {
    long id, position;
    String maingoalTitle, endDate;

    public MainGoalModel(String maingoalTitle, String endDate, long position) {
        this.maingoalTitle = maingoalTitle;
        this.endDate = endDate;
        this.position = position;
    }

    public MainGoalModel(long id, String maingoalTitle, String endDate, long position) {
        this.id = id;
        this.maingoalTitle = maingoalTitle;
        this.endDate = endDate;
        this.position = position;
    }

    public long getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaingoalTitle() {
        return maingoalTitle;
    }

    public void setMaingoalTitle(String maingoalTitle) {
        this.maingoalTitle = maingoalTitle;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }
}
