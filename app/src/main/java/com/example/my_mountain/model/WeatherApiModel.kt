package com.example.my_mountain.model


import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class WeatherApiModel(
    @Element(name = "header")
    val header: Header,

    @Element(name = "body")
    val body: Body
)

@Xml(name = "header")
data class Header(
    @PropertyElement(name = "resultCode")
    val resultCode: String,

    @PropertyElement(name = "resultMsg")
    val resultMsg: String
)

@Xml(name = "body")
data class Body(
    @PropertyElement(name = "dataType")
    val dataType: String,

    @Element(name = "items")
    val items: Items,

    @PropertyElement(name = "numOfRows")
    val numOfRows: Int,

    @PropertyElement(name = "pageNo")
    val pageNo: Int,

    @PropertyElement(name = "totalCount")
    val totalCount: Int
)

@Xml(name = "items")
data class Items(
    @Element(name = "item")
    val itemList: List<Item>
)

@Xml(name = "item")
data class Item(
    @PropertyElement(name = "baseDate")
    val baseDate: String,

    @PropertyElement(name = "baseTime")
    val baseTime: String,

    @PropertyElement(name = "category")
    val category: String,

    @PropertyElement(name = "nx")
    val nx: Int,

    @PropertyElement(name = "ny")
    val ny: Int,

    @PropertyElement(name = "obsrValue")
    val obsrValue: Double
)


