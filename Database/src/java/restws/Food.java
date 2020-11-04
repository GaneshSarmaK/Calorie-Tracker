/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package restws;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author ganesh
 */
@Entity
@Table(name = "FOOD")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Food.findAll", query = "SELECT f FROM Food f")
    , @NamedQuery(name = "Food.findByFoodId", query = "SELECT f FROM Food f WHERE f.foodId = :foodId")
    , @NamedQuery(name = "Food.findByFoodName", query = "SELECT f FROM Food f WHERE f.foodName = :foodName")
    , @NamedQuery(name = "Food.findByCategory", query = "SELECT f FROM Food f WHERE f.category = :category")
    , @NamedQuery(name = "Food.findByCalorieAmount", query = "SELECT f FROM Food f WHERE f.calorieAmount = :calorieAmount")
    , @NamedQuery(name = "Food.findByServingUnit", query = "SELECT f FROM Food f WHERE f.servingUnit = :servingUnit")
    , @NamedQuery(name = "Food.findByServingAmount", query = "SELECT f FROM Food f WHERE f.servingAmount = :servingAmount")
    , @NamedQuery(name = "Food.findByFat", query = "SELECT f FROM Food f WHERE f.fat = :fat")})
public class Food implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "FOOD_ID")
    private Integer foodId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "FOOD_NAME")
    private String foodName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "CATEGORY")
    private String category;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CALORIE_AMOUNT")
    private int calorieAmount;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 10)
    @Column(name = "SERVING_UNIT")
    private String servingUnit;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Column(name = "SERVING_AMOUNT")
    private BigDecimal servingAmount;
    @Basic(optional = false)
    @NotNull
    @Column(name = "FAT")
    private int fat;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "foodId")
    private Collection<Consumption> consumptionCollection;

    public Food() {
    }

    public Food(Integer foodId) {
        this.foodId = foodId;
    }

    public Food(Integer foodId, String foodName, String category, int calorieAmount, String servingUnit, BigDecimal servingAmount, int fat) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.category = category;
        this.calorieAmount = calorieAmount;
        this.servingUnit = servingUnit;
        this.servingAmount = servingAmount;
        this.fat = fat;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(int calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public BigDecimal getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(BigDecimal servingAmount) {
        this.servingAmount = servingAmount;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    @XmlTransient
    public Collection<Consumption> getConsumptionCollection() {
        return consumptionCollection;
    }

    public void setConsumptionCollection(Collection<Consumption> consumptionCollection) {
        this.consumptionCollection = consumptionCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (foodId != null ? foodId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Food)) {
            return false;
        }
        Food other = (Food) object;
        if ((this.foodId == null && other.foodId != null) || (this.foodId != null && !this.foodId.equals(other.foodId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "restws.Food[ foodId=" + foodId + " ]";
    }
    
}
