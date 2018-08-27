package com.higgsup.kpi.glossary;

public enum GroupType {
    SEMINAR(1, "Seminar"),
    CLUB(2, "Câu lạc bộ"),
    TEAM_BUILDING(3, "Team building"),
    SUPPORT(4, "Support chung");

    private Integer id;

    private String name;

    GroupType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public static final GroupType getGroupType(Integer id) {
        for (GroupType groupType : GroupType.values()) {
            if (groupType.getId().equals(id))
                return groupType;
        }
        return GroupType.SEMINAR;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
