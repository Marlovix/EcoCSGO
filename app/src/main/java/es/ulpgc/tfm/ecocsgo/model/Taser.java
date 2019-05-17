package es.ulpgc.tfm.ecocsgo.model;

public class Taser extends Equipment {
    public Taser(String key){
        super("utilities", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GEAR
        };
    }
}
