package model;

import org.parceler.Parcel;

@Parcel
public class Ikastetxeak implements java.io.Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 7028199022785488745L;
    private int CCEN;
    private String NOM;
    private String NOME;
    private String DGENRC;
    private String DGENRE;
    private String GENR;
    private int MUNI;
    private String DMUNIC;
    private String DMUNIE;
    private String DTERRC;
    private String DTERRE;
    private String DEPE;
    private String DTITUC;
    private String DTITUE;
    private String DOMI;
    private int CPOS;
    private long TEL1;
    private String TFAX;
    private String EMAIL;
    private String PAGINA;
    private String COOR_X;
    private String COOR_Y;
    private double LATITUD;
    private double LONGITUD;
    public Ikastetxeak() {
    }

    public Ikastetxeak(int CCEN, String NOM, String NOME, String DGENRC, String DGENRE, String GENR, int MUNI,
                       String DMUNIC, String DMUNIE, String DTERRC, String DTERRE, String DEPE, String DTITUC, String DTITUE,
                       String DOMI, int CPOS, long TEL1, String TFAX, String EMAIL, String PAGINA, String COOR_X, String COOR_Y,
                       double LATITUD, double LONGITUD) {
        super();
        this.CCEN = CCEN;
        this.NOM = NOM;
        this.NOME = NOME;
        this.DGENRC = DGENRC;
        this.DGENRE = DGENRE;
        this.GENR = GENR;
        this.MUNI = MUNI;
        this.DMUNIC = DMUNIC;
        this.DMUNIE = DMUNIE;
        this.DTERRC = DTERRC;
        this.DTERRE = DTERRE;
        this.DEPE = DEPE;
        this.DTITUC = DTITUC;
        this.DTITUE = DTITUE;
        this.DOMI = DOMI;
        this.CPOS = CPOS;
        this.TEL1 = TEL1;
        this.TFAX = TFAX;
        this.EMAIL = EMAIL;
        this.PAGINA = PAGINA;
        this.COOR_X = COOR_X;
        this.COOR_Y = COOR_Y;
        this.LATITUD = LATITUD;
        this.LONGITUD = LONGITUD;
    }
    // Getters and Setters
    public int getCCEN() {
        return CCEN;
    }
    public void setCCEN(int CCEN) {
        this.CCEN = CCEN;
    }
    public String getNOM() {
        return NOM;
    }
    public void setNOM(String NOM) {
        this.NOM = NOM;
    }
    public String getNOME() {
        return NOME;
    }
    public void setNOME(String NOME) {
        this.NOME = NOME;
    }
    public String getDGENRC() {
        return DGENRC;
    }
    public void setDGENRC(String DGENRC) {
        this.DGENRC = DGENRC;
    }
    public String getDGENRE() {
        return DGENRE;
    }
    public void setDGENRE(String DGENRE) {
        this.DGENRE = DGENRE;
    }
    public String getGENR() {
        return GENR;
    }
    public void setGENR(String GENR) {
        this.GENR = GENR;
    }
    public int getMUNI() {
        return MUNI;
    }
    public void setMUNI(int MUNI) {
        this.MUNI = MUNI;
    }
    public String getDMUNIC() {
        return DMUNIC;
    }
    public void setDMUNIC(String DMUNIC) {
        this.DMUNIC = DMUNIC;
    }
    public String getDMUNIE() {
        return DMUNIE;
    }
    public void setDMUNIE(String DMUNIE) {
        this.DMUNIE = DMUNIE;
    }
    public String getDTERRC() {
        return DTERRC;
    }
    public void setDTERRC(String DTERRC) {
        this.DTERRC = DTERRC;
    }
    public String getDTERRE() {
        return DTERRE;
    }
    public void setDTERRE(String DTERRE) {
        this.DTERRE = DTERRE;
    }
    public String getDEPE() {
        return DEPE;
    }
    public void setDEPE(String DEPE) {
        this.DEPE = DEPE;
    }
    public String getDTITUC() {
        return DTITUC;
    }
    public void setDTITUC(String DTITUC) {
        this.DTITUC = DTITUC;
    }
    public String getDTITUE() {
        return DTITUE;
    }
    public void setDTITUE(String DTITUE) {
        this.DTITUE = DTITUE;
    }
    public String getDOMI() {
        return DOMI;
    }
    public void setDOMI(String DOMI) {
        this.DOMI = DOMI;
    }
    public int getCPOS() {
        return CPOS;
    }
    public void setCPOS(int CPOS) {
        this.CPOS = CPOS;
    }
    public long getTEL1() {
        return TEL1;
    }
    public void setTEL1(long TEL1) {
        this.TEL1 = TEL1;
    }
    public String getTFAX() {
        return TFAX;
    }
    public void setTFAX(String TFAX) {
        this.TFAX = TFAX;
    }
    public String getEMAIL() {
        return EMAIL;
    }
    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }
    public String getPAGINA() {
        return PAGINA;
    }
    public void setPAGINA(String PAGINA) {
        this.PAGINA = PAGINA;
    }
    public String getCOOR_X() {
        return COOR_X;
    }
    public void setCOOR_X(String COOR_X) {
        this.COOR_X = COOR_X;
    }
    public String getCOOR_Y() {
        return COOR_Y;
    }
    public void setCOOR_Y(String COOR_Y) {
        this.COOR_Y = COOR_Y;
    }
    public double getLATITUD() {
        return LATITUD;
    }
    public void setLATITUD(double LATITUD) {
        this.LATITUD = LATITUD;
    }
    public double getLONGITUD() {
        return LONGITUD;
    }
    public void setLONGITUD(double LONGITUD) {
        this.LONGITUD = LONGITUD;
    }
    @Override
    public String toString() {
        return "Ikastetxeak [CCEN=" + CCEN + ", NOM=" + NOM + ", NOME=" + NOME + ", DGENRC=" + DGENRC + ", DGENRE="
                + DGENRE + ", GENR=" + GENR + ", MUNI=" + MUNI + ", DMUNIC=" + DMUNIC + ", DMUNIE=" + DMUNIE
                + ", DTERRC=" + DTERRC + ", DTERRE=" + DTERRE + ", DEPE=" + DEPE + ", DTITUC=" + DTITUC + ", DTITUE="
                + DTITUE + ", DOMI=" + DOMI + ", CPOS=" + CPOS + ", TEL1=" + TEL1 + ", TFAX=" + TFAX + ", EMAIL="
                + EMAIL + ", PAGINA=" + PAGINA + ", COOR_X=" + COOR_X + ", COOR_Y=" + COOR_Y + ", LATITUD=" + LATITUD
                + ", LONGITUD=" + LONGITUD + "]";
    }


}

