package edu.ranken.jbuesking.gettogether.data.entity;

import java.util.List;

public class Group {
    private String organizer;
    private String name;
    private String description;
    private List<String> interestTags;
    private List<String> members;

    public Group() {
        // Empty constructor for Firebase
    }

    public Group(String organizer, String name, String description, List<String> interestTags, List<String> members) {
        this.organizer = organizer;
        this.name = name;
        this.description = description;
        this.interestTags = interestTags;
        this.members = members;
    }

    public String getOrganizer() { return organizer; }
    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getInterestTags() { return interestTags; }
    public void setInterestTags(List<String> interestTags) {
        this.interestTags = interestTags;
    }

    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) {
        this.members = members;
    }
}
