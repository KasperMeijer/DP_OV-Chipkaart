package nl.hu.dp.domain;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private java.sql.Date geboortedatum;

    public Reiziger() {}

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, java.sql.Date geboortedatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;
    }

    public int getId() {
        return id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public java.sql.Date getGeboortedatum() {
        return geboortedatum;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }

    public void setGeboortedatum(java.sql.Date geboortedatum) {
        this.geboortedatum = geboortedatum;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(voorletters);

        if(tussenvoegsel != null && !tussenvoegsel.isEmpty()){
            str.append(" " + tussenvoegsel);
        }

        str.append(" " + achternaam);

        str.append(" " + geboortedatum);

        return str.toString();
    }
}
