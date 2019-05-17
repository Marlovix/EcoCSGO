package es.ulpgc.tfm.ecocsgo.model;

public class MainGun extends Gun {
    public MainGun(String key){
        super(key);
        acceptedCategories = new EquipmentCategory[]{
                EquipmentCategory.HEAVY, EquipmentCategory.SMG, EquipmentCategory.RIFLE
        };
    }
}
