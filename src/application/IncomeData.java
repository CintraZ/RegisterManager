package application;

import javafx.beans.property.SimpleStringProperty;

public class IncomeData {
    private final SimpleStringProperty soName = new SimpleStringProperty();
    private final SimpleStringProperty dtID = new SimpleStringProperty();
    private final SimpleStringProperty dtName = new SimpleStringProperty();
    private final SimpleStringProperty numType = new SimpleStringProperty();
    private final SimpleStringProperty regCount = new SimpleStringProperty();
    private final SimpleStringProperty Income = new SimpleStringProperty();

    public String getSoID() {
        return soName.get();
    }
    public void setSoName(String soName) {
        this.soName.set(soName);
    }

    public String getDtID() {
        return dtID.get();
    }
    public void setDtID(String dtID) {
        this.dtID.set(dtID);
    }

    public String getDtName() {
        return dtName.get();
    }
    public void setDtName(String dtName) {
        this.dtName.set(dtName);
    }

    public String getNumType() {
        return numType.get();
    }
    public void setNumType(String numType) {
        this.numType.set(numType);
    }

    public String getRegCount() {
        return regCount.get();
    }
    public void setRegCount(String regCount) {
        this.regCount.set(regCount);
    }

    public String getIncome() {
        return Income.get();
    }
    public void setIncome(String Income) {
        this.Income.set(Income);
    }

    public SimpleStringProperty soNameProperty() {return soName; }
    public SimpleStringProperty dtIDProperty() {return dtID; }
    public SimpleStringProperty dtNameProperty() {return dtName; }
    public SimpleStringProperty numTyperoperty() {return numType; }
    public SimpleStringProperty regCountProperty() {return regCount; }
    public SimpleStringProperty IncomeProperty() {return Income; }
}
