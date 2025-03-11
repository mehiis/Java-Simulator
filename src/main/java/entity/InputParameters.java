package entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Entity class representing an entry in the "inputs" table.
 * This class maps the simulation input parameters stored in the database.
 * @Column annotations are used to map the fields to the corresponding columns in the database.
 * @Entity annotation is used to mark this class as an entity class.
 * @Table annotation is used to specify the name of the table in the database.
 * @Id annotation is used to mark the id field as the primary key.
 * @GeneratedValue annotation is used to specify the generation strategy for the primary key.
 */
@Entity
@Table(name="inputs")
public class InputParameters {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    private LocalDate date;

    @Column(name="simulation_time")
    private int simulationTime;

    @Column(name="mean_trash_per_throw")
    private double meanTrashPerThrow;

    @Column(name="truck_arrival_interval")
    private int truckArrivalInterval;

    @Column(name="single_apt_amount")
    private int singleAptAmount;

    @Column(name="double_apt_amount")
    private int doubleAptAmount;

    @Column(name="triple_apt_amount")
    private int tripleAptAmount;

    @Column(name="quad_apt_amount")
    private int quadAptAmount;

    @Column(name="mixed_amount")
    private int mixedAmount;

    @Column(name="plastic_amount")
    private int plasticAmount;

    @Column(name="bio_amount")
    private int bioAmount;

    @Column(name="glass_amount")
    private int glassAmount;

    @Column(name="paper_amount")
    private int paperAmount;

    @Column(name="metal_amount")
    private int metalAmount;

    public InputParameters(LocalDate date, int simulationTime, double meanTrashPerThrow, int truckArrivalInterval, int singleAptAmount, int doubleAptAmount, int tripleAptAmount, int quadAptAmount, int mixedAmount, int plasticAmount, int bioAmount, int glassAmount, int paperAmount, int metalAmount) {
        super();
        this.date                 = date;
        this.simulationTime       = simulationTime;
        this.meanTrashPerThrow    = meanTrashPerThrow;
        this.truckArrivalInterval = truckArrivalInterval;
        this.singleAptAmount      = singleAptAmount;
        this.doubleAptAmount      = doubleAptAmount;
        this.tripleAptAmount      = tripleAptAmount;
        this.quadAptAmount        = quadAptAmount;
        this.mixedAmount          = mixedAmount;
        this.plasticAmount        = plasticAmount;
        this.bioAmount            = bioAmount;
        this.glassAmount          = glassAmount;
        this.paperAmount          = paperAmount;
        this.metalAmount          = metalAmount;
    }

    /**
     * Default constructor for hibernate to instantiate empty objects.
     */
    public InputParameters() {

    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public int getSimulationTime() { return simulationTime; }
    public void setSimulationTime(int simulationTime) { this.simulationTime = simulationTime; }

    public double getMeanTrashPerThrow() { return meanTrashPerThrow; }
    public void setMeanTrashPerThrow(double meanTrashPerThrow) { this.meanTrashPerThrow = meanTrashPerThrow; }

    public int getTruckArrivalInterval() { return truckArrivalInterval; }
    public void setTruckArrivalInterval(int truckArrivalInterval) { this.truckArrivalInterval = truckArrivalInterval; }

    public int getSingleAptAmount() { return singleAptAmount; }
    public void setSingleAptAmount(int singleAptAmount) { this.singleAptAmount = singleAptAmount; }

    public int getDoubleAptAmount() { return doubleAptAmount; }
    public void setDoubleAptAmount(int doubleAptAmount) { this.doubleAptAmount = doubleAptAmount; }

    public int getTripleAptAmount() { return tripleAptAmount; }
    public void setTripleAptAmount(int tripleAptAmount) { this.tripleAptAmount = tripleAptAmount; }

    public int getQuadAptAmount() { return quadAptAmount; }
    public void setQuadAptAmount(int quadAptAmount) { this.quadAptAmount = quadAptAmount; }

    public int getMixedAmount() { return mixedAmount; }
    public void setMixedAmount(int mixedAmount) { this.mixedAmount = mixedAmount; }

    public int getPlasticAmount() { return plasticAmount; }
    public void setPlasticAmount(int plasticAmount) { this.plasticAmount = plasticAmount; }

    public int getBioAmount() { return bioAmount; }
    public void setBioAmount(int bioAmount) { this.bioAmount = bioAmount; }

    public int getGlassAmount() { return glassAmount; }
    public void setGlassAmount(int glassAmount) { this.glassAmount = glassAmount; }

    public int getPaperAmount() { return paperAmount; }
    public void setPaperAmount(int paperAmount) { this.paperAmount = paperAmount; }

    public int getMetalAmount() { return metalAmount; }
    public void setMetalAmount(int metalAmount) { this.metalAmount = metalAmount; }
}
