package com.kartik.goallist.Model;

public class AddGoalModel {
    private long id;
    private String subGoalTitle;
    long mainGoalId;

    public AddGoalModel(String subGoalTitle, long mainGoalId) {
        this.subGoalTitle = subGoalTitle;
        this.mainGoalId = mainGoalId;
    }

    public AddGoalModel(long id, String subGoalTitle, long mainGoalId) {
        this.id = id;
        this.subGoalTitle = subGoalTitle;
        this.mainGoalId = mainGoalId;
    }

    public AddGoalModel(String subGoalTitle) {
        this.subGoalTitle = subGoalTitle;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSubGoalTitle() {
        return subGoalTitle;
    }

    public void setSubGoalTitle(String subGoalTitle) {
        this.subGoalTitle = subGoalTitle;
    }

    public long getMainGoalId(long s) {
        mainGoalId = s;
        return mainGoalId;
    }

    public void setMainGoalId(long mainGoalId) {
        this.mainGoalId = mainGoalId;
    }
}
