package model;

public class InsuranceItem {
    String insuranceName;
    int insuranceIcon;

    public InsuranceItem(String insuranceName, int insuranceIcon) {
        this.insuranceName = insuranceName;
        this.insuranceIcon = insuranceIcon;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public int getInsuranceIcon() {
        return insuranceIcon;
    }

    public void setInsuranceIcon(int insuranceIcon) {
        this.insuranceIcon = insuranceIcon;
    }
}
