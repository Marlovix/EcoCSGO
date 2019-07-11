package es.ulpgc.tfm.ecocsgo.model

class Equipment {
    var referenceKey: String = ""
    var cost: Int = 0
    var name: String = ""
    var numeration: HashMap<HashMap<String, Int>, Int>? = null
    var reward: Int = 0
    var team: Int = 0
    var acceptedCategories: Int = 0
}