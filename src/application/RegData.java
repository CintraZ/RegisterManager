package application;

import javafx.beans.property.SimpleStringProperty;

public final class RegData {
    private final SimpleStringProperty regID = new SimpleStringProperty();
    private final SimpleStringProperty ptName = new SimpleStringProperty();
    private final SimpleStringProperty regDate = new SimpleStringProperty();
    private final SimpleStringProperty numType = new SimpleStringProperty();


    /*
     *Constructor
     */
    public RegData() {

    }

    public String getRegID() {
        return regID.get();
    }

    public void setRegID(String regID) {
        this.regID.set(regID);
    }

    public SimpleStringProperty regIDProperty() {
        return regID;
    }

    public String getPtName() {
        return ptName.get();
    }

    public void setPtName(String ptName) {
        this.ptName.set(ptName);
    }

    public SimpleStringProperty ptNameProperty() {
        return ptName;
    }

    public String getRegDate() {
        return regDate.get();
    }

    public void setRegDate(String regDate) {
        this.regDate.set(regDate);
    }

    public SimpleStringProperty regDateProperty() {
        return regDate;
    }

    public String getNumType() {
        return numType.get();
    }

    public void setNumType(String numType) {
        this.numType.set(numType);
    }

    public SimpleStringProperty numtypeProperty() {
        return numType;
    }

}
