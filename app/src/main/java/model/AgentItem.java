package model;

public class AgentItem {

    private int imageURL;
    private String uniqueID;
    private String name;
    private String agency;
    private String policies;
    private String score;
    private String location;
    private String mobile;
    private double Lat_coordinate;
    private double Lng_coordinate;

    public AgentItem(String uniqueID, String name, String agency, String score, String location,
                     String mobile, String policies, double Lat_coordinate, double Lng_coordinate) {

        this.uniqueID = uniqueID;
        this.name = name;
        this.agency = agency;
        this.score = score;
        this.location = location;
        this.mobile = mobile;
        this.policies = policies;
        this.Lat_coordinate = Lat_coordinate;
        this.Lng_coordinate = Lng_coordinate;
        this.imageURL = imageURL;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getName() {
        return name;
    }

    public String getAgency() {
        return agency;
    }

    public String getScore() {
        return score;
    }

    public String getLocation() {
        return location;
    }

    public String getMobile() {
        return mobile;
    }

    public double getLat_coordinate() {
        return Lat_coordinate;
    }

    public double getLng_coordinate() {
        return Lng_coordinate;
    }

    public int getImageURL() {
        return imageURL;
    }

    public String getPolicies() {
        return policies;
    }

}
