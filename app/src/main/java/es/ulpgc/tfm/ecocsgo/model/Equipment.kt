package es.ulpgc.tfm.ecocsgo.model

abstract class Equipment {
    //var referenceKey: String = ""
    var cost: String = ""
    var name: String = ""
    var numeration: HashMap<String, HashMap<String, String>>? = null
    var team: String = ""
    //var acceptedCategories: String = ""
}