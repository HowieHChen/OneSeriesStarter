package com.oneseries.starter.bean

data class CategoryBean(val name: String?){
    private val iconSet: MutableSet<String> = LinkedHashSet()

    val isEmpty: Boolean
        get() = iconSet.isEmpty()

    fun pushIcon(iconName: String){
        iconSet.add(iconName)
    }

    fun listIcons(): List<String> {
        val list: MutableList<String> = ArrayList(iconSet.size)
        list.addAll(iconSet)
        return list
    }

}
