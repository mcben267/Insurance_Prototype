package model;

public class AgentItem {

    private int imageURL;
    private String uniqueID;
    private String name;
    private String agency;
    private String score;
    private String location;
    private String mobile;
    private String Lat_coordinate;
    private String Lng_coordinate;

    public AgentItem(String uniqueID, String name, String agency, String score, String location,
                     String mobile, String Lat_coordinate, String Lng_coordinate) {

        this.uniqueID = uniqueID;
        this.name = name;
        this.agency = agency;
        this.score = score;
        this.location = location;
        this.mobile = mobile;
        this.Lat_coordinate = Lat_coordinate;
        this.Lng_coordinate = Lng_coordinate;
        this.imageURL = imageURL;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLat_coordinate() {
        return Lat_coordinate;
    }

    public void setLat_coordinate(String lat_coordinate) {
        Lat_coordinate = lat_coordinate;
    }

    public String getLng_coordinate() {
        return Lng_coordinate;
    }

    public void setLng_coordinate(String lng_coordinate) {
        Lng_coordinate = lng_coordinate;
    }

    public int getImageURL() {
        return imageURL;
    }

    public void setImageURL(int imageURL) {
        this.imageURL = imageURL;
    }
}
