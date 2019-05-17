package es.ulpgc.tfm.ecocsgo.model;

public class Grenade extends Equipment {
    public Grenade(String key){
        super("grenades", key);
        this.acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.GRENADE
        };
    }
}
