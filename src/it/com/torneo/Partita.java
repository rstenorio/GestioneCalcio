package it.com.torneo;

public class Partita {

    private int giornata;
    private String squadra1;
    private String squadra2;
    private int golSquadra1;
    private int golSquadra2;

    public Partita(int giornata, String squadra1, String squadra2, int golSquadra1, int golSquadra2) {
        this.giornata = giornata;
        this.squadra1 = squadra1;
        this.squadra2 = squadra2;
        this.golSquadra1 = golSquadra1;
        this.golSquadra2 = golSquadra2;
    }

    public int getGiornata() {
        return giornata;
    }

    public String getSquadra1() {
        return squadra1;
    }

    public String getSquadra2() {
        return squadra2;
    }

    public int getGolSquadra1() {
        return golSquadra1;
    }

    public int getGolSquadra2() {
        return golSquadra2;
    }

    String getCSV() {
        String ris = "";

        ris += giornata + ";" + squadra1 + ";" + golSquadra1 + ";" + golSquadra2 + ";" + squadra2 + "\n";

        return ris;

    }

}
