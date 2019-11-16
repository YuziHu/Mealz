package com.example.mealz.Models;

import java.util.List;

public class Group {
    private String groupID;
    private List<String> members;

    public Group(){}

    public Group(String groupID, List<String> members) {
        this.groupID = groupID;
        this.members = members;
    }

    public String getgroupID() {
        return groupID;
    }

    public void setgID(String groupID) {
        this.groupID = groupID;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
